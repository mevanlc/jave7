# Tool Options Rework — PHASE2: Sweep Migration

> **Status:** complete. The 9 tools in this phase's scope are migrated.
> The two tools this doc deferred (`WatermarkTool`, `FillTool`) were
> subsequently migrated inline in PHASE3 — see
> `devdocs/PLAN-TOOL-OPTIONS-REWORK-PHASE3.md`. All 21 tools now render
> their options inline; no tool relies on the legacy floating dialog.
>
> Builds on landed PHASE0 infrastructure (`IInlineToolOptions`,
> `ToolSelectorBarOptionsHost`, `InlineOptionsWidthMeasurer`,
> `ToolOptionsDialog` hint-label branch) and PHASE1 (10 tools migrated).
>
> **Master plan:** `aidocs/PLAN-TOOL-OPTIONS-REWORK.md`
> **Prerequisite phases:** PHASE0 (landed), PHASE1 (landed)

## Summary

PHASE2 sweeps the bulk of remaining tools — 9 of the 11 unmigrated —
into the inline contract. After PHASE2, only the two genuine size-hogs
(`WatermarkTool`, `FillTool`) remain in the legacy floating dialog.
Those two need real "Edit X…" decomposition work and warrant their own
phase (PHASE3).

User-visible result at the end of PHASE2:

- 19 of 21 tools render their options inline.
- Only Watermark and Fill still pop the floating dialog.
- Bar width remains constant across all tool switches.

## Tools in scope (9)

| # | Tool | Today's options shape | Migration shape |
|---|---|---|---|
| 1 | `PanTool` | none (uses `Tool` default "No options available" label) | none — host fallback already says the same thing |
| 2 | `AuxiliaryLinesTool` | single `JButton("Clear")` | lift into trivial inline panel |
| 3 | `EraserTool` | Style combo + Size spinner (2-col `GridDialogLayout`) | lift verbatim into `EraserOptionsPanel implements IInlineToolOptions` |
| 4 | `CloneTool` (extends `EraserTool`) | super's panel + `MergeCharactersPanel` | compose super's view + Merge below |
| 5 | `BrushTool` (extends `AbstractPencilTool`) | `brushPanel` (rebuilt on `setBrush`) + `MergeCharactersPanel` | lift; preserve dynamic rebuild via `revalidate()` of the inline content |
| 6 | `TextTool` | CursorMovement combo + `MergeCharactersPanel` | lift verbatim |
| 7 | `SelectionTool` | Paste-mode combo + Collision cb + 3D cb + Merge cb | lift verbatim |
| 8 | `FreehandSelectionTool` (extends `SelectionTool`) | inherits parent | inherits parent's `getInlineOptionsPanel` automatically |
| 9 | `FIGletTool` | already factored into `FigletToolOptionsPanel` | adapt that class to `implements IInlineToolOptions` |

## Tools explicitly deferred to PHASE3

| Tool | Why deferred |
|---|---|
| `WatermarkTool` | `SourceFilePanel` + position/size + Fit + brightness slider + negative cb — large enough to want decomposition. Also already slated for promotion-to-View-menu in master plan. |
| `FillTool` | Tabbed pane (Solid/Pattern/Gradient) + match-mode combo. Master plan calls this out as the first real "Edit X…" candidate. |

## Goals

- All 9 PHASE2 tools either override `getInlineOptionsPanel()` with a
  real implementation or — for tools with no options — deliberately
  fall back to the host's "No options available" panel (Pan).
- Selecting any of the 9 tools and opening the legacy dialog (`Cmd/Ctrl+T`)
  shows the PHASE0 hint label (or, for Pan, the legacy "No options"
  label, since `getInlineOptionsPanel` returns null).
- Functional behavior preserved: every option settable today via the
  legacy panel can be set via the inline panel, with same drawing result.
- `FreehandSelectionTool` automatically picks up `SelectionTool`'s
  inline panel via inheritance — no separate panel class.
- Bar width remains constant across all 21 tools. Width measurement
  pass naturally picks up the new panels via the existing PHASE1 ζ
  loop.

## Non-goals (PHASE2)

- Migrating `WatermarkTool` or `FillTool` (PHASE3).
- Promoting `WatermarkTool` to View-menu (master-plan PHASE0 item that
  was deferred — still deferred).
- The first real "Edit X…" focused dialog (PHASE3, with Fill).
- Removing the legacy `ToolOptionsDialog` class entirely.
- Removing `Tool.createOptionsComponent` infrastructure; still needed
  for Watermark + Fill until PHASE3.
- JNA window-level helpers for the residual floating dialog.

## Resolved design decisions

These extend PHASE1's D1–D4. New decisions D5–D9.

### D5 — Inheritance: rely on it for `FreehandSelectionTool`

