# Cell Model — PHASE1: `int[][]` glyph plane and PGA support

> **Status:** to be implemented. Class names below are conceptual placeholders
> unless noted as "exists today" with a file path.
>
> **R&D basis:** `devdocs/PGA-INT-VS-STRING.md` — the decision document. It
> establishes, with measurements, why the cell becomes an `int` rather than a
> `char`, `long`, `String`, or `Cell` object; why clusters need a side table;
> and what encoding space must be reserved for the parked work. **Read it before
> this plan.** Section references below (§2.1, §6.1, §12.2 …) point into it.
>
> **Later phases** (documents not yet written):
> `PLAN-CELL-MODEL-PHASE2.md` — colour, styling, OSC 8 hyperlinks.
> `PLAN-CELL-MODEL-PHASE3.md` — glyphs wider than one column.
> PHASE1 must not foreclose either; §12 of the R&D doc defines what that means
> and this plan implements it.

## Summary

PHASE1 converts JavE's canvas storage from `char[][]` to an `int[][]` glyph
plane, introduces a `Cell` API so callers stop touching raw arrays, and uses
that headroom to store a grapheme cluster — a glyph plus its Preceding-Glyph
Assimilators — as one inseparable cell.

User-visible result at the end of PHASE1:

- Pasting or typing `e`+U+0301 produces **one** cell, not two, and the rest of
  the row stays aligned. The defect in R&D §3 is gone.
- Clusters move, copy, delete, and select as single units; backspace removes the
  whole cluster.
- Emoji and other non-BMP characters occupy one cell instead of two lone
  surrogates.
- Documents round-trip through save, clipboard, and undo unchanged.

Explicitly **not** user-visible: no colour, no styling, no change to how wide
glyphs are drawn.

## Goals

1. **G1** — One extended grapheme cluster occupies exactly one cell.
2. **G2** — Every existing algorithm (fill, rotate, mirror, selection, figlet,
   image2ascii…) moves a cluster atomically **without being taught about
   clusters**. This is the property `int` was chosen for; it should require no
   per-algorithm work.
3. **G3** — Group A clusters (`Mn`, `Me`, stacks — measured at advance exactly
   1.00 in R&D §2.1) render composed inside their own cell with the row aligned.
4. **G4** — The encoding reserves space for PHASE2 and PHASE3 per R&D §12, and
   the reservation is enforced in code, not just documented.
5. **G5** — No regression in the existing 25 test classes.

## Non-goals (PHASE1)

| Deferred to | Item |
|---|---|
| PHASE2 | Per-cell foreground/background colour, SGR attributes, OSC 8 hyperlinks, `int[][] style` plane, `Style` record, xterm/VTE + HTML export |
| PHASE3 | Correct rendering of glyphs with advance > 1 column: Group B clusters (emoji, flags, Indic, jamo) **and** the pre-existing CJK case (`中` = 1.75 today). Continuation cells, hit-testing, and cursor motion across wide cells |
| — | Multi-column-aware layout, reflow, or word wrap |

Group B clusters **are** stored correctly in PHASE1 (one cell, atomic editing,
clean round-trip) — they simply overdraw their neighbours when drawn. That is
the accepted trade from R&D §11 Q1: the stored document is already correct for
PHASE3, so no data is redone later.

## Resolved design decisions

Carried from the R&D doc; not to be relitigated during implementation.

### D1 — Cell is an `int`; `Cell` is an API type, not the array element

Storage is `int[][]`. `Cell` is a record materialised by accessors — measured
free (1.00×, R&D §10.6) because escape analysis scalarises it. Making `Cell` the
array element is rejected: mutable cells alias on ordinary assignment
(`cells[a] = cells[b]` shares one object), immutable cells cost 22.73× on writes.

### D2 — Glyph encoding, with PHASE3 space reserved

| Range | Meaning |
|---|---|
| `>= 0` | Unicode code point (`0..0x10FFFF`) |
| `-1 .. -0x3FFF_FFFF` | cluster-table handle (~1.07 billion) |
| `-0x4000_0000 .. Integer.MIN_VALUE` | **reserved, unused in PHASE1** (~1.07 billion) |
| `Integer.MIN_VALUE` | earmarked `CONTINUATION` for PHASE3 |

