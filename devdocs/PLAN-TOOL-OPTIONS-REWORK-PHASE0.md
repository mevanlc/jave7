# Tool Options Rework — PHASE0: Infrastructure

> **Status:** to be implemented. Class/interface names below are conceptual
> placeholders unless noted as "exists today" with a file path.
>
> **Master plan:** `aidocs/PLAN-TOOL-OPTIONS-REWORK.md` — vision, architecture,
> conventions. This doc is the PHASE0 implementation plan only.
>
> **Reviewer-noted gotchas this doc explicitly addresses** (so they don't
> regress out): startup auto-show of the legacy dialog, tool-index
> assumptions in `setTool(int)` callers, Watermark's role beyond
> "set-and-forget", checkbox-as-source-of-truth for overlay visibility,
> generic-tool shared `PixelPlateOptionsPanel` state, and ambiguity over
> what renders in the inline host for unmigrated tools.

## Summary

PHASE0 ships infrastructure. **No tool's options content migrates yet.** The
user-visible result at the end of PHASE0:

- Five new visibility toggles in the top toolbar (grid, mark-illegal,
  connected-lines, watermark, aux-lines), replacing the old bottom-of-bar
  checkboxes.
- The legacy floating `ToolOptionsDialog` no longer auto-shows at startup
  by default; visibility persists across launches.
- `Cmd/Ctrl+T` toggles the legacy dialog.
- Watermark is reachable only via View menu (`Edit Watermark…` + `Show
  Watermark`); no tool button, no paired checkbox.
- AuxLines remains a tool button in the left bar (now unadorned —
  no paired checkbox); its visibility lives in the top toolbar with a View
  menu mirror.
- The left bar's tool button matrix is 4 columns wide (was 2). Below it: a
  horizontal divider, then a new `ToolSelectorBarOptionsHost` region that
  shows a fallback "No inline options for this tool — use Tool Options
  Dialog (⌘T)" panel for every tool (since no tool has migrated yet).
- All existing programmatic flows that pass raw tool indices to
  `setTool(int)` continue to work unchanged.

## Goals

- Land all the structural changes the master plan calls for, without
  migrating any tool's options content.
- Decouple tool-array indexes from button positions, so PHASE0 (Watermark
  removal) and any future button-set changes don't break programmatic
  `setTool(int)` callers.
- Stop hitting the macOS focus-steal path on every launch.
- Establish the `IInlineToolOptions` contract, the host region, and the
  runtime width measurement pass.
- Provide a fallback inline-options rendering for tools that have not yet
  migrated, so the host region is never empty.

## Non-goals (PHASE0)

- Migrating any tool's options content to inline form (PHASE1+).
- Touching `Tool`, `JaveMainPanel`, or `ToolManager` API surfaces beyond
  adding one optional method to `Tool` (Unit J).
- Removing or modifying the legacy `ToolOptionsDialog` itself, beyond the
  startup-default change (Unit A) and the new hint-label branch (Unit L).
- The JNA helper for `setLevel:NSFloatingWindowLevel` (deferred — late
  phase).