`FreehandSelectionTool extends SelectionTool` and inherits
`createOptionsComponent` today. Doing the same with
`getInlineOptionsPanel` is the obvious move: do not override on
Freehand, let the JVM dispatch to `SelectionTool`'s implementation.

**Why.** It mirrors the legacy behavior exactly. The two tools share
state (`mergeCharactersModel`, `cb3d`, `chSelectionLayer`) since
Freehand inherits all `SelectionTool` fields and calls
`super.takeToHand()`. A separate inline panel for Freehand would
diverge confusingly from this state-sharing model.

**Caveat.** PHASE1 D2's measurement pass calls `getInlineOptionsPanel()`
once per tool. Both `SelectionTool` and `FreehandSelectionTool` will
return the same `IInlineToolOptions` instance (cached on the parent's
field). The measurer will measure the same `JComponent` twice — wasted
but harmless. Acceptable.

### D6 — Inheritance: compose for `CloneTool`

`CloneTool extends EraserTool` and today calls
`super.createOptionsComponent()` then wraps it with a Merge panel
(`CloneTool.java:32-38`). The inline-equivalent is:
`CloneTool.buildInlineOptions()` calls `super.getInlineOptionsPanel().getContent()`,
wraps that JComponent + a fresh `MergeCharactersPanel.getContent()` in
a 1-col `GridDialogLayout` JPanel.

**Why.** Same composition as today, just renamed. Avoids duplicating
the eraser style-combo and size-spinner construction.

**Caveat.** The super's content `JComponent` is a single instance
cached on the super — taking ownership in a Clone subclass means
EraserTool's *own* inline panel must not also try to display that same
JComponent in the host at the same time. Practical impact: Clone and
Eraser are different tools; only one is "active" at a time, so the
host swaps content between them. But the `IInlineToolOptions` instance
returned by `super.getInlineOptionsPanel()` would be re-parented across
swaps — exactly the issue PHASE1 D1 warned about for the static
`PixelPlateOptionsPanel`.

**Resolution.** `CloneTool.buildInlineOptions()` does NOT call
`super.getInlineOptionsPanel()`. Instead, both `EraserTool` and
`CloneTool` call a new `protected JComponent buildEraserOptionsContent()`
helper that returns a *fresh* `JComponent` each invocation. Clone
composes that fresh content with its Merge panel. Each tool's
inline panel content tree is independent — no re-parenting.

### D7 — Tools with no options: don't override

`PanTool` has no `createOptionsComponent` override and falls through
to `Tool.createOptionsComponent` which returns a `JLabel(JaveMessages.ToolDialog_NoOptionsAvailableText)`.

PHASE0's host fallback is exactly this same kind of placeholder
("Inline options pending — use Tool Options Dialog" or similar).

**Decision.** Do not override `getInlineOptionsPanel()` on Pan. The
host shows its fallback when a tool returns null. Two side effects:

1. Pan's options-dialog hint behavior: `ToolOptionsDialog.setTool`
   shows the legacy "No options available" label (because Pan's
   `getInlineOptionsPanel` returns null), not the PHASE0 "Options shown
   in the toolbar" hint. That's correct — Pan is *not* migrated; the
   inline content is the host fallback, identical to what Pan would
   show in the dialog. Either label communicates the right thing for
   this tool.
2. Pan still counts as "unmigrated" by the metric "tool overrides
   `getInlineOptionsPanel`." That's a label; the user-visible behavior
   is what matters.

Consider tweaking the host fallback wording in a tidy-up commit so
that it reads sensibly when shown for Pan ("No options for this tool"
vs. "Inline options pending"). Leave as-is for now.

### D8 — Brush dynamic-content: revalidate on `setBrush`

`BrushTool.setBrush(char[][])` rebuilds the brushPanel grid in place
(rows × cols of `CharField`s) and calls `packOptionsDialog()`
(`BrushTool.java:115-117`). In the inline world, no dialog to pack —
instead the brush panel's parent in the host needs to revalidate so
the new GridLayout takes effect.

**Decision.** After rebuilding `brushPanel`, call
`brushPanel.revalidate(); brushPanel.repaint();`. The host region
will lay out around it without explicit involvement (Swing
revalidate cascades upward through the validate-root chain).

**Caveat.** A user-selected brush wider than the default 4×4 may
overflow the bar's natural width. The bar width is locked at startup
based on the *default* 4×4 brush. Wider brushes will visually overflow
or wrap badly inside the locked-width host.

**Mitigation.** Accept for PHASE2. Brush is the only tool with this
issue. If users complain, options are (a) measure with the maximum
plausible brush size at startup, or (b) wrap the brushPanel in a
`JScrollPane`. Both are PHASE2.5 / opportunistic follow-ups, not
blockers.

