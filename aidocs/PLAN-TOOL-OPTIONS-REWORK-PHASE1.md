# Tool Options Rework — PHASE1: First Tool Migrations

> **Status:** complete. All 10 tools migrated; legacy
> `PixelPlateOptionsPanel` deleted; bar width measurement in place.
> Notable PHASE1 discovery captured separately at
> `aidocs/NOTES-disy-swing-layouts.md` (the `equalColumns=true` ×
> span-cell quirk in disy GridDialogLayout that inflated the bar).
>
> Builds on landed PHASE0 infrastructure (`IInlineToolOptions`,
> `ToolSelectorBarOptionsHost`, `InlineOptionsWidthMeasurer`,
> `ToolOptionsDialog` hint-label branch).
>
> **Master plan:** `aidocs/PLAN-TOOL-OPTIONS-REWORK.md`
> **Prerequisite phase:** `aidocs/PLAN-TOOL-OPTIONS-REWORK-PHASE0.md` (landed)

## Summary

PHASE1 migrates the first ten tools' options content from the legacy
floating `ToolOptionsDialog` to inline panels rendered in the
`ToolSelectorBarOptionsHost` set up by PHASE0. The ten tools split into
two groups that share PHASE0 infrastructure but raise different
state-management questions.

User-visible result at the end of PHASE1:

- Selecting any of the ten migrated tools shows that tool's options
  inline in the left bar.
- Selecting any migrated tool and opening the legacy dialog (`Cmd/Ctrl+T`)
  shows the PHASE0 hint label "Options shown in the toolbar."
- Selecting any non-migrated tool (the other 11) still shows the
  PHASE0 fallback panel inline and the legacy options in the floating
  dialog.
- Bar width is constant across all 21 tools (set once at startup to the
  widest measured panel).

## Tools in scope (10)

**Generic group (6)** — currently funnel through the static
`PixelPlateOptionsPanel` shared via `GenericTool.pixelPlateOptionsPanel`
(`GenericTool.java:12`):

| Tool index | Class | Adds |
|---|---|---|
| 0 | `FreehandToolGeneric` | — |
| 2 | `LineToolGeneric` | — |
| 4 | `RectangleToolGeneric` | `cbFill` "Fill" checkbox |
| 6 | `EllipseToolGeneric` | `cbFill` "Fill" checkbox |
| 8 | `BezierToolGeneric` | — |
| 9 | `ArcToolGeneric` | — |

The generic-tool shared content has three regions
(`PixelPlateOptionsPanel.java:40-91`):
1. Pencil size (5 buttons: thin + 4 thick).
2. Style: line/dot radios (when thin) **or** feltpen char buttons
   (when thick) — via a `CardPanel` keyed off pencil size.
3. Mix-characters checkbox (`MergeCharactersPanel`).

**Algorithmic group (4)** — each has its own legacy panel with the same
shape (Style combobox + per-style `MouseCharacterPanel` + mix-chars
checkbox):

| Tool index | Class | Legacy panel |
|---|---|---|
| 1 | `FreehandAlgorithmicTool` | `FreehandAlgorithmicOptionsPanel` |
| 3 | `LineAlgorithmicTool` | `LineAlgorithmicOptionsPanel` |
| 5 | `RectangleAlgorithmicTool` | inline construction in `createOptionsComponent` (`RectangleAlgorithmicTool.java:48`); uses `RectangleStylePanel` instead of a top-level combobox |
| 7 | `EllipseAlgorithmicTool` | `EllipseAlgorithmicOptionsPanel` |

## Goals

- All 10 tools override `Tool.getInlineOptionsPanel()` with a real
  implementation. Selecting any of these tools shows its inline panel
  in the host (no longer the PHASE0 fallback).
- Selecting any of these tools and opening the legacy floating dialog
  shows the PHASE0 hint label.
- All functional behavior is preserved: every option that the user can
  set today via the legacy panel can be set via the new inline panel,
  and produces the same drawing behavior.
- The startup width-measurement pass measures all 10 panels plus the
  fallback, and pins the host width to the widest. Width is constant
  for the rest of the session.

## Non-goals (PHASE1)

- Migrating the other 11 tools (Selection, FreehandSelection, Text,
  FIGlet, Brush, Eraser, Fill, Clone, Pan, Watermark, AuxLines).
  Those are PHASE2+.
