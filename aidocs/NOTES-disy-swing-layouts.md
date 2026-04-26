# Disy Swing Layouts — Notes & Gotchas

Notes captured while space-optimizing the ToolSelectorBar (PHASE1 of
the tool options rework). Most of JavE uses Disy's house layout
library (`net.disy.commons.swing.layout.grid.*`) instead of stdlib
`GridBagLayout`. The library is generally pleasant — closer to SWT's
GridLayout than to AWT — but has a few sharp edges worth knowing.

## TL;DR

- `equalColumns=true` + cells with `horizontalSpan > 1` produces
  surprising column widths. Use `equalColumns=false` unless every
  column genuinely needs to match.
- `FILL_HORIZONTAL` on a row makes it paint the full container width,
  hiding the row's natural preferred width.
- `JCheckBox` on macOS Aqua paints over its own border; wrap in a
  `JPanel` for `setBorder` debug visualizations.
- Width measurement at startup (PHASE0's `InlineOptionsWidthMeasurer`)
  works on undecorated `JFrame.pack()` — no need to make windows
  visible.

## Where the code lives

| Path | Role |
|---|---|
| `net.disy.commons.swing.layout.grid.GridDialogLayout` | LayoutManager — entry point |
| `net.disy.commons.swing.layout.grid.GridDialogLayoutData` | Per-cell hints (alignment, span, fill, indent) |
| `net.disy.commons.swing.layout.grid.GridBuilder` | Builds the `Grid` from a parent's children + their data |
| `net.disy.commons.swing.layout.grid.Grid` | The flattened cell/row/column model |
| `net.disy.commons.swing.layout.grid.GridCell` | One cell — caches `preferredComponentSize` + applies layout-data hints |
| `net.disy.commons.swing.layout.util.GridCellSizeList` | Per-axis size list with `increasePreferredSizes(start, span, totalIncrement)` for distributing span-cell demand |

## Gotcha: `equalColumns=true` × span cells

`Grid.makeColumnsEqualWidth()` (`Grid.java:52`) sets every column's
preferred to the **max preferred of any column**. Combined with
spanning, this creates a feedback loop:

1. Single-cell content (e.g. tool buttons) sets initial column
   preferreds (say, all `28`).
2. A `horizontalSpan=N` cell wants `P` total. The current column sum
   is `N×28 = 4×28 = 112`. Excess `P − 112 = 43` is distributed
   across the spanned columns via
   `GridCellSizeList.increasePreferredSizes(start, span, excess)` —
   evenly with rounding remainder pushed to last cells. Cols become
   `~38, 39, 39, 39`.
3. **Then** `makeColumnsEqualWidth()` runs and sets all four cols to
   the max (`39`). At this point the math is what you expect — total
   `156`.

But during PHASE1 we observed total = `284` for a layout where the
math predicted `156`. The reproduction had ~30 components, several
spanning rows, and `equalColumns=true`. Switching to
`equalColumns=false` collapsed the bar to the expected `155` with no
visual regression. The exact path through `Grid` that produced the
inflation wasn't pinned down — when this matters again, instrument
`GridCellSizeList.increasePreferredSizes` directly and walk the
order in which cells get added to confirm.

**Recipe.** Default to `equalColumns=false`. Only set `true` when
you have a flat grid (no spans, no FILLs spanning multiple columns)
*and* you need uniform column widths.

## Gotcha: `FILL_HORIZONTAL` hides natural row widths

`GridDialogLayoutData.FILL_HORIZONTAL` makes a row paint at the full
container width regardless of its content's preferred width. Useful
for the visible layout, misleading when you're trying to debug
"why is row X so wide" — adding a `LineBorder` to the row will show
the *rendered* bounds (full container), not the row's natural
preferred bounds.

To see natural widths: print `component.getPreferredSize().width`
directly. Walk the tree top-down; that's the only honest signal.

## Recipe: measuring a panel's preferred width without showing it

Already in the codebase as
`de.jave.gui.layout.InlineOptionsWidthMeasurer`. The trick:

```java
JFrame f = new JFrame();
f.setUndecorated(true);
f.add(panel);
f.pack();                        // triggers addNotify + validate
Dimension d = panel.getPreferredSize();
f.remove(panel);
f.dispose();
```

`pack()` runs the layout cycle even though the frame is never made
visible — peers get created, fonts resolve, layout managers settle.
The `f.remove(panel)` before `f.dispose()` matters: it detaches the
panel from the throwaway frame so it can be re-parented later
without dragging a disposed parent reference around.

Documented escape hatch (not yet needed): if a panel reports `0`
(HTML labels, certain custom components), make the frame briefly
visible off-screen, then invisible, then dispose.

## Gotcha: `JCheckBox.setBorder` is invisible on macOS Aqua

The Aqua look-and-feel paints the checkbox + label inside the
component bounds, drawing over any border you set. Wrap in a
`JPanel` if you need a visible debug border:

```java
JPanel wrapper = new JPanel(new BorderLayout());
wrapper.add(checkbox, BorderLayout.WEST);
wrapper.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
```

`BorderLayout.WEST` keeps the checkbox at its natural size instead
of stretching it.

## When to consider switching off Disy

`GridBagLayout` is the obvious stdlib alternative. Reasons to switch
in a localized area:

- The disy quirks above are biting you and a one-off `GridBagLayout`
  panel would be cleaner than wrestling Disy.
- You need GBL-only features (e.g., per-cell `weightx`/`weighty` for
  proportional growth that isn't all-or-nothing).

Reasons to stay:

- Most of the codebase uses Disy. Mixing two grid layouts in a
  single window is hard to reason about, especially under
  re-validate cascades.
- Disy's per-row `GridDialogLayoutData` is more legible than GBC's
  field-of-many-things constructor.

If switching one panel: do it locally, document why in a comment,
and don't propagate the change opportunistically — same-style
neighbors should keep using Disy unless they too hit a quirk worth
calling out.

## Things to instrument next time the layout misbehaves

Walk the tree printing `component.getPreferredSize().width` per
child. Then:

- For each spanning cell, also print
  `((GridDialogLayoutData) ((GridDialogLayout) parent.getLayout()).getConstraints(child)).getHorizontalSpan()`
  to confirm the spans you think are set are actually set.
- After `pack()`, also print each child's `getBounds()` to see
  the *laid-out* widths — those reveal column widths after
  `makeColumnsEqualWidth()` and any FILL stretching.
- If the parent's preferred is much larger than the sum of column
  preferreds (printed via the same mechanism on each cell), the
  `equalColumns=true` quirk is the prime suspect.