`packOptionsDialog()` becomes a no-op for Brush (the inline path
doesn't need it). Keep the call site so Brush still works if anyone
ever opens it via the legacy dialog (it won't, post-migration — the
hint label takes over).

### D9 — `FigletToolOptionsPanel` already has `getContent()`

The class already exposes `JComponent getContent()` matching the
contract. Add `implements IInlineToolOptions`. Adopt the pattern from
PHASE1 ε (the algorithmic `*OptionsPanel` classes).

`FIGletTool.createOptionsComponent` constructs the panel today and
wires several listeners. Move the panel construction into a new
`buildInlineOptions()` override so listeners are wired exactly once
per tool instance.

## Work units

Eight units, lettered ι through π. Suggested commit order in
"Cross-unit ordering" below.

### Unit ι — `PanTool` (no-op acknowledgement)

**Why.** D7 says don't override. This unit is just confirming that
behavior is correct (the fallback shows for Pan) and adding a brief
inline comment if needed.

**Change.** None to source code, but verify in a manual exercise that
selecting Pan shows the host fallback (or "No options available" if
the fallback message has been wordsmithed) and that opening the
legacy dialog also works.

**Acceptance.** Visual exercise documented in Unit ρ.

### Unit κ — `EraserTool` inline panel

**Why.** D7 doesn't apply; Eraser has real options.

**Change.**
- New file: `src/main/java/de/jave/jave/EraserOptionsPanel.java` —
  a class implementing `IInlineToolOptions`, containing the existing
  Style combo + Size spinner construction lifted verbatim from
  `EraserTool.createOptionsComponent`. Constructor takes the
  `JComboBox` and `SpinnerNumberModel` references the tool needs to
  read in `getBrush()`.
- Refactor `EraserTool`: extract the Style+Size widget construction
  into `protected JComponent buildEraserOptionsContent()` (per D6).
  Override `buildInlineOptions()` to wrap that content in an
  `IInlineToolOptions`. Keep the legacy `createOptionsComponent` for
  now — once D6 is applied, the legacy override becomes dead, but
  it doesn't hurt to leave it (the hint label takes over).

Actually, simpler shape: **delete** `EraserTool.createOptionsComponent`
per PHASE1 D3 (delete-as-you-migrate). Store `chStyle` and
`sizeModel` on the tool as today; build them in `buildInlineOptions()`.

**Acceptance.** Switching to Eraser shows the inline panel; Style and
Size spinner change the brush as before.

### Unit λ — `CloneTool` inline panel

**Why.** D6 — Clone composes Eraser's content with a Merge panel.

**Change.**
- `CloneTool.buildInlineOptions()` — calls
  `super.buildEraserOptionsContent()` (the helper introduced by
  Unit κ) and wraps with `MergeCharactersPanel`.
- Delete `CloneTool.createOptionsComponent` per D3.

**Acceptance.** Switching to Clone shows Eraser's Style+Size widgets
and a Merge checkbox below.

### Unit μ — `TextTool` inline panel

**Why.** Straightforward lift.

**Change.**
- New file: `src/main/java/de/jave/jave/tool/text/TextOptionsPanel.java`
  implementing `IInlineToolOptions`, containing the CursorMovement
  combo + `MergeCharactersPanel` from `TextTool.createOptionsComponent`.
- `TextTool.buildInlineOptions()` returns it.
- Delete `TextTool.createOptionsComponent`.

**Acceptance.** Switching to Text shows the CursorMovement combo and
Merge checkbox; cursor movement preference takes effect on key input.

### Unit ν — `SelectionTool` inline panel (+ Freehand inherits)

**Why.** Lift, plus D5.

**Change.**
- New file: `src/main/java/de/jave/jave/SelectionOptionsPanel.java`
  implementing `IInlineToolOptions`. Constructor takes the field
  references back to the tool (`cb3d`, `chSelectionLayer`,
  `cbCollision`, `mergeCharactersModel`) — these stay tool-side
  because the tool's drawing code reads them.
- `SelectionTool.buildInlineOptions()` constructs the panel.
- Delete `SelectionTool.createOptionsComponent`.
- `FreehandSelectionTool` — no change; inherits.

**Acceptance.** Both Selection and FreehandSelection show the same
inline panel; selecting either auto-enables Merge correctly when
paste-mode is "Mix"; 3D toggle works.

### Unit ξ — `AuxiliaryLinesTool` inline panel

**Why.** Trivial single-button lift.

**Change.**
- `AuxiliaryLinesTool.buildInlineOptions()` constructs the existing
  `SmartAction("Clear")` + `JButton(clearAction)` and returns an
  `IInlineToolOptions` that exposes the button as content.
- Delete `AuxiliaryLinesTool.createOptionsComponent`.

**Acceptance.** Switching to AuxLines shows a "Clear" button; pressing
it removes all lines; button is disabled when no lines exist.

### Unit ο — `BrushTool` inline panel

**Why.** Most involved migration — dynamic content (D8).

**Change.**
- `BrushTool.buildInlineOptions()` constructs the `brushPanel` +
  `MergeCharactersPanel` content tree as `createOptionsComponent`
  does today.
- `BrushTool.setBrush(char[][])` — replace `packOptionsDialog()` call
  with `brushPanel.revalidate(); brushPanel.repaint();`.
- Delete `BrushTool.createOptionsComponent`.
- Handle measurement: the startup measurement uses the default 4×4
  brush since `setBrush(DEFAULT_BRUSH)` is called from
  `createOptionsComponent` today and will be called from
  `buildInlineOptions()` going forward — same shape.

**Acceptance.** Switching to Brush shows the 4×4 char grid and Merge
checkbox. Editing a char updates the cursor preview. Loading a brush
from the brush palette (via menu) resizes the inline grid and the
Merge checkbox stays visible.

### Unit π — `FIGletTool` inline panel

**Why.** D9 — already half-done.

**Change.**
- `FigletToolOptionsPanel` — add `implements IInlineToolOptions` to
  the class declaration (the existing `getContent()` already matches
  the contract).
- `FIGletTool.buildInlineOptions()` constructs the panel and wires
  its listeners (lift from `createOptionsComponent`).
- Delete `FIGletTool.createOptionsComponent`.

**Acceptance.** Switching to FIGlet shows the Font Category + Font +
Font-info button + Merge checkbox; selecting a font updates the
preview.

### Unit ρ — Verification & polish

**Why.** Catch regressions before declaring PHASE2 done.

**Change.** Manual checklist:
- [ ] PanTool: switching to Pan shows host fallback inline; legacy
  dialog shows "No options available" label.
- [ ] All 8 actively-migrated tools: switching to each shows its
  inline panel.
- [ ] All 8 actively-migrated tools: opening the legacy dialog shows
  the hint label.
- [ ] FreehandSelection inherits Selection's panel cleanly (same
  widgets visible; toggling cb3d on Selection shows the same state
  on Freehand and vice versa).