- Removing the legacy `ToolOptionsDialog` class entirely.
- The first real "Edit X…" focused dialog implementation. Likely not
  needed for these ten tools; if measurement says one is, sketch it as
  a sub-decision rather than a separate phase.
- JNA window-level helpers (deferred to a late phase).
- Internationalization-aware re-measurement at runtime.

## Resolved design decisions

These resolve the PHASE0-deferred questions for PHASE1.

### D1 — Generic-tools state sharing: model + per-tool views (option a)

The static `PixelPlateOptionsPanel` (`GenericTool.java:12`) is replaced
with a shared **model** holding pencil-size, line-style, and feltpen-char
state. Each generic tool's inline panel is a fresh **view** bound to that
model — six independent `JComponent` trees, one per tool.

Why option (a) over (b) re-parenting:

- Rect and Ellipse generic add a per-tool `cbFill` checkbox that does
  *not* belong in the shared panel (`RectangleToolGeneric.java:43`,
  `EllipseToolGeneric.java:43`). With (a), each tool composes its own
  panel = shared view + per-tool extras cleanly. With (b), we'd still
  need a wrapper layer to glue extras onto a shared panel — same
  composition cost, harder reasoning.
- Re-parenting one `JComponent` across six callers couples the host's
  remove/add ordering to per-tool selection — easy source of subtle
  bugs (focus, listener leaks, re-validate cascades).
- Drawing code today reads from the panel (`GenericTool.isFeltpenMode`
  etc.). Pointing it at a model object is a one-time refactor; once
  done, it's straightforward.

### D2 — Measurement integration: explicit panel registry

`JavEApplication` currently measures only the fallback
(`JavEApplication.java:187`). Extend the measurement step to pass all
inline panels' contents:

1. Construct each migrated tool's inline panel content eagerly
   (just-in-time at measurement time).
2. Pass the full list to `InlineOptionsWidthMeasurer.measureMaxWidth`.
3. `setMinWidth(maxOfAll)`.

Cost is ~11 hidden-`JFrame` packs at startup; trivially fast.

### D3 — Legacy panel cleanup: delete-as-you-migrate

Each migrated tool's legacy `*OptionsPanel` class becomes unreferenced
once the migration commit lands. Delete it in the same commit. Two
benefits:

- No drift between two implementations of the same options.
- Forces the inline panel to be a real replacement (if anything still
  references the legacy class, the build breaks — surfaces gaps early).

`Tool.createOptionsComponent` overrides on migrated tools also become
dead (the legacy dialog never calls them once `getInlineOptionsPanel()`
returns non-null — see `ToolOptionsDialog.java:71-73`). Delete those too.

### D4 — `IInlineToolOptions` contract: do not widen yet

The current contract is just `JComponent getContent()`. Resist the urge
to add `notifyShown()`, `getMaxPreferredWidth()`, or
`tearDown()` until at least three real participants exist and the need
is concrete. Eager construction (D2) sidesteps any "refresh on
activation" need for the ten PHASE1 tools.

## Work units

Eight units. Some are infrastructure-touching; most are per-tool work.
Suggested commit order in "Cross-unit ordering" below.

### Unit α — `PixelPlateModel` extraction

**Why.** D1 requires a model object so the six generic-tool views can
share state without sharing a JComponent.

**Change.** New class `de.jave.jave.pixelplate.PixelPlateModel` (or
similar). Holds:
- `ObjectModel<PencilSize> sizeModel`
- `ObjectModel<LineStyle> lineStyleModel`
- `ObjectModel<Character> feltPenStyleModel`
- Convenience: `isLineMode()`, `isFeltpenMode()`,
  `getFeltpenPreviewDiameter()`, `configure(PixelPlate)`.

These are the methods `GenericTool` and `PixelPlateOptionsPanel`
currently expose. Move the implementations onto the model verbatim.

Keep existing `GenericTool` accessors (`isFeltpenMode` etc.) as
delegations to the model — drawing-code refactor is in Unit β.

**Acceptance.** Model class compiles and is unit-testable in isolation
(no Swing dependency).

### Unit β — Generic tools point at the model, not the static panel

**Why.** Drawing code in `*ToolGeneric` (and `PixelPlate.configure`)
today reads from the static `PixelPlateOptionsPanel`. After D1 it must
read from the model.

