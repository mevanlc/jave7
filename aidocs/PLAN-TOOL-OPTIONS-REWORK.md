# Tool Options Rework — Master Plan

> **Status:** master plan only. Phase docs (`PLAN-TOOL-OPTIONS-REWORK-PHASE0.md`,
> `…-PHASE1.md`, …) are not yet written. Class/interface names below are
> **conceptual placeholders** for communication, not refactoring proposals.

## Summary

Today, every drawing tool's options live behind a single floating
`ToolOptionsDialog` (`src/main/java/de/jave/jave/tool/dialog/ToolOptionsDialog.java`).
That dialog has chronic UX issues on macOS — most notably focus-steal —
because Swing's `JDialog` is not backed by `NSPanel` and AppKit refuses the
non-activating-panel style mask on plain `NSWindow`s (see Background).

The rework: widen the left tool-selector bar, render each tool's options
**inline in that bar**, and reserve the floating dialog for the small
minority of options that legitimately need a larger drawing area
(the "Edit X…" dialogs invoked from inline buttons). Because most option
editing then happens inline, the user invokes the floating dialog much less
often, and the residual focus-steal becomes a tolerable rough edge instead
of a daily annoyance.

## Goals

- Render most tool options inline in a widened ToolSelectorBar.
- Constant ToolSelectorBar width across tool changes — no jumping.
- Phased migration covering all 21 tools.
- Keep the legacy floating dialog functional throughout the transition; tools
  whose options have not yet migrated continue to use it as today.
- Reduce — not eliminate — exposure to the macOS focus-steal issue, by making
  inline editing the dominant interaction.

## Non-goals

- True `NSPanel`-backed floating windows (probed; not feasible without
  hacky JDK-internal poking — see Background).
- Refactoring `Tool`, `JaveMainPanel`, or the `ToolManager` API surface.
- Introducing new layout libraries (MigLayout, GBL, etc.).
- Internationalization-aware re-measurement at runtime (restart required if
  language changes alter widget widths).

## Background — why not just fix the floating dialog?

A standalone JNA probe (`/tmp/jdialog-nspanel-probe/Probe.java`) confirmed:

- Both `JFrame` and `JDialog` (modal, modeless, ownerless) on macOS come back
  from `[NSApp windows]` as `AWTWindow_Normal` — an `NSWindow` subclass, not
  `NSPanel`.
- Setting `NSWindowStyleMaskNonactivatingPanel` (`0x80`) via `setStyleMask:`
  is silently rejected on every one of these windows. That bit is `NSPanel`-
  only and AppKit drops it on plain `NSWindow`.

So the clean "flip a style-mask bit" path to true non-activating-panel
behavior is dead. Remaining options to make the floating dialog less
focus-thievish are all partial:

1. `setLevel:NSFloatingWindowLevel` (=3) — easy, makes window float above
   other app windows but does not prevent activation/focus-steal.
2. `setHidesOnDeactivate:NO` plus the existing `toFront()`+`requestFocus()`
   dance (`ToolOptionsDialog.java:83-84`) — what the codebase already does.
3. Method-swizzling `canBecomeKeyWindow` via JNA — rejected (too hacky,
   fragile across JDK updates).
4. Replacing the underlying `NSWindow` with an `NSPanel` — rejected (very
   fragile, breaks across JDK updates).

This rework instead moves the bulk of option editing **out** of the floating
dialog into the always-visible left bar. Options 1 and 2 are kept on the
table for the residual "Edit X…" dialogs in a late phase.

## Architecture

### ToolSelectorBar (the West-docked panel — today's `actions/ToolBar.java`)

Top-to-bottom layout in the new world:

1. Tool button matrix — refactored from the current 2-column
   `GridDialogLayout` (`ToolBar.java:118`) to a 4-column grid. Same buttons,
   same separators between tool groups.
2. Existing horizontal-line separators between tool groups (preserved).
3. **New `HorizontalLine` divider.**
4. **Inline tool-options host region** — bottommost segment, swaps content
   per active tool.

The current bottom-of-bar app-option toggles (`cbWatermark`, `cbAuxLines`,
`cbGrid`, `cbPure`, `cbPixel`, `ToolBar.java:147-156`) are **removed** from
the ToolSelectorBar. See "Top Toolbar" and "Watermark" / "AuxiliaryLines"
sections for their new homes.