- [ ] Watermark and Fill still use the legacy dialog (unchanged).
- [ ] Bar width measured at startup includes the new panels and stays
  constant when switching across all 21 tools.
- [ ] Brush: changing brush size via the brush palette resizes the
  inline grid; locked bar width does not deform other panels.
- [ ] Clone: Style/Size widgets and Merge checkbox both work.
- [ ] FIGlet: font selection + Merge checkbox both work; preview
  updates.

## Cross-unit ordering

Chunky commit shape (informed by PHASE1 retrospective — algorithmic
tools were each a one-line lift and got their own commits, which was
overkill). PHASE2 bundles them:

1. **Commit A — Eraser + Clone (Units κ + λ).** Bundled because Clone
   depends on the `buildEraserOptionsContent` helper introduced by κ.
2. **Commit B — Text + Selection (+ Freehand inherit) + AuxLines +
   FIGlet (Units μ + ν + ξ + π).** All independent low-risk lifts.
3. **Commit C — Brush (Unit ο).** Isolated commit because of D8's
   dynamic-content risk; easier to bisect if the Brush rebuild path
   regresses.
4. **Unit ι** (Pan) — no commit; absorbed into Unit ρ verification.
5. **Unit ρ** (verification) — checklist run; PHASE2 doc status flip
   commit.

## Open questions (resolve during PHASE2, not now)

- Whether to wordsmith the host fallback label so it reads sensibly
  for Pan ("No options for this tool" vs. PHASE0's pending-hint).
  Defer; either communicates correctly.
- Whether Brush's locked-width problem (D8) deserves an opportunistic
  fix in PHASE2. Defer until user feedback.
- Whether to factor a base for the algorithmic-style + tools that
  share `Style combo + MouseCharacterPanel + Merge panel` shape (now
  with Eraser also similar). Carried over from PHASE1 ε open question;
  still defer — wait until PHASE3 brings more participants.

## What's explicitly out of scope and pushed to PHASE3

- `WatermarkTool` migration. Likely promoted to View menu per the
  master plan rather than getting an inline panel at all.
- `FillTool` migration. Likely the first real "Edit X…" decomposition
  (Pattern picker pops a focused dialog; Solid + Gradient stay inline
  alongside a mode selector).
- Removing `Tool.createOptionsComponent` infrastructure once all 21
  tools are migrated.
- JNA window-level helpers (`setLevel:NSFloatingWindowLevel`,
  `setHidesOnDeactivate:YES`) for any residual "Edit X…" dialogs.
- User preference to bring back legacy "everything in floating dialog"
  mode — only revisited if user demand materializes.