**Change.**
- `JavEApplication` (or a small dedicated holder it owns) constructs
  one `PixelPlateModel` instance.
- `GenericTool.pixelPlateOptionsPanel` static is removed.
  `GenericTool` exposes the model (constructor-injected from the
  application during `createTools`, like `filter` is today —
  `ToolBar.java:171-176`).
- `GenericTool.isFeltpenMode` / `isLineMode` /
  `getFeltpenPreviewDiameter` delegate to the model.
- `PixelPlateOptionsPanel.configure` callers (search; should be
  one-ish) point at `model.configure(...)` instead.

**Acceptance.** All 6 generic tools draw correctly with each
size/style combination — verified via manual exercise. Mix-chars
checkbox state still flows through `tool.getMixCharactersModel()` (the
existing tool-side model).

### Unit γ — `PixelPlateOptionsView` (shared view component)

**Why.** Six tools need the same UI bound to the shared model.

**Change.** New class
`de.jave.jave.pixelplate.PixelPlateOptionsView`. Constructor takes
`(PixelPlateModel model, BooleanModel mixCharactersModel)`. Builds the
size/style/mix-chars layout that today lives in
`PixelPlateOptionsPanel.java:40-91`. Exposes `JComponent getContent()`.

Note: `mixCharactersModel` is per-tool (`tool.getMixCharactersModel()`),
so the view itself is per-tool too — this is just a builder that
produces a fresh `JComponent` each instantiation.

**Acceptance.** Building a `PixelPlateOptionsView` and putting its
content into a window produces the same visual layout as
`PixelPlateOptionsPanel` does today, and changes are observed by the
shared model.

### Unit δ — Generic tools' inline panels (6 tools)

**Why.** The actual migration.

**Change.** Each of the 6 generic tools overrides
`getInlineOptionsPanel()` to return an `IInlineToolOptions` whose
content is:
- Freehand / Line / Bezier / Arc generic: just a fresh
  `PixelPlateOptionsView`.
- Rect / Ellipse generic: a `JPanel` containing
  `PixelPlateOptionsView` + the existing `cbFill` checkbox below.

Per D3, also delete each tool's `createOptionsComponent` override
(if any) and the static `PixelPlateOptionsPanel` class itself.

The Rect/Ellipse `cbFill` checkbox stays a per-tool field as today —
its drawing code reads `this.cbFill.isSelected()`
(`RectangleToolGeneric.java:115`, `EllipseToolGeneric.java:74`).

**Acceptance.** Each generic tool, when active, shows its inline panel
in the host. State sharing across all 6 verified by switching tools and
observing pencil-size/style stay set. Rect/Ellipse Fill toggle
independently per-tool.

### Unit ε — Algorithmic tools' inline panels (4 tools)

**Why.** The actual migration for the algorithmic group.

**Change.** Each of the 4 algorithmic tools overrides
`getInlineOptionsPanel()`. The 4 panels share a shape but not a class —
each tool already has independent options state, so no shared model
extraction needed.

For Freehand / Line / Ellipse algorithmic: lift the existing
`*OptionsPanel.java` body almost verbatim into the inline panel
implementation (rename, repackage as needed). Per D3, delete the legacy
`*OptionsPanel.java` and the tool's `createOptionsComponent` override.

For Rectangle algorithmic: same approach, but the legacy logic is
inline in `RectangleAlgorithmicTool.createOptionsComponent`
(`RectangleAlgorithmicTool.java:48-55`) and uses `RectangleStylePanel`
instead of a top-level combobox. Lift to a new inline panel class;
delete the `createOptionsComponent` override.

Whether to factor a shared base for these four panels (style selector
+ mouse-char panel + mix-chars panel): **defer**. With four
participants in PHASE1 the duplication is measurable but the right
abstraction isn't obvious yet. Revisit during PHASE2 when more tools
follow the same pattern.

**Acceptance.** Each algorithmic tool, when active, shows its inline
panel. Style selector toggles `MouseCharacterPanel` enabled state as
before. Drawing produces same characters as the legacy panel did.

### Unit ζ — Measurement extension

**Why.** D2 — the bar must be sized to the widest of all migrated
panels, not just the fallback.

