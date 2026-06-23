# Tool Options Rework — PHASE3: Watermark, Fill, and Legacy Dialog Removal

> **Status:** complete. This is a status record, not a forward plan — the
> work it describes is already landed and building.
>
> Closes out the rework begun in PHASE0–PHASE2. Builds on landed
> infrastructure (`IInlineToolOptions`, `ToolSelectorBarOptionsHost`,
> `InlineOptionsWidthMeasurer`) and the PHASE1/PHASE2 tool migrations.
>
> **Master plan:** `devdocs/PLAN-TOOL-OPTIONS-REWORK.md`
> **Prerequisite phases:** PHASE0, PHASE1, PHASE2 (all landed)

## What landed

PHASE3 migrated the two tools PHASE2 deferred and then retired the legacy
floating dialog outright:

- **`WatermarkTool`** renders its options inline.
- **`FillTool`** renders its options inline, including the rework's first
  and only real "Edit X…" decomposition.
- **The legacy `ToolOptionsDialog` is gone** — class deleted, no remaining
  references anywhere in `src/main/java`.

End state: **all 21 tools render their options inline in the
`ToolSelectorBarOptionsHost`.** No tool uses a floating options dialog, and
there is no longer a legacy options dialog to fall back to.

## Current state by tool

### WatermarkTool — inline (not View-menu-only)

The master plan floated promoting Watermark to View-menu-only and dropping
its tool button. That is **not** what shipped. Watermark kept its inline
panel:

- `WatermarkTool.getInlineOptionsPanel()` returns a `WatermarkOptionsPanel`
  (`src/main/java/de/jave/jave/WatermarkOptionsPanel.java`,
  `implements IInlineToolOptions`) built by
  `WatermarkTool.buildWatermarkOptionsContent()`.
- The View menu retains a watermark **visibility** toggle
  (`getWatermarkVisibilityToggleAction`) and a Special-menu
  "Set Content as Watermark" item — these are orthogonal to where the
  *options* are edited.

### FillTool — inline with an "Edit Pattern" focused dialog

Fill is the size-hog the master plan predicted would need decomposition.
Its inline panel lives in
`src/main/java/de/jave/jave/tool/fill/FillInlineOptionsPanel.java`
(`implements IInlineToolOptions`) and is mode-driven (Solid / Pattern /
Gradient):

- Mode + match-mode selectors, Solid and Gradient editing are inline.
- The Pattern card carries an **"Edit…"** button. It opens a focused
  `UserDialog` titled "Edit Pattern" hosting an `AsciiTextArea`
  (`FillInlineOptionsPanel.performEditPattern`). This is the rework's only
  "Edit X…" pop-out — the pattern grid is genuinely too large for the
  locked bar width.

Supporting Fill files: `FillModeUi`, `FillMatchModeUi`, `FillOptions`,
`SolidFillOptionsPanel`, `PatternPreviewComponent` (all under
`tool/fill/`).

## Legacy dialog removal

`ToolOptionsDialog` and its scaffolding were removed once every tool had an
inline panel:

- The class file is deleted; `rg ToolOptionsDialog src/main/java` returns
  nothing.
- The PHASE0 hint-label branch ("Options shown in the toolbar.") is gone
  with it.
- The `Cmd/Ctrl+T` toggle action and its `toolOptionsDialogVisibility`
  preference plumbing are gone — there is no floating dialog left to
  toggle.

This resolves the master plan's deferred question ("when migration is
complete, revisit whether to keep the legacy dialog"): it was removed
rather than kept behind a preference.

## Status of earlier-phase deferrals

- **JNA window-level helpers** (`setLevel:NSFloatingWindowLevel`,
  `setHidesOnDeactivate:YES`): not implemented and now largely moot — the
  only residual pop-out is Fill's modal "Edit Pattern" `UserDialog`, which
  does not exhibit the original modeless focus-steal problem. Revisit only
  if that dialog proves annoying in practice.
- **`Tool.createOptionsComponent` infrastructure**: no overrides remain
  in the tree (`rg createOptionsComponent` is empty). The inline contract
  (`Tool.getInlineOptionsPanel()`) is the sole options-rendering path.
- **"Bring back the floating dialog" user preference**: not built; no
  demand. With the legacy dialog deleted it would now be a re-introduction,
  not a toggle.

## Notes

- Bar width: the startup `InlineOptionsWidthMeasurer` pass picks up the two
  new panels automatically via the existing PHASE1 measurement loop; bar
  width stays constant across all 21 tools.
- A pre-implementation mockup for the watermark panel is retained at
  `devdocs/watermark-option-panel-mockup.drawio` (and `…-mockup2.drawio`).
- These docs were authored under the path alias `aidocs/`; the directory
  has since been renamed `devdocs/`. Older cross-references that still say
  `aidocs/` refer to this same directory.
