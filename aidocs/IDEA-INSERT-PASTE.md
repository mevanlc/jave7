# Idea: Insert-Paste (displacing) vs. Overwrite-Paste

## Context

JavE's existing `Paste mode:` dropdown (Selection tool options; values defined
in `de.jave.jave.Selection.STR_LAYER`) offers four variants — but all of them
are **overwrite-style**: pasted content lands on top of the canvas and the
question is only how the source pixels combine with the destination.

| Mode         | Source cells (incl. spaces) | Source non-space | Notes |
|--------------|-----------------------------|------------------|-------|
| Background   | place under existing        | dest wins on overlap | non-destructive only where dest already has content |
| Normal       | clobber                     | clobber          | the default; what most users see |
| Foreground   | spaces transparent          | merge via `config/mix.txt` | enables the "Merge Characters" tickbox |
| Difference   | XOR-ish                     | XOR-ish          | toggle effect |

All four overwrite some subset of the existing cells under the paste
rectangle. None of them **make room** for the pasted block.

## The gap

A user pasting into the middle of existing artwork often wants to *insert*
the block — i.e. push existing cells out of the way so nothing is lost.
This is the 2D analogue of a text editor's `Insert` key: instead of typed
characters overwriting, they push existing characters right.

Call this **insert-paste**.

## Semantic interpretations

ASCII canvases are 2D, so "push out of the way" has more than one natural
meaning. The candidates fall into two families.

### Family 1 — single-axis, single-direction (recommended core)

Pick one of four cardinal directions and slide displaced cells that way.
The other three quadrants of the canvas are untouched. Each is exactly the
2D analogue of a text editor's insert key, oriented one way.

#### A. Shift right (per-row, content moves rightward)

For each row `r ∈ [pasteY, pasteY+pasteH)`, slide the existing chars in
`[pasteX, docWidth)` right by `pasteWidth`, then drop the paste block into
the now-empty `[pasteX, pasteX+pasteWidth)` slot.

```
before:        paste "XX" at (2,1):    after:
ABCDEF                                 ABCDEF
GHIJKL                                 GHXXIJKL
MNOPQR                                 MNOPQR
```

Closest analogue to text-editor insert mode; the most intuitive default
for LTR readers.

#### A'. Shift left (per-row, content moves leftward) — RTL-friendly

Mirror of A. For each row in the paste range, slide chars in
`[0, pasteX+pasteWidth)` left by `pasteWidth`, then drop the paste at
`[pasteX, pasteX+pasteWidth)`.

```
before:        paste "XX" at (3,1):    after:
   ABCDEF                              ABCDEF
   GHIJKL                              GHIXXJKL
   MNOPQR                              MNOPQR
```

Useful for RTL artists, or for inserting near the right edge of a piece
where pushing right would overflow but pushing left has slack.

Implementation note: shifting left can only borrow space from the left
margin. If there isn't enough whitespace there, we either need to clip or
grow the canvas leftward (see open design questions).

#### B. Shift down (per-column, content moves downward)

For each col `c ∈ [pasteX, pasteX+pasteWidth)`, slide existing chars in
`[pasteY, docHeight)` down by `pasteHeight`. Columns left and right of the
paste rectangle are untouched.

```
before:        paste "XX" at (2,1):    after:
ABCDEF                                 ABCDEF
GHIJKL                                 GHXXKL
MNOPQR                                 MNXXQR
                                       ..IJ..
                                       ..OP..
```

Vertical analogue of A. Useful when surrounding artwork is laid out in
horizontal strips, so vertical displacement is less destructive than
horizontal.

#### B'. Shift up (per-column, content moves upward)

Mirror of B. Slide chars in the affected columns above the paste up by
`pasteHeight`, then drop the paste in. Useful when the artwork below the
paste is the load-bearing part you want to preserve in place.

Same caveat as A': shifting up borrows from the top margin; may need to
clip or grow upward.

### Family 2 — multi-axis (a "hole" instead of a slide)

#### C. Shift right + down (anchored top-left)

Insert `pasteHeight` rows at `pasteY` AND `pasteWidth` columns at `pasteX`
**globally**: every char at `(c ≥ pasteX, r ≥ pasteY)` slides diagonally
by `(+pasteWidth, +pasteHeight)`.

```
before:        paste "XX" at (2,1):    after:
ABCDEF                                 ABCDEF
GHIJKL                                 GHXX..
MNOPQR                                 ....IJKL
                                       ....OPQR
```

Preserves all original cells but reflows the entire bottom-right quadrant.
Almost always wrong for ASCII art — breaks vertical alignment of unrelated
content below the paste point. Listed for completeness; probably skip.

#### D. Iris / radial (50%/50%/50%/50%)

Open the paste hole symmetrically: existing content gets displaced
outward in all four directions, by half the paste dimensions on each side.

For each existing cell at `(c, r)` *outside* the paste rectangle:

| location relative to rect | displacement                       |
|---------------------------|------------------------------------|
| left of rect              | `-pasteW/2` columns                |
| right of rect             | `+pasteW/2` columns                |
| above rect                | `-pasteH/2` rows                   |
| below rect                | `+pasteH/2` rows                   |
| left+above                | `(-pasteW/2, -pasteH/2)` (diagonal)|
| right+below               | `(+pasteW/2, +pasteH/2)` (diagonal)|
| etc.                      | corresponding diagonal shifts      |