**Change.** In `JavEApplication.startup()` (the block at
`JavEApplication.java:186-191`), after `createTools` has run, gather
all tools' inline panel contents (skip nulls), prepend the fallback
content, pass the full list to
`InlineOptionsWidthMeasurer.measureMaxWidth`. Use the result for
`setMinWidth`.

Construction order matters: `ToolBar` constructor calls `createTools`
which builds tools. Measurement must happen *after* tools exist.
Today's startup already does
`new ToolSelectorBarOptionsHost(fallback)` *before* `new ToolBar(...)`
because `ToolBar` takes the host. Move `setMinWidth` to *after*
`new ToolBar(...)` so tools can be queried for their inline panels.

**Acceptance.** Bar width at startup is `max(width(allInlinePanels),
width(fallback))` and stays constant when switching tools.

### Unit η — Verification & polish

**Why.** Catch regressions before declaring PHASE1 done.

**Change.** Manual checklist:
- [ ] Each of the 10 tools, when activated, shows its inline panel
  (not the fallback).
- [ ] Each of the 10 tools, when the legacy dialog is open, shows
  the hint label.
- [ ] Each of the 11 unmigrated tools still shows the fallback inline
  and its options in the legacy dialog.
- [ ] Pencil-size / line-style / feltpen state persists across switches
  among the 6 generic tools.
- [ ] Rect-generic Fill and Ellipse-generic Fill toggle independently.
- [ ] Each algorithmic tool's style selector enables/disables its
  `MouseCharacterPanel` correctly.
- [ ] Drawing output matches legacy behavior for one representative
  configuration of each of the 10 tools.
- [ ] Bar width unchanged after switching through all 21 tools.

### Unit θ — Tidy-up sweep (optional, can defer to PHASE2)

**Why.** After Units β/δ/ε land, `GenericTool.pixelPlateOptionsPanel`
is gone but `Tool.createOptionsComponent` still exists for the 11
unmigrated tools. Nothing to do yet — flagged for PHASE2 to track.

## Cross-unit ordering

Suggested commit sequence:

1. **Unit α** (`PixelPlateModel` extraction) — pure addition, no caller
   changes yet.
2. **Unit β** (point generic tools at the model) — drawing-code refactor.
   Lands behind a model that's not yet user-visible. After this commit,
   the static panel is still in use only for legacy-dialog rendering.
3. **Unit γ** (`PixelPlateOptionsView`) — pure addition.
4. **Unit δ** (generic-tools inline panels + delete legacy panel) — the
   first user-visible PHASE1 change. Six tools migrate together because
   they share a model; partial migration would leave half the tools
   with no readable state-source.
5. **Unit ε** (algorithmic-tools inline panels + delete legacy panels) —
   four independent commits, one per tool, OK to interleave.
6. **Unit ζ** (measurement extension) — can land before Unit δ as a
   no-op (would just measure the fallback) and start working
   automatically as panels arrive. Recommend landing **first** so each
   tool's migration commit needs no follow-up width tuning.
7. **Unit η** (verification) — last; not a commit, a checklist run.

So a sensible order is: α, β, γ, ζ, δ, ε×4, η.

## Open questions (resolve during PHASE1, not now)

- Whether any algorithmic panel measures wide enough to want
  decomposition behind an "Edit X…" button. Measurement after Unit ζ
  + ε will tell.
- Whether to introduce a base class for the four algorithmic inline
  panels. Defer past Unit ε; revisit once PHASE2 has more participants.
- Where the `PixelPlateModel` instance lives (held by `JavEApplication`
  vs. a small `GenericToolGroup` holder). Decide during Unit α; either
  is fine — pick the smaller change.

## What's explicitly out of scope and pushed to PHASE2+

- The other 11 tools (Selection, FreehandSelection, Text, FIGlet,
  Brush, Eraser, Fill, Clone, Pan, Watermark, AuxLines) and their
  inline-options content.
- FillTool decomposition (the most likely first source of an "Edit X…"
  dialog — PHASE2+ candidate).
- Removing the legacy `ToolOptionsDialog` class entirely. Stays alive
  through at least PHASE2.
- Removing the dead `Tool.createOptionsComponent` infrastructure once
  all 21 tools are migrated.
- User preference to bring back legacy "everything in floating dialog"
  mode — only revisited if user demand materializes.
