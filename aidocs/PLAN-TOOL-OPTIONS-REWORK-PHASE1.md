# Tool Options Rework — PHASE1: First Tool Migrations (Skeleton)

> **Status:** structural skeleton only. Detailed per-tool surgery is
> deliberately deferred until PHASE0 implementation produces real
> measurements and real ergonomic feedback on the contract. Treat this doc
> as a frame; fill in detail after PHASE0 lands.
>
> **Master plan:** `aidocs/PLAN-TOOL-OPTIONS-REWORK.md`
> **Prerequisite phase:** `aidocs/PLAN-TOOL-OPTIONS-REWORK-PHASE0.md`

## Summary

PHASE1 migrates the first ten tools' options content from the legacy
floating `ToolOptionsDialog` to inline panels rendered in the
`ToolSelectorBarOptionsHost` set up by PHASE0. The ten tools split into
two groups that share PHASE0 infrastructure but raise different
state-management questions.

## Tools in scope (10)

**Generic group (6) — currently funnel through a shared static
`PixelPlateOptionsPanel` (`GenericTool.java:12`):**

- `FreehandToolGeneric` (tool index 0)
- `LineToolGeneric` (2)
- `RectangleToolGeneric` (4) — adds a `cbFill` "Fill" checkbox
- `EllipseToolGeneric` (6) — adds a `cbFill` "Fill" checkbox
- `BezierToolGeneric` (8)
- `ArcToolGeneric` (9)

**Algorithmic group (4) — each has its own legacy `*OptionsPanel`:**

- `FreehandAlgorithmicTool` (1) — `FreehandAlgorithmicOptionsPanel`
- `LineAlgorithmicTool` (3) — `LineAlgorithmicOptionsPanel`
- `RectangleAlgorithmicTool` (5) — `RectangleAlgorithmicOptionsPanel`
- `EllipseAlgorithmicTool` (7) — `EllipseAlgorithmicOptionsPanel`

## Goals

- Each of the ten tools overrides `Tool.getInlineOptionsPanel()` with a
  real implementation. Selecting any of these tools shows its inline
  panel in the host (no longer the PHASE0 fallback).
- Selecting any of these tools and opening the legacy floating dialog
  shows the PHASE0 hint label (per `ToolOptionsDialog` Unit L plumbing).