- Resolving the generic-tools shared-state question
  (`GenericTool.java:12`'s static `PixelPlateOptionsPanel`). PHASE0's
  contract supports both possible PHASE1 answers; the choice is deferred.

## Work units

13 discrete change units. Suggested commit order at the end of "Cross-unit
ordering."

### Unit A — Persist legacy dialog visibility, default off

**Why.** Today `JavEApplication.java:121-122` defaults
`toolOptionsDialogVisibilityModel` to `true`, and `startupOptionsDialog()`
(`JavEApplication.java:302-310`) unconditionally calls `.show()` and
`.toFront()`. Every launch surfaces the focus-steal-prone floating dialog.
This must change before PHASE1 — PHASE1's value depends on the user not
being assaulted by the legacy dialog at startup.

**Change.**

- Add a persisted boolean preference for legacy-dialog visibility in
  `JaveApplicationPreferences` (mirror the existing
  `getToolDialogLocation` / `setToolDialogLocation` pattern).
- On startup, initialize `toolOptionsDialogVisibilityModel` from the
  persisted value; default to **`false`** on fresh installs.
- Subscribe a listener to the model that writes back to the preference
  whenever the user toggles the dialog.
- `startupOptionsDialog()` keeps its existing `show()` + `toFront()`
  calls — they no-op when the model is `false`, since
  `ToolOptionsDialog.setVisible` already gates on `model.getValue()`
  (`ToolOptionsDialog.java:113-115`).

**Acceptance.** Fresh install on macOS launches without the floating
dialog. Toggling visible (via `Cmd+T` from Unit M, or View menu), then
quit + relaunch, restores it visible.

### Unit B — Tool-index decoupling layer

**Why.** Units F, G, H remove WatermarkTool's button and reshape the
button matrix. Today `ToolBar.createTools()` (`ToolBar.java:219-232`)
iterates tool indices 0..20 and uses `buttonGroup.getSelectedIndex()`
**directly** as the tool index in the action. `selectToolButton(int)`
(`ToolBar.java:184`) treats its argument as a tool index passed straight
to `buttonGroup.setSelectedIndex`. Several call sites pass raw tool
indices: `setTool(10)`, `setTool(12)`, `setTool(14)`, `setTool(19)`
(`JavEApplication.java:375, 537, 545, 770, 776, 781, 789`). Removing
button #19 without a mapping layer would shift AuxLines from index 20 to
button position 19 and silently break those callers.

**Change.**

- Keep `tools[]` indices stable. `tools[19]` remains `WatermarkTool` even
  after its button is removed (Unit F). `tools[]` length stays 21.
- Inside `ToolBar`, introduce two structures populated as buttons are
  added:
  - `int[] buttonIndexToToolIndex` — sized to the number of buttons,
    populated in button-add order.
  - `Map<Integer,Integer> toolIndexToButtonIndex` (or equivalent sparse
    int[]) — inverse map; `null`/`-1` for tool indices with no button
    (post-PHASE0: tool 19, Watermark).
- Button action becomes:
  ```java
  ToolBar.this.application.setTool(
      ToolBar.this.buttonIndexToToolIndex[buttonGroup.getSelectedIndex()]);
  ```
- `selectToolButton(int toolIndex)` becomes:
  ```java
  Integer bi = toolIndexToButtonIndex.get(toolIndex);
  if (bi != null) buttonGroup.setSelectedIndex(bi);
  // tools without a button (Watermark post-PHASE0) silently no-op
  // for the button side; the rest of setTool() still wires the active tool.
  ```
- `JavEApplication.setTool(int)` (`JavEApplication.java:796-807`) is
  unchanged — still takes tool indices; `selectToolButton` translates
  internally.
- The public constants `BRUSH_TOOL_INDEX = 14`, `SELECTION_TOOL_INDEX =
  12`, `TEXT_TOOL_INDEX = 10`, `AUXILIARY_LINES_TOOL_INDEX = 20`,
  `WATERMARK_TOOL_INDEX = 19`, `DEFAULT_TOOL_INDEX = 0`
  (`ToolBar.java:60-66`) keep their semantics — they refer to tool
  indices, not button positions.

**Acceptance.** All existing `setTool(int)` call sites work as today.
Selecting AuxLines via its tool button selects `tools[20]`. Calling
`setTool(19)` (WatermarkTool) makes WatermarkTool the active tool —
`mainPanel.setCurrentTool(...)` is reached, mouse handlers
(`WatermarkTool.java:337`) become live — even though no button is
highlighted in the bar.

### Unit C — Shared visibility models for Watermark and AuxLines

**Why.** Today `cbWatermark` and `cbAuxLines` (`ToolBar.java:84-103`) are
both UI source-of-truth and the listener that calls
`IWatermarkPainter.setEnabled(...)`. Programmatic enables go through
`ToolBar.setWatermarkVisible(boolean)` /
`ToolBar.setAuxiliaryLinesVisible(boolean)` (`ToolBar.java:174-182`),
which update both the painter and the checkbox. With multiple
presentations (top-toolbar toggle, View menu mirror, programmatic, tool
selection), we need a shared `BooleanModel` per overlay so all views stay
synced. `AuxiliaryLinesTool.takeToHand()`
(`AuxiliaryLinesTool.java:586`) auto-enables visibility on tool selection
and must propagate to all observers, not just the painter.

**Change.**

- Two new `BooleanModel`s, owned by `JavEApplication` (transient — match
  how `toolOptionsDialogVisibilityModel` is owned; persistence is not a
  PHASE0 requirement for these):
  - `watermarkVisibilityModel`
  - `auxLinesVisibilityModel`
- Wire each model's change listener once at construction:
  ```java
  watermarkVisibilityModel.addChangeListener(() -> {
      ((IWatermarkPainter) tools[19]).setEnabled(watermarkVisibilityModel.getValue());
      mainPanel.repaint();
  });
  ```
  (Same shape for AuxLines.) These listeners are the **single** path
  from model → painter; no direct `setEnabled` calls anywhere else.
- `ToolBar.setWatermarkVisible(boolean)` /
  `ToolBar.setAuxiliaryLinesVisible(boolean)` (`ToolBar.java:174-182`)
  retain their public API but become thin shims that call
  `model.setValue(...)`. Existing callers
  (`JavEApplication.java:541, 546`) need no changes.
- `AuxiliaryLinesTool.takeToHand()` (`AuxiliaryLinesTool.java:586`):
  change the line that auto-enables visibility to call
  `auxLinesVisibilityModel.setValue(true)` instead of touching the
  painter or any checkbox directly. The model's listener propagates to
  the painter.
- Top-toolbar toggles (Unit E) and View menu items (Units E + G) bind
  directly to these models.

**Acceptance.** Toggling visibility from any source (top-toolbar button,
View menu item, programmatic `setWatermarkVisible(true)`, AuxLines tool
selection via button) updates **all** observers consistently. No
checkbox-as-source-of-truth remains.

### Unit D — Move three view toggles to top toolbar

**Why.** Master plan, "Top Toolbar" section. Frees vertical space in the
left bar.

**Change.**

- Remove from `ToolBar` the bottom-of-bar block (`ToolBar.java:104-156`,
  selectively): `cbGrid`, `cbPure`, `cbPixel` and their layout adds.
- Add to `JaveTopToolbar` (after the zoom group, before About) as
  `JToggleButton`s bound to the existing models:
  - `platePreferences.getGridVisibilityModel()` →
    `JaveIcons.GRID_VISIBLE_ICON`
  - `platePreferences.getMarkIllegalModel()` →
    `JaveIcons.PURE_ASCII_ICON`
  - `platePreferences.getConnectedLinesViewModel()` →
    `JaveIcons.CONNECTED_LINES_VIEW_ICON`
- `ButtonToolbarBuilder.add(AbstractButton)` already accepts these
  (`ButtonToolbarBuilder.java:39-42`) and applies the Mac-sized 28×28
  dimensions via `adjustButton`.
- `ButtonToolbarBuilder.addSeparator()` between the zoom group and the
  new toggle group.

**Acceptance.** Each toggle's effect on the canvas matches the old
checkboxes exactly.

### Unit E — Watermark + AuxLines visibility toggles in top toolbar

**Why.** Master plan. The four overlay-visibility toggles want to live
together.

**Change.**

- Append two more `JToggleButton`s after the Unit D group (no separator
  between — they form one logical visibility group):
  - Watermark visibility, bound to `watermarkVisibilityModel` (Unit C).
  - AuxLines visibility, bound to `auxLinesVisibilityModel` (Unit C).
- Icons: reuse the existing watermark and aux-lines tool icons. If
  visual confusion proves a problem, swap to dedicated visibility icons
  in a follow-up.

**Acceptance.** Clicking each toggle changes overlay visibility without
changing the active tool.

### Unit F — Remove orphaned widgets from the ToolSelectorBar

**Why.** Units D and E moved their functions; the ToolSelectorBar entries
for `cbWatermark`, `cbAuxLines`, `cbGrid`, `cbPure`, `cbPixel`
(`ToolBar.java:147-156`) and the WatermarkTool button (`tools[19]`'s
button) can now be deleted.

**Change.**

- Remove from `ToolBar`:
  - `cbWatermark`, `cbAuxLines` field declarations and ItemListener
    wiring (`ToolBar.java:69-103`) — superseded by Unit C's models.
  - `cbGrid`, `cbPure`, `cbPixel` (already gone via Unit D).
  - The button-add for tool index 19 (Watermark): in
    `createTools()` (`ToolBar.java:219-232`), iterate only the indices
    that get buttons; tool 19 is excluded, tool 20 is included.
- Layout cleanup: remove the `panel.add(this.cbWatermark)`,
  `panel.add(this.buttonGroup.getButton(19))`,
  `panel.add(this.cbAuxLines)`, the bottom horizontal lines and gaps
  related to the removed widgets. Adjust separator gaps to keep
  visual balance.

**Acceptance.** The bottom of the left bar (above the new divider from
Unit I) ends after the AuxLines tool button row. No checkboxes, no
Watermark button visible. AuxLines tool button is present and selectable.

### Unit G — Watermark to View menu

**Why.** Master plan, Watermark section. The reviewer's note: Watermark
is **not** purely set-and-forget — `JavEApplication.setWatermarkImage`
and `doLoadWatermark` (`JavEApplication.java:536-549`) explicitly call
`setTool(19)` so the user can move/resize the watermark via
`WatermarkTool`'s mouse handlers (`WatermarkTool.java:337`). The View
menu item must replicate this affordance.

**Change.**

- Add to `JaveMenuBar`'s View menu:
  - **`Edit Watermark…`** — action that:
    1. Opens the watermark options dialog modeless (the same dialog
       reachable today by clicking the old #19 tool button).
    2. Calls `application.setTool(19)` to activate WatermarkTool's
       canvas mouse handlers, so the user can move/resize the
       watermark immediately. Note: post-PHASE0 there is no button to
       highlight; `selectToolButton` (Unit B) silently no-ops on the
       button side, but the tool is still set as current via the rest
       of `setTool`.
    3. Calls `watermarkVisibilityModel.setValue(true)` (Unit C) so the
       user can see what they're editing.
  - **`Show Watermark`** — `JCheckBoxMenuItem` bound to
    `watermarkVisibilityModel`.
  - **`Show Auxiliary Lines`** — `JCheckBoxMenuItem` bound to
    `auxLinesVisibilityModel` (the menu mirror of the top-toolbar
    toggle from Unit E).
- Locate the new items in View menu near existing visibility-related
  items if any exist; otherwise group at the bottom of View.

**Acceptance.** `View → Edit Watermark…` activates WatermarkTool (canvas
mouse handlers live, watermark visible, options dialog open) — equivalent
to the pre-PHASE0 experience of clicking the #19 tool button + the
`cbWatermark` checkbox in one action. The two `Show …` items toggle
overlay visibility independently of tool selection. Existing
`setWatermarkImage` / `doLoadWatermark` flows continue to work.

### Unit H — Restructure tool button matrix 2 → 4 columns

**Why.** Master plan, ToolSelectorBar section.

**Change.**

- `ToolBar.createTools()` builds buttons in the order below. The
  generic-tools / algorithmic-tools header labels
  (`genericToolsLabel`, `algorithmicToolsLabel`,
  `ToolBar.java:114-117`) are **dropped** — the 4-col layout no longer
  maps onto a left-generic / right-algorithmic split, and the
  horizontal-line separators between groups carry the visual structure.

  | Group           | Tool indices             | Count |
  |-----------------|--------------------------|-------|
  | Generic         | 0, 2, 4, 6, 8, 9         | 6     |
  | Algorithmic     | 1, 3, 5, 7               | 4     |
  | Selection       | 10, 11, 12, 13           | 4     |
  | Brush           | 14, 15, 16, 17, 18       | 5     |
  | AuxLines        | 20                       | 1     |

- 4-column `GridDialogLayout` with `createHorizontalLine(...)` separators
  between groups (preserve existing style).
- Use `Gap` cells to keep groups aligned within rows where group sizes
  aren't multiples of 4 (e.g., generic = row of 4 + row of 2 + 2 gaps).
- Group ordering preserves today's vertical reading order.

**Acceptance.** All 18 visible tool buttons (was 19; Watermark removed
via Unit F) are present, grouped sensibly in a 4-wide grid. Selection
works for all of them. The four `*_TOOL_INDEX` constants
(`ToolBar.java:60-66`) still resolve to the same `tools[]` slots.

### Unit I — Add `HorizontalLine` divider + `ToolSelectorBarOptionsHost`

**Why.** Master plan, ToolSelectorBar section.

**Change.**

- After the AuxLines row (the bottommost tool button), add a
  `HorizontalLine(2)` divider matching `createHorizontalLine(...)`
  style.
- Below the divider, add a new `ToolSelectorBarOptionsHost` (placeholder
  name) — a `JPanel` with `BorderLayout`, intended to swap its center
  child per active tool.
- The host's preferred width participates in the bar's overall width.
  Per master plan and Unit K, the bar's width is set by the measurement
  pass; the host should not declare a `setPreferredSize` of its own.

**Acceptance.** Divider visible at the bottom of the tool button matrix,
host region present, initially showing the fallback panel from Unit J.

### Unit J — `IInlineToolOptions` contract + host plumbing

**Why.** Master plan contract section. Reviewer's note: the inline host
needs a defined behavior for unmigrated tools (most tools, in PHASE0).

**Change.**

- Define the interface (placeholder name):
  ```java
  public interface IInlineToolOptions {
      JComponent getContent();
  }
  ```
- Add an optional method to `Tool`:
  ```java
  /** Override to return an inline-options panel for this tool.
   *  Default returns null (tool has no inline options yet). */
  public IInlineToolOptions getInlineOptionsPanel() {
      return null;
  }
  ```
  In PHASE0, **no tool overrides this** — every call returns null. PHASE1
  is the first phase that overrides.
- `ToolSelectorBarOptionsHost` listens for tool changes and, on each
  change:
  - Calls `currentTool.getInlineOptionsPanel()`.
  - If non-null: removes its current center child and adds
    `inlineOpts.getContent()` to center; revalidates.
  - If null: replaces center with the **`FallbackInlineOptionsPanel`**
    (placeholder name) — a `JPanel` with a single short label, e.g.
    `"No inline options for this tool — use Tool Options Dialog (⌘T)."`
    The fallback is constructed once and reused.
- Hookup point: extend `JavEApplication.setTool(int)`
  (`JavEApplication.java:796-807`) to also call
  `toolSelectorBarOptionsHost.setTool(newTool)`, mirroring the existing
  `optionsDialog.setTool(newTool)` call at line 800.
- **Contract note** (document in interface Javadoc): a single
  `JComponent` returned by `getContent()` may be shared across multiple
  tools. The host removes-from-old-parent / adds-to-new-parent on each
  change; Swing supports this. PHASE1 will exercise this for the six
  generic tools that share a single static `PixelPlateOptionsPanel`
  (`GenericTool.java:12`).

**Acceptance.** With no tool overriding `getInlineOptionsPanel()` (PHASE0
state), every tool selection shows the fallback panel in the host.
Re-selecting the same tool is a no-op (no re-add, no flicker).

### Unit K — Runtime width measurement pass

**Why.** Master plan, width measurement section.

**Change.**

- New helper class (placeholder location)
  `de.jave.gui.layout.InlineOptionsWidthMeasurer`:
  ```java
  public final class InlineOptionsWidthMeasurer {
      public static int measureMaxWidth(List<JComponent> panels) {
          int max = 0;
          for (JComponent p : panels) {
              JFrame f = new JFrame();
              f.setUndecorated(true);
              f.add(p);
              f.pack();
              max = Math.max(max, p.getPreferredSize().width);
              f.dispose();
          }
          return max + LayoutUtilities.getDpiAdjusted(PADDING_PX);
      }
  }
  ```
  with a small constant `PADDING_PX` (e.g. 8).
- Call this from `JavEApplication.startup` after the toolbar is
  constructed (`JavEApplication.java:170`) but before the content-pane
  add at line 175. Pass it the fallback panel plus all
  `IInlineToolOptions` panels registered by overriding tools (in PHASE0,
  only the fallback). Set the result as the preferred width on
  `ToolSelectorBarOptionsHost`.
- Off-screen-flash fallback (for panels that return 0 width): document
  in code comments but do not implement for PHASE0 — no real panels are
  measured yet. PHASE1 implements the fallback if it's needed.

**Acceptance.** On launch, the bar's width is determined by the fallback
panel's measured width plus padding. Width does not jump when switching
tools (since only the fallback is shown in PHASE0).

### Unit L — Legacy dialog hint-label plumbing

**Why.** Master plan, legacy dialog section. Reviewer's note: PHASE0
adds the plumbing only; no tool triggers it yet.

**Change.**

- `ToolOptionsDialog.setTool(Tool tool)` (`ToolOptionsDialog.java:66-87`):
  branch on whether the tool has an inline options panel.
  ```java
  IInlineToolOptions inline = tool.getInlineOptionsPanel();
  if (inline != null) {
      // tool is migrated — show hint label instead of legacy options
      contentPane.removeAll();
      contentPane.add(buildHintLabelPanel(), BorderLayout.CENTER);
  } else {
      // unchanged legacy path
      contentPane.removeAll();
      contentPane.add(tool.getOptionsComponent(), BorderLayout.CENTER);
  }
  // ...existing pack() / title / focus calls follow
  ```
- `buildHintLabelPanel()` returns a small `JPanel` with a centered
  `JLabel`: `"Options shown in the toolbar."`
- In PHASE0, since no tool overrides `getInlineOptionsPanel()`, this
  branch is dead code that lights up in PHASE1.

**Acceptance.** PHASE0 behavior of the legacy dialog is unchanged for
every tool. Smoke-test PHASE1-readiness by temporarily overriding
`getInlineOptionsPanel()` on one tool during testing and verifying the
hint label renders.

### Unit M — `Cmd/Ctrl+T` accelerator

**Why.** Previously discussed.

**Change.**

- Add to `JaveKeyBindings.java`:
  ```java
  public static final KeyStroke TOGGLE_TOOL_OPTIONS_DIALOG =
      KeyStroke.getKeyStroke(KeyEvent.VK_T, MENU);
  ```
- Wire `setAcceleratorKey(JaveKeyBindings.TOGGLE_TOOL_OPTIONS_DIALOG)` on
  the `getToolOptionsDialogToggleAction()` SmartToggleAction
  (`JaveActions.java:197`).
- Verify the accelerator renders in the View menu item created at
  `JaveMenuBar.java:350` (it should auto-render via
  `ActionWidgetFactory.createToggleMenuItem`).

**Acceptance.** `Cmd+T` (macOS) / `Ctrl+T` (Windows/Linux) toggles the
legacy dialog. View menu item shows the accelerator. Toggled state
persists across launches (per Unit A).

## Cross-unit ordering

Hard dependencies:

1. **Unit B (tool-index decoupling)** must land **before or with** Units
   F (remove Watermark button), G (View-menu Watermark with `setTool(19)`
   call), and H (4-col grid that drops the Watermark button slot).
   Otherwise, removing button #19 silently shifts AuxLines and breaks
   raw-index `setTool(...)` callers.
2. **Unit C (visibility models)** must land **before** Units D, E, F, G
   (which bind to or rely on those models replacing the checkbox
   source-of-truth).
3. **Unit J (contract + host)** must land **before** Units K (measurement
   needs the fallback panel) and L (legacy dialog hint plumbing reads
   `getInlineOptionsPanel()`).
4. **Unit A** (startup default) is independent.
5. **Unit M** (Cmd+T) is independent and trivial.

Suggested commit order (smallest, most independently-revertible first;
visible UI rearrangement last):

1. **A** — startup default off + persist
2. **M** — `Cmd+T` accelerator
3. **B** — tool-index decoupling
4. **C** — visibility models + AuxLines `takeToHand` change
5. **J** — `IInlineToolOptions` contract + host shell + fallback panel
6. **L** — legacy dialog hint-label branch (dormant in PHASE0)
7. **K** — width measurement pass + apply to host
8. **D** — three view toggles into top toolbar
9. **E** — Watermark + AuxLines visibility toggles into top toolbar
10. **G** — three new View menu items
11. **F** — remove orphaned ToolSelectorBar widgets (Watermark button, all five checkboxes)
12. **H** — 4-col tool button matrix
13. **I** — divider + host placement in bar layout

## End-to-end acceptance criteria

- App launches without the floating dialog appearing (fresh install).
- `Cmd+T` toggles the dialog. Toggled state persists across launches.
- All `setTool(int)` call sites
  (`JavEApplication.java:375, 537, 545, 770, 776, 781, 789`) work as today.
- Top toolbar shows five new toggle buttons after the zoom group, in
  order: Grid, Mark Illegal, Connected Lines, Watermark Visibility,
  AuxLines Visibility. Each toggles its respective overlay.
- Bottom of left bar no longer contains any checkboxes.
- Left bar tool buttons are 4 wide. Watermark tool button is gone.
  AuxLines tool button is present and selectable; selecting it
  auto-enables AuxLines visibility (top toolbar toggle reflects the
  change).
- View menu has `Edit Watermark…`, `Show Watermark`, `Show Auxiliary
  Lines`. `Edit Watermark…` activates WatermarkTool (canvas mouse
  handlers live), turns watermark visibility on, and opens the watermark
  options dialog. The two `Show …` items toggle visibility independently
  of tool selection.
- Bottom of left bar shows a horizontal divider, then the
  `ToolSelectorBarOptionsHost` displaying the fallback panel
  (`"No inline options for this tool — use Tool Options Dialog (⌘T)."`).
- Bar width is constant across tool changes (no jumping); width is
  determined by the fallback panel's measured size + padding.
- Loading a watermark via existing flows
  (`JavEApplication.setWatermarkImage`, `doLoadWatermark`) still works:
  tool 19 active for canvas mouse interaction, visibility on, painter
  draws.

## Files touched (expected)

**New:**

- `src/main/java/de/jave/jave/tool/dialog/IInlineToolOptions.java`
- `src/main/java/de/jave/jave/tool/dialog/ToolSelectorBarOptionsHost.java`
- `src/main/java/de/jave/jave/tool/dialog/FallbackInlineOptionsPanel.java`
- `src/main/java/de/jave/gui/layout/InlineOptionsWidthMeasurer.java`

**Modified:**

- `JavEApplication.java` — startup default visibility (A), visibility
  models + listeners (C), measurement-pass call + host width application
  (K), `Edit Watermark…` action wiring (G), `setTool(int)` calls
  `host.setTool(newTool)` (J).
- `JaveApplicationPreferences.java` — persistence for legacy-dialog
  visibility (A).
- `actions/ToolBar.java` — index decoupling (B), removal of view
  checkboxes + Watermark button (F), 4-col grid + label removal (H),
  divider + host add (I), `setWatermarkVisible` /
  `setAuxiliaryLinesVisible` shim to models (C).
- `actions/JaveTopToolbar.java` — five new toggles (D, E).
- `actions/JaveMenuBar.java` — three new View menu items (G).
- `actions/JaveKeyBindings.java` — `TOGGLE_TOOL_OPTIONS_DIALOG`
  constant (M).
- `actions/JaveActions.java` — accelerator on toggle action (M).
- `tool/dialog/ToolOptionsDialog.java` — hint-label branch in
  `setTool` (L).
- `Tool.java` — optional `getInlineOptionsPanel()` method, default null
  return (J).
- `tool/auxiliarylines/AuxiliaryLinesTool.java` — `takeToHand()` calls
  the shared model instead of the painter directly (C).

## Open design questions resolved during PHASE0

- **Generic / algorithmic header labels** in 4-col layout: dropped. The
  4-col grid no longer maps onto a left-generic / right-algorithmic
  split; horizontal-line separators between groups carry the visual
  structure.
- **Watermark / AuxLines visibility-toggle icons** in top toolbar: reuse
  existing tool icons. Revisit only if visual confusion proves a
  problem.
- **Persistence of overlay visibility** (Watermark / AuxLines): not
  persisted in PHASE0 — these models are transient. (Persistence can be
  added later by switching to `JaveApplicationPreferences`-backed
  models.)

## Open design questions deferred to PHASE1+

- **Generic-tool state sharing.** The 6 generic tools currently share a
  single static `PixelPlateOptionsPanel` (`GenericTool.java:12`). PHASE1
  must decide: (a) extract a shared options model with per-tool view
  bindings, or (b) reuse the same `JComponent` as the
  `IInlineToolOptions.getContent()` return value across all 6 tools'
  `getInlineOptionsPanel()` overrides (Swing supports re-parenting on
  tool change). PHASE0's contract supports both.
- **Decomposition of legacy panels into inline + Edit-X dialogs** — per
  tool, decided at migration time.
- **JNA helper** for `setLevel:NSFloatingWindowLevel` and
  `setHidesOnDeactivate:YES` on residual "Edit X…" dialogs — late phase.
- **Whether the fallback hint label should mention `⌘T` literally on
  Windows/Linux** (where it's `Ctrl+T`), or use a platform-aware label.
  Defer until i18n / platform-string handling is reviewed in PHASE1+.