### Top Toolbar (the North-docked `actions/JaveTopToolbar.java`)

Stays a single `FlowLayout` row. Three new `JToggleButton`s appended
(after the zoom group, before About):

- Toggle Grid — bound to `platePreferences.getGridVisibilityModel()`.
- Toggle Mark Illegal — bound to `platePreferences.getMarkIllegalModel()`.
- Toggle Connected Lines View — bound to `platePreferences.getConnectedLinesViewModel()`.
- Toggle Aux Lines Visibility — bound to `AuxiliaryLinesTool`'s enabled
  flag, decoupled from tool selection.

Icons already exist for the first three (`JaveIcons.GRID_VISIBLE_ICON`,
`PURE_ASCII_ICON`, `CONNECTED_LINES_VIEW_ICON`); the AuxLines visibility
toggle will reuse the existing AuxLines tool icon (or get a new one — TBD).

`ButtonToolbarBuilder` already accepts `AbstractButton`s
(`ButtonToolbarBuilder.java:39`), so no structural change to the builder is
needed.

### View Menu additions

- `Edit Watermark…` — opens the existing watermark options dialog
  (modeless, same JNA treatment as other floating dialogs in a late phase).
- `Show Watermark` — checkable item, bound to `IWatermarkPainter.setEnabled`.
- `Show Auxiliary Lines` — checkable item mirroring the new top-toolbar
  visibility toggle (menu mirror for completeness).

### Watermark — promoted out of the ToolSelectorBar

`WatermarkTool` (`ToolBar.TOOL_COUNT` index 19) and the paired `cbWatermark`
checkbox are **removed from the ToolSelectorBar entirely.** Reachable only
via the two new View menu items. Rationale: watermarks are set-and-forget;
they don't merit a permanent slot in the always-visible tool palette.

### AuxiliaryLines — split across spaces

`AuxiliaryLinesTool` (index 20) **stays** in the ToolSelectorBar as a plain
unadorned tool button. The current paired `cbAuxLines` checkbox in the bar
is **removed**.

Visibility decouples from tool selection:

- New top-toolbar toggle (alongside grid / mark-illegal / connected-lines).
- New View menu mirror item.
- Selecting the AuxLines tool button still auto-enables visibility (current
  behavior preserved).

Why split asymmetrically with Watermark: AuxLines genuinely is a
"during-drawing" overlay edited more often than Watermark; it earns the
spatial promotion. Watermark is set-and-forget; it earns the menu-only
treatment. This dissolves the current "tool button + paired checkbox"
oddity from the ToolSelectorBar without flattening both tools to the
same shape.

### Inline tool-options contract (placeholder names)

```java
interface IInlineToolOptions {
    JComponent getContent();
}
```

Each migrated tool exposes one `IInlineToolOptions`. The
`ToolSelectorBarOptionsHost` (placeholder name for the new bottom-of-bar
region) swaps `getContent()` into its center as the active tool changes.

### Width measurement (runtime, no hardcoded pixels)

JavE already uses `LayoutUtilities.getDpiAdjusted` and supports DPI
switches; hardcoded pixel widths would silently undermine that. So:

- Run a measurement pass **after L&F install, before `frame.pack()`**
  (between `JaveTopToolbar`/`ToolBar` construction at
  `JavEApplication.java:169-170` and the content-pane add at line 178).
- For each registered `IInlineToolOptions`:
  1. Build a hidden, undecorated `JFrame`.
  2. `add(panel)`, then `pack()`. `pack()` triggers `addNotify()` and
     `validate()` even when the frame is never shown — peers created,
     fonts resolved, layout runs.
  3. Read `panel.getPreferredSize().width`.
  4. `dispose()` the frame.
- ToolSelectorBar width = `max(measured widths) + padding`. **Locked for
  the session** — no remeasure on LAF or i18n change (those require restart).
- **Fallback** for any panel returning 0 (HTML labels, exotic custom
  components): off-screen-flash trick —
  `setLocation(-20000, -20000)` → `setVisible(true)` → `setVisible(false)` →
  `dispose()`. Off-screen, never visible to the user on a normal monitor
  setup. Documented as the escape hatch; not the default.