Handles allocate from `-1` downward. Entering the reserved band is a programming
error and must assert (G4). Retrofitting this later would require rewriting every
persisted document.

### D3 — Cluster table is session-local, append-only, never serialised

Per R&D §7. Handles expand to real text at every I/O boundary and re-intern on
the way in, so a handle can never reach disk or the clipboard. No eviction —
undo snapshots hold handles.

### D4 — NFC-normalise on input

Collapses `e`+U+0301 to `U+00E9`, avoiding a table entry. Free, and it shrinks
the table. **Relied on for nothing** — the precomposed set is frozen and
lacunary (R&D §2, §11 Q2).

### D5 — Legacy pack format is emitted whenever the document allows it

`AsciiPacker` gains a new algorithm letter. But a document containing no
clusters and no non-BMP code points is still encoded with the existing `A`/`B`
algorithms, byte-identically. Existing art therefore produces unchanged output,
which matters because `CompressedJavaScriptAnimationExporter` and
`ActionScriptAnimationExporter` emit packed strings into generated players
consumed outside JavE.

## Work units

### Unit α — Encoding primitives (new package, nothing existing touched)

New `de.jave.lib.cell`:

- `GlyphEncoding` — the D2 constants plus `isCodePoint(int)`, `isCluster(int)`,
  `isReserved(int)`, `handleToIndex(int)`, `indexToHandle(int)`. `indexToHandle`
  throws if the index would reach the reserved band (**this is G4's teeth**).
- `ClusterTable` — append-only intern table. `int intern(String cluster)`,
  `String textOf(int handle)`, `int columnsOf(int handle)`. `columnsOf` returns
  `1` unconditionally in PHASE1; the field exists so PHASE3 is "start trusting a
  value that is already computed" rather than a schema change. Advance is
  computable at intern time as `stringWidth / charWidth('M')`.
- `GraphemeSplitter` — wraps `BreakIterator.getCharacterInstance()`, which R&D
  §2 verified segments every PGA kind correctly, including ZWJ families, tag
  sequences, and regional indicators. No ICU dependency.
- `Cell` — `record Cell(int glyph)` with `text()`, `columns()`, `isCluster()`.
  PHASE2 widens it to carry style; callers use accessors so that stays contained.

Fully additive and independently testable. Nothing else compiles differently.

### Unit β — `CharacterPlate` core conversion

`src/main/java/de/jave/lib/CharacterPlate.java` (exists today, 728 lines) plus
its two subclasses, `LocatedCharacterPlate` and
`de/jave/jave/layers/ActiveLayerCharacterPlate.java`.

- Field `char[][] chars` → `int[][] glyphs`.
- **Rename the raw accessors**: `getContent()`/`getContentClone()` →
  `glyphPlane()`/`glyphPlaneClone()`. This is deliberate. There are 440
  `getContent()` call sites and 65 of them index the returned array *and mutate
  it in place* (e.g. `Plate.java:933`). Returning a converted `char[][]` copy
  would break those silently; renaming turns every one into a compile error.
- Add `int glyphAt(x,y)`, `String textAt(x,y)`, `Cell cellAt(x,y)`,
  `setText(x,y,String)`, alongside the existing `get`/`set`/`setForce`/`fill`.
- `ICharacterDrawable.set(int,int,char)` widens to `int`. It has few
  implementors (`CharacterPlate`, `PixelPlate`) and few callers
  (`LineAlgorithm`, `EllipseAlgorithm`).

At the end of this unit the tree does not compile. That is intended and is the
whole point of choosing a loud-failure encoding (R&D §10.5).

### Unit γ — Mechanical caller sweep

Fix the compile errors the rename produced, across the 56 files that touch the
grid. Per the R&D §6.1 census the breakage is ~299 sites and almost entirely
type-name substitution:

| Idiom | Sites | Fix |
|---|---:|---|
| `char[][]` / `char[]` declarations | 191 | rename type |
| `char`-typed locals from cells | 58 | `char c =` → `int c =` |
| `(char)` casts | 39 | drop, or move to the render boundary |
| `new String(char[])` | 11 | `new String(int[], 0, n)` |

**Nothing in `Rot13`, `UpperCase`, `LowerCase`, `AsciiAntialiasing`,
`Rotate180Action`, `MirrorDynamicAction`, `FlipDynamicAction`, or the
`AsciiGreyscaleTable` ramps needs semantic change** — 175 char-literal
comparisons, 25 relational compares, and 6 arithmetic sites survive verbatim via
implicit `char`→`int` widening (verified in R&D §6.1). If one of these files
starts wanting a judgment call, stop: that is a signal the encoding is being
used wrongly.

Rule for this unit: **no behaviour changes.** Purely restoring the build.

### Unit δ — Text↔grid boundaries

The only places that need to know clusters exist.

**Intern (text → grid):** NFC-normalise, split into graphemes, intern anything
multi-code-point.

- `TextTools.toCharField(String)` and `toCharField(String[])` — the single
  chokepoint most input flows through
- `CharacterPlate(String)`, `CharacterPlate(String[])`, `paste(String,…)`,
  `insertLine(int,String)`, `tabelize`
- `ClipboardTransferer.createSelection` (`de/jave/awt/clipboard/`)
- file open, `TextboxDialog`, `FillInlineOptionsPanel`, figlet import

**Expand (grid → text):** handles become their cluster text.

- `TextTools.toStringArray(int[][])`
- `CharacterPlate.toString()`, `asString()`, `getLine()`, `toStringArray()`
- `JaveClipboardSelection`

A property test belongs here: `text -> grid -> text` is identity for
NFC-normalised input.

### Unit ε — Rendering

`Plate.paintDocumentContents()` (`Plate.java:825`) currently draws each row with
one `drawString(new String(row))`. Replace with:

- **Fast path** — if every cell in the row is a BMP code point, keep the single
  `drawString`. This is every existing document, so no repaint regression.
- **Slow path** — otherwise draw cell by cell at `originX + x * charWidth`.

The slow path fixes G3 for Group A. Group B still overdraws — correct for
PHASE1, and PHASE3 replaces the slow path with continuation-aware layout.

Do not trust `FontMetrics.charWidth()` for cluster measurement: R&D §3 found it
returns 8 (not 0) for a lone combining mark, disagreeing with actual
`stringWidth` layout.

Same treatment for the other draw sites: the tool preview at `Plate.java:951`,
`Selection.java:972` and `:1003`, `ConnectedLinesViewRenderer`, and
`AsciiToThumbnailConverter`.

**Keep the cell↔pixel arithmetic behind one helper** rather than inlining
`x * charWidth` at each paint and hit-test site — R&D §12.3 lists that inlining
as the main way PHASE3's design space gets forfeited.

### Unit ζ — Editing semantics

Mostly free: because one cell holds one cluster, cell-based cursor motion,
backspace, and selection are already atomic (G1 ⇒ most of the definition of
done). The real work:

- **Typing an assimilator merges into the preceding cell** rather than consuming
  a new one. `Plate.keyTyped` (`Plate.java:1060`) has a hardcoded dead-key table
  emitting precomposed characters; it needs a path for marks with no precomposed
  form, and for IME input.
- Verify the text tool's insert/overwrite and the shift-cells shortcuts treat a
  cluster cell as one unit.

### Unit η — Serialization

`AsciiPacker` (`de/jave/jave/algorithm/compress/`) is a char-per-cell RLE with
`%` escapes, reached by undo (`CompressedDocumentState`), 15 `.jcf` clipart
files, `.jmov` animations, saved patterns, and the JS/ActionScript exporters.

- Add algorithm `C`: code points, plus an escape introducing a cluster's text.
- **Keep `decodeA`/`decodeB` unchanged** for reading existing files — an old
  stream is "every cell is one BMP code point", already a valid document in the
  new model.
- Emit `A`/`B` byte-identically whenever the document has no clusters and no
  non-BMP (D5).
- `decode` already throws a "download a new release" error on an unknown
  algorithm byte, so older builds degrade legibly.

### Unit θ — Verification

- Unit tests for `GlyphEncoding` (including that the reserved band asserts),
  `ClusterTable`, `GraphemeSplitter`.
- A PGA corpus fixture built from the R&D §2 table — one case per kind, Group A
  and Group B — asserting cell counts.
- Round-trip properties: text→grid→text, and grid→pack→grid.
- Regression: all 25 existing test classes pass, especially
  `CompressedDocumentStateTest`, `ClipboardTransfererTest`, `SelectionTest`.
- Manual: paste the corpus into a running canvas and confirm row alignment for
  Group A.

## Cross-unit ordering

```
α  (additive, safe)
└─ β  (breaks the build deliberately)
   └─ γ  (restores the build; no behaviour change)
      ├─ δ  (clusters start existing in documents)
      │   └─ η  (they can now be saved)
      ├─ ε  (they render correctly)
      └─ ζ  (they can be typed)
         └─ θ
```

α is worth landing and reviewing on its own — it is pure addition and it pins
down D2, the one decision that is expensive to change later. β+γ should land as
a single commit; a half-converted tree has no value and does not compile.
δ, ε, ζ are independent of each other once γ is in.

## Resolved during planning

### D6 — Delete `char get(int,int)`; no lossy shim

Callers move to `glyphAt(x,y)` / `textAt(x,y)` / `cellAt(x,y)`. Keeping a
narrowing shim would shorten unit γ, but silent lossy narrowing is precisely the
failure mode this encoding was chosen to avoid (R&D §10.5). Every caller becomes
a compile error and gets looked at. Same reasoning applies to `setForce(int,int,
char)` and `fill(…, char)` — widen to `int`, do not overload.

### D7 — Cluster cells pass through merge rules unmerged

`CharacterMergeRulesConfiguration` (`config/mix.txt`, backing the Foreground
paste mode and the "Merge Characters" tickbox) is keyed by `char` pairs. A
cluster cell participates in no rule and is copied verbatim. Rationale: the merge
table encodes ASCII line-art joins (`-` + `|` → `+`); a base glyph carrying
assimilators is not a line-art primitive, and silently merging on its base code
point would drop the assimilators. Pass-through preserves data, which is the
PHASE1 invariant everywhere else too.

### D8 — Cluster table: no eviction in PHASE1; instrument and leave an escape hatch

The R&D doc's original rationale for append-only ("undo snapshots hold handles")
**was wrong** and has been corrected in R&D §7: `CompressedDocumentState` stores
`AsciiPacker` strings, and `AsciiPacker` expands handles to text, so the undo
stack holds no handles.

The corrected bound is stronger. Table growth is bounded by **distinct cluster
texts seen in the session** — not by operations, documents, or undo depth, since
`intern` deduplicates and undo/redo of identical content adds nothing. A
realistic session interning a few hundred distinct clusters costs tens of
kilobytes.

Therefore:

1. **No eviction.** Implementing it now is real complexity against a problem
   with no evidence of existing.
2. **Instrument it.** `ClusterTable` exposes `size()` and total interned-text
   bytes, and logs a one-time warning past a threshold (suggest 100,000 entries,
   ~15 MB). If this ever fires in the wild there is a breadcrumb instead of a
   mystery.
3. **Escape hatch is mark-sweep, not refcounting.** Live handles reside only in
   enumerable in-memory plates — document, layers, selection, internal clipboard,
   tool previews. If eviction is ever needed, walk those roots, collect reachable
   handles, and rebuild the table at a safe point such as document close.
   Refcounting across every plate mutation would be fragile; mark-sweep is
   obviously correct and can be added later without changing the encoding.

Deliberately **not** doing (2) via a `WeakHashMap` or similar: handles are `int`s
with no identity, so weak references cannot express reachability here.

## Open questions (resolve during PHASE1, not now)

None outstanding. D6–D8 above closed the three that were open at planning time.

## What PHASE2+ inherits

PHASE1 leaves these in place deliberately:

- The `-0x4000_0000` reserved band and `CONTINUATION` constant — **PHASE3**.
- `ClusterTable.columnsOf()`, populated but always `1` — **PHASE3**.
- `Cell.columns()`, returning `1` unconditionally — **PHASE3**.
- Cell↔pixel arithmetic behind a single helper — **PHASE3**.
- `Cell` as a record widened with a style field, and a sibling `int[][] style`
  plane that a document may simply not have — **PHASE2**.
- `AsciiPacker` algorithm `C`, which will need a `D` carrying style runs —
  **PHASE2**.

Nothing in PHASE1 should assume a cell is one column wide, or that a document
has no style plane, beyond the temporary constants listed above.