- All functional behavior of the legacy options panels is preserved —
  every option that the user can set today via the legacy panel can be
  set via the new inline panel (possibly with sub-options behind an
  "Edit X…" button if measurement says they don't fit inline).
- The bar width measured by PHASE0's measurement pass accommodates the
  widest of these ten panels without exceeding what feels reasonable in
  visual review. If it does exceed, the widest panel is decomposed
  before merging.

## Non-goals (PHASE1)

- Migrating the other 8 tools (Selection, FreehandSelection, Text,
  FIGlet, Brush, Eraser, Fill, Clone, Pan, plus AuxLines/Watermark
  inline content). Those are PHASE2+.
- The first real "Edit X…" focused dialog implementation. PHASE1 panels
  may or may not need one — likely not, since the generic and
  algorithmic shape tools are relatively simple compared to FillTool.
  If one is needed, its mechanics (modeless vs modal, position
  persistence, JNA window-level treatment) get sketched here as a
  PHASE1 sub-decision, not a separate phase.
- JNA window-level helpers (still deferred to a late phase).

## Blockers — PHASE0 discoveries that gate PHASE1 detail

These cannot be answered until PHASE0 ships:

1. **Measured bar width with the fallback panel only.** Sets the
   baseline. If a PHASE1 panel measures wider than this, the bar grows
   and stays grown (constant width across all tools, per master plan).
2. **Measurement-pass robustness with real panels.** PHASE0 only
   measures the trivial fallback. Whether the hidden-frame + `pack()`
   recipe gives accurate widths for `PixelPlateOptionsPanel`-style
   real-world panels needs verification before PHASE1 commits to the
   approach.
3. **Ergonomics of the `IInlineToolOptions` contract.** With one
   placeholder participant in PHASE0, the contract is untested under
   load. PHASE1 may discover the contract needs widening (e.g., a
   `notifyShown()` hook for panels that need to refresh on tool
   activation, or a `getMaxWidthHint()` to inform measurement).
4. **Generic-tools state sharing approach.** PHASE0 documents that the
   contract supports both options below; PHASE1 must choose:
   - **(a) Shared model + per-tool views.** Extract the state currently
     held by the static `PixelPlateOptionsPanel` (`GenericTool.java:12`)
     into a model object owned at application or generic-tool-group
     level. Each of the 6 generic tools' inline panels is a fresh view
     bound to that model. Drawing code reads from the model instead of
     the panel. Cleaner but more refactoring surface.
   - **(b) Shared `JComponent` re-parented across tools.** All 6 tools'
     `getInlineOptionsPanel()` return the same `IInlineToolOptions`
     instance whose `getContent()` returns the same `JComponent`. The
     host re-parents on tool change (Swing supports this). Drawing code
     reads from the panel as today. Less refactoring, but multiple
     `getInlineOptionsPanel()` call sites returning identical objects
     is a code smell that may warrant a helper.

## Provisional work units (subject to revision after PHASE0)

### Unit α — Generic-tools state sharing decision

Resolve blocker #4. Implement whichever option (a) or (b) is chosen.
This is the foundation for Unit β.

### Unit β — Generic tools migration (6 tools)

Per-tool override of `getInlineOptionsPanel()` for the six generic
tools. Per Unit α: either six panels bound to a shared model, or six
overrides returning a shared inline-options object.

Open per-tool details (deferred until α resolves):

- How `cbFill` for Rect/Ellipse generic surfaces in the shared/per-tool
  model.
- Whether the existing `MergeCharactersPanel` (`PixelPlateOptionsPanel`
  uses one) fits inline or wants extraction.

### Unit γ — Algorithmic tools migration (4 tools)

Each algorithmic tool gets its own `IInlineToolOptions` implementation
recreating the functional content of its existing
`*AlgorithmicOptionsPanel`. No shared-state question here — each tool
already has independent options state.

Open per-tool details (deferred):

- Whether any of the four legacy panels has a sub-section that needs
  decomposition behind an "Edit X…" button. Measurement pass will tell.

### Unit δ — Legacy panel cleanup

Once a tool's inline panel is verified-equivalent to its legacy panel,
the legacy `*OptionsPanel` class becomes unreferenced. Decision deferred
to PHASE1 implementation time:

- **Keep** legacy panel classes around until full migration completes
  (PHASE2+), as comparison reference and in case the user-preference-
  toggleable legacy mode (deferred late phase) needs them, **or**
- **Delete** them as each tool migrates, on the principle that git
  history is a sufficient archive.

### Unit ε — Verification

Per-tool functional regression check: every option that could be set
via the legacy panel can be set via the inline panel and produces the
same drawing behavior. PHASE1 will sketch a checklist; for now the
check is "manual exercise of each tool's inline panel against the
legacy panel's documented behavior."

## Open design questions (will resolve during PHASE1)

- "Edit X…" dialog mechanics if any PHASE1 panel needs one (per-tool
  singleton vs new-per-click, modeless vs modal, position persistence).
  Likely not needed for these ten tools but flagged.
- Whether to introduce a base class or trait for inline panels that
  share patterns (e.g., both Rect and Ellipse generic add a Fill
  checkbox in the same way). Defer until at least three panels exist
  and the duplication is concrete.

## What's explicitly out of scope and pushed to PHASE2+

- The other 8 tools (Selection, FreehandSelection, Text, FIGlet, Brush,
  Eraser, Fill, Clone, Pan) and the AuxLines/Watermark inline-options
  content.
- FillTool decomposition (the one most likely to produce the first real
  "Edit X…" dialog — PHASE2+ candidate).
- Removing the legacy `ToolOptionsDialog` class entirely. Stays alive
  through at least PHASE2.
- User preference to bring back legacy "everything in floating dialog"
  mode — only revisited if user demand materializes.