If the measured max width exceeds whatever feels reasonable in a quick
visual review, that is the **signal that the widest panel needs to be
decomposed** — extract a "size-hog" subset behind an "Edit X…" button and
re-measure. There is no hardcoded ceiling.

### Legacy `ToolOptionsDialog` during the migration

- Stays fully functional for unmigrated tools.
- For migrated tools: shows a hint-label-only panel
  ("Options shown in the toolbar"). The dialog is not removed and the View
  menu / status-bar toggle continue to work for it; users who pop it open
  on a migrated tool see a discoverable hint rather than an empty surface.
- `Cmd/Ctrl+T` accelerator added to the existing
  `getToolOptionsDialogToggleAction()` (PHASE0). `Cmd+T` is currently
  unbound in `JaveKeyBindings.java`.

When the migration is complete (all 21 tools have inline panels), revisit
whether to keep the legacy dialog at all. That decision is deferred
(potentially behind a user preference — see "Deferred" below).

## House conventions for new code

- **Layout: `GridDialogLayout`.** Matches existing house style
  (`ToolBar.java:118`, `PixelPlateOptionsPanel`, etc.). Do not introduce
  MigLayout, GroupLayout, or hand-rolled GridBagLayout.
- **Group like-behaving widgets in nested `JPanel`s** rather than expressing
  all structure via constraints. Plywood-and-2x4s, not ABC blocks.
- **Never `.setSize()`.** Let layout negotiation choose values.
- **`setPreferredSize` only on leaf widgets** that need a non-intrinsic
  size (e.g., the `Dimension(10, 6)` character-cell area in
  `PatternFillOptionsPanel.java:69`). Do not set it on container `JPanel`s
  — that defeats the runtime measurement pass.
- **All pixel literals** wrap in `LayoutUtilities.getDpiAdjusted(...)`.
- **No `minimumSize` / `maximumSize`** unless guarding a genuinely
  pathological case.

## Naming conventions (all tentative — change anytime)

- Inline panel impls: `<Tool>InlineToolOptionsPanel` (e.g., `LineInlineToolOptionsPanel`).
- Host region inside the bar: `ToolSelectorBarOptionsHost`.
- Contract interface: `IInlineToolOptions`.
- Focused popup dialogs invoked from inline "Edit X…" buttons:
  `<Tool><Subset>EditDialog` (e.g., `FillToolPatternEditDialog`).

## Decomposition rule for legacy panels

The general principle when migrating a legacy options panel to inline form:

1. **Fits inline at the negotiated bar width →** render inline.
2. **Doesn't fit (size-hog or benefits from being resizable) →** extract
   behind an "Edit X…" button that pops a focused `JDialog` hosting only
   that subset.
3. **Tabbed legacy panels** (e.g., `PatternFillOptionsPanel`): tabs are not
   preserved by default. Each tab becomes either an inline section or its
   own "Edit X…" dialog. Whether a tab fits is decided at panel-design time.
4. **Modality choices within a tool** (e.g., FillTool: Solid / Pattern /
   Gradient): expressed inline as a row of toggle buttons, a segmented
   control, a combobox, or a list — decided per tool at migration time.
   Not pre-specified here.
5. **Conditional visibility within a panel** (e.g., the "Solid Fill char"
   1-char input box should hide when in Gradient mode) — decided per panel
   at migration time.

## Phase index

### PHASE0 — infrastructure (next doc to write)

`aidocs/PLAN-TOOL-OPTIONS-REWORK-PHASE0.md` — to be written next.

Scope:

- Move the three view toggles (grid / mark-illegal / connected-lines) to
  `JaveTopToolbar` as `JToggleButton`s.
- Introduce the AuxLines visibility split: top-toolbar toggle + View menu
  mirror, keep tool button in ToolSelectorBar, drop the paired checkbox.
- Move Watermark to View menu (Edit + Show); drop tool button #19 and
  `cbWatermark` from the bar.
- Restructure the ToolSelectorBar tool button matrix from 2 cols to 4 cols.
- Add the `HorizontalLine` divider and the new
  `ToolSelectorBarOptionsHost` region.
- Wire the `IInlineToolOptions` contract.
- Implement the runtime width measurement pass (hidden-frame + `pack()`).
- Register a single placeholder `IInlineToolOptions`
  ("Inline options pending — use Tool Options Dialog") so the seam is
  observable end-to-end before any real tool migrates.