Cells inside the paste rectangle are replaced. The canvas grows by
`pasteW` total in width (split evenly on both sides) and `pasteH` total
in height (split evenly top and bottom).

```
before:        paste "XXXX/XXXX" 4x2 at (2,1):
ABCDEFGH                                 (canvas grows by 2 cols, 1 row;
IJKLMNOP                                  paste centered around (4,1.5))
QRSTUVWX
YZ012345
```

Conceptually like opening an iris: the paste appears at the focus, the
existing artwork breathes outward symmetrically around it. Useful when
the user thinks of the paste point as a center rather than a top-left
anchor — e.g. "make room here for this thing, push everything else away
evenly."

Caveats:
* Half-pixel offsets when `pasteW` or `pasteH` is odd. Round one side up,
  the other down (configurable, but a sensible default is "extra row
  goes below, extra col goes right" so the bias is consistent).
* All four edges of the canvas may need to grow.
* Significantly changes coordinates of *every* existing cell, not just
  those near the paste point.

## Recommended scope for v1

A small, pragmatic subset; the rest can land later as easy additions
because the underlying primitive (per-row or per-col slide) is the same.

* **A — Shift right**: the strong default; closest mental model to
  insert-mode in a text editor.
* **B — Shift down**: cheap to ship alongside A and answers "but my art
  is laid out horizontally."

Defer A', B', C, and D until there's demand. A' and B' are trivial extensions
once A and B exist (mirror the slide direction); D is more involved because
it touches all four canvas edges.

## Open design questions

1. **Document growth.** When shifted content runs past a canvas edge, do
   we silently auto-grow, clip and warn, or clip silently?

   The edges in play depend on the mode:
   - A (shift right): right edge.
   - A' (shift left): left edge.
   - B (shift down): bottom edge.
   - B' (shift up): top edge.
   - C: right and bottom edges.
   - D (iris): potentially all four edges, by `pasteW/2` and `pasteH/2`.

   Growing right/bottom is straightforward (append rows/cols, all existing
   coordinates unchanged). Growing **left** or **top** is more invasive
   because every existing cell's coordinates shift — anything that holds a
   `(col, row)` snapshot (cursor position, scroll origin, undo records,
   floating-selection origin, ruler origin) must be translated by the same
   delta. This is doable but needs care.

   Auto-grow should respect the existing `autoResizeOnDropForTextEditor` /
   `autoResizeOnDropForAnimationEditor` prefs (`PlatePreferences`). For
   left/top growth we may want a separate opt-in pref since it's a more
   surprising operation.

2. **Source spaces.** With overwrite-paste, "Foreground" mode treats source
   spaces as transparent. For insert-paste, every source cell is *placed*,
   spaces included — so "transparent space" doesn't apply. The pasted block
   is rectangular and opaque by definition. Worth calling out in the UI
   tooltip.

3. **Mask / non-rectangular selections.** If the floating selection has a
   non-rectangular mask (e.g. magic-wand selection), what does "push right"
   even mean? Two options:
   - bound the displacement to the selection's bounding rect (simple);
   - per-row, displace by the run-length of the source mask in that row
     (precise but funky).

   Bounding-rect is fine for v1.

4. **Undo granularity.** Should a single insert-paste be one undo step,
   or two (the shift, then the placement)? Almost certainly one — it's a
   single user action.

5. **Animation editor.** Whether the same operation should apply across
   all frames, or just the current frame. Default: current frame only,
   matching how the existing `Paste mode:` works for animations.

6. **Defaults / persistence.** If we add this, do we also persist the
   last-used `Paste mode:` to `PlatePreferences` so the user's choice
   survives a restart? Probably yes — separate small win, see
   `SelectionTool.java:104` (currently hard-coded to index `1` = Normal).

## Implementation sketch

* **`CharacterPlate`** — add `insertShiftRight(int x, int y, int w, int h, char[][] src)`
  and `insertShiftDown(...)` methods that perform the row-slide /
  column-slide and drop in source content. These are pure-data operations
  on the underlying char array, parallel to the existing `set` / `paste`
  paths.
* **`Selection.java`** — extend `STR_LAYER` with the new mode names and
  thread an `enum` (rather than a layer index) through the commit path.
  `SelectionTool.createOptionsComponent` reads from the same array.
* **Commit path** — wherever the floating selection is dropped onto the
  underlying plate, dispatch on the new mode and call the right
  `CharacterPlate.insertShift*` method.
* **Undo** — capture the pre-state via the existing `saveCurrentState("paste")`
  hook in `PasteAsNewSelectionAction` / the selection drop code.
* **Auto-grow** — if document growth is desired and the prefs allow it,
  call the existing resize plumbing before the shift.

## Out of scope for this idea

* Vector / reflow-aware behavior (e.g. respecting word boundaries) — JavE
  treats cells atomically, no semantic reflow.
* Diff-aware insert (only insert cells that would actually clobber) — adds
  complexity for marginal benefit.