- Legacy `ToolOptionsDialog`: hint-label panel for tools registered as
  having inline options (initially only the placeholder tool).
- Add `Cmd/Ctrl+T` accelerator constant in `JaveKeyBindings` and apply it
  to `getToolOptionsDialogToggleAction()`.
- Verify no regressions in tool selection, dialog visibility, or drawing
  behavior.

PHASE0 visual proof point: end-to-end visible reveal — divider appears,
host region appears, placeholder content appears, bar width measurement
visibly works, top toolbar shows new toggles, View menu shows Watermark
items, AuxLines visibility toggle works independently of tool selection,
`Cmd+T` toggles legacy dialog.

### PHASE1 — first tool migrations (later doc)

Tools (10 total: 6 generic + 4 algorithmic):

- Generic: `FreehandToolGeneric`, `LineToolGeneric`, `RectangleToolGeneric`,
  `EllipseToolGeneric`, `BezierToolGeneric`, `ArcToolGeneric`.
- Algorithmic: `FreehandAlgorithmicTool`, `LineAlgorithmicTool`,
  `RectangleAlgorithmicTool`, `EllipseAlgorithmicTool`.

Approach:

- One inline panel class per tool initially. Refactor toward shared panels
  later only if duplication is severe enough to justify the indirection.
- The 6 generic tools today funnel through `PixelPlateOptionsPanel.configure(...)`
  (`FreehandToolGeneric.java:49`, `LineToolGeneric.java:41`, etc.) and the
  Rect/Ellipse generics add a `cbFill` "Fill" checkbox. Likely fits inline;
  measurement pass will confirm.
- The 4 algorithmic tools each have their own legacy `*OptionsPanel`
  (`LineAlgorithmicOptionsPanel`, etc.); each gets a from-scratch inline
  rewrite preserving functional behavior.

### PHASE2+ — TBD (one-liners)

Separate phase docs deferred until needed. Likely groupings:

- Selection / FreehandSelection / Text / FIGlet inline panels.
- Brush / Eraser / Fill / Clone / Pan inline panels (Fill is the largest
  decomposition; will likely produce the first real "Edit X…" dialog).
- WatermarkTool / AuxiliaryLinesTool inline-options content (the PHASE0
  changes only relocate them — their option editing UIs may also benefit
  from the new shape later).
- JNA helper: `setLevel:NSFloatingWindowLevel` and
  `setHidesOnDeactivate:YES` applied to residual "Edit X…" dialogs.
- User preference to bring back the legacy "everything in floating dialog"
  mode (only if user demand materializes).

## Deferred / explicitly out of scope

- True `NSPanel`-backed floating windows (Swing+macOS doesn't support
  cleanly; probed and confirmed dead).
- Method-swizzling `canBecomeKeyWindow` via JNA (rejected — too hacky).
- Re-measure on LAF / language change (restart required for now).
- Dynamic re-flowing tool button grid (4-col fixed for now).
- Renaming legacy classes — placeholder names in this doc are
  communication aids only.

## Open design questions for later phases

- "Edit X…" dialog mechanics: per-tool singleton vs new-per-click; remember
  position via preferences? (Decided at PHASE2+ when the first real one lands.)
- Modality presentation per tool (toggle row / segmented / combobox / list)
  — decided per tool at migration time.
- Inline-host content for tools that have **no options today** (PanTool,
  simple line tools): placeholder hint label by default; revisit if a
  better answer emerges.

## References

- Legacy floating dialog: `src/main/java/de/jave/jave/tool/dialog/ToolOptionsDialog.java`
- Today's ToolSelectorBar: `src/main/java/de/jave/jave/actions/ToolBar.java`
- Top toolbar: `src/main/java/de/jave/jave/actions/JaveTopToolbar.java`
- Toolbar builder (already accepts new toggles): `src/main/java/de/jave/jave/actions/ButtonToolbarBuilder.java`
- Key bindings registry: `src/main/java/de/jave/jave/actions/JaveKeyBindings.java`
- App wiring (where bar/topbar/main-panel get docked): `src/main/java/de/jave/jave/JavEApplication.java:169-178`
- One-off NSPanel-backing probe: `/tmp/jdialog-nspanel-probe/Probe.java`
