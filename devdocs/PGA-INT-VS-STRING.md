# PGA storage: `int[][]` vs. `String[][]`

**Status:** **decided** — `int[][]` glyph plane, accessed through a `Cell` API.
No code written yet.
**Implementation plan:** `devdocs/PLAN-CELL-MODEL-PHASE1.md`. Colour/styling and
wide-glyph handling become `PLAN-CELL-MODEL-PHASE2.md` and `-PHASE3.md`
(not yet written).
**Question:** JavE's canvas cell is a Java `char`. To hold a glyph plus its
Preceding-Glyph Assimilators (PGAs) as one inseparable editing unit, the cell
type has to change. To what?

### Scope

**In scope: PGAs handled correctly. Nothing else.**

**Parked, but must be left room:** per-cell colour and styling, OSC 8
hyperlinks, and correct handling of glyphs whose advance exceeds one column.
These are analysed here only far enough to prove the chosen encoding does not
foreclose them — see **§12**, which is the section to check that claim against.
No part of them is built now.

**Width unit.** Throughout this document, `1` column = the advance of an ASCII
`M` in the canvas monospace font (`FontMetrics.charWidth('M')`). Every `advance`
figure is a ratio against that unit.

All numbers below are measured on this machine (Java 21.0.11, macOS 25.6.0,
`sourceCompatibility = 17`) against this working tree. Probe sources are in
the appendix so the numbers can be re-derived.

---

## 1. Terminology

**PGA — Preceding-Glyph Assimilator.** A code point that does not stand on its
own but modifies, joins, or attaches to the glyph before it. The project term of
art. A **cluster** is a base glyph plus its trailing PGAs — what Unicode calls
an extended grapheme cluster, and what the user perceives as one character.

---

## 2. The taxonomy, measured

The working table, with three measured columns added. `utf16` is
`String.length()`; `cps` is `codePointCount`; `advance` is rendered width in
monospace cell widths, where **1.00 means "fits in exactly one cell"**.

| Kind | Example | Cat | utf16 | cps | advance | Truly assimilated? |
|------|---------|----:|------:|----:|--------:|--------------------|
| Nonspacing combining mark | `◌́` U+0301 | `Mn` | 2 | 2 | **1.00** | **Yes** |
| Enclosing combining mark | `◌⃝` U+20DD | `Me` | 2 | 2 | **1.00** | **Yes** |
| Stacked marks ("zalgo", 3×) | `e` + 3×`Mn` | `Mn` | 4 | 4 | **1.00** | **Yes** |
| Spacing combining mark | Indic vowel U+093E | `Mc` | 2 | 2 | 1.88 | No — bears width |
| Variation selector | U+FE0F `VS16` | `Mn`/`Cf` | 2 | 2 | 2.00 | No — retargets to a wide emoji glyph |
| Emoji skin-tone modifier | `🏻` U+1F3FB | `Sk` | 4 | 2 | 2.38 | No — bears width |
| ZWJ | U+200D | `Cf` | 8 | 5 | 7.13 | No — joins, but width is font-dependent |
| Emoji tag characters | U+E0020… | `Cf` | 14 | 7 | 2.38 | No — bears width |
| Regional indicator pair | `🇺🇸` | `So` | 4 | 2 | 2.38 | No — bears width |
| Conjoining jamo | `각` | `Lo` | 3 | 3 | 1.50 | No — bears width |
| Surrogate | U+D800…DFFF | `Cs` | 1 | 1 | — | **Not a character at all** |
| *(not a PGA, listed for contrast)* CJK wide | `中` | `Lo` | 1 | 1 | 1.75 | n/a — already broken today |

Java's `BreakIterator.getCharacterInstance()` segments **every** row above into
exactly **1** grapheme cluster, including ZWJ families, tag sequences, and
regional-indicator flags. Segmentation is a solved problem on the JDK; no ICU
dependency is needed.

### 2.1 The taxonomy splits in two — and this governs scope

The `advance` column is the important discovery, and it does not follow the
Unicode category. Measured across three real monospace fonts:

| Sequence | Monospaced | Menlo | Courier New |
|----------|-----------:|------:|------------:|
| combining acute (`Mn`) | 1.00 | 1.00 | 1.00 |
| enclosing (`Me`) | 1.00 | 1.00 | 1.00 |
| zalgo ×3 (`Mn`) | 1.00 | 1.00 | 1.00 |
| variation selector | 2.00 | 2.00 | 2.00 |
| skin tone (`Sk`) | 2.38 | 2.38 | 2.38 |
| ZWJ family (`Cf`) | 7.13 | 7.13 | 7.13 |
| flag (RI) | 2.38 | 2.38 | 2.38 |
| CJK wide | 1.75 | 1.75 | 1.75 |

- **Group A — true assimilators** (`Mn`, `Me`, and stacks of them): exactly
  **1.00 in every monospace font tested**. Font-independent. Packing these into
  one cell is unambiguously correct — storage, editing, *and* rendering all
  agree.
- **Group B — width-bearing clusters** (`Mc`, VS16, `Sk`, ZWJ, tags, RI, jamo):
  **1.50–7.13**, and font-dependent — the same ZWJ family measures 7.13 in
  Menlo but 1.58 in Apple Color Emoji, because Menlo lacks the ligature and
  renders the components separately. For these, *one cell is a lie*: the glyph
  will overdraw its neighbours no matter which storage type we pick.

**Consequence for scope.** Packing solves Group A completely. It solves Group B's
*editing* problem (they stop being splittable) but not their *rendering* problem.
Honest multi-column cells are a separate, larger feature — a grid where one cell
can claim N columns — and are explicitly **out of scope** here. Note that JavE
already has this bug independent of PGAs: a bare CJK `中` measures 1.75 today.

---

## 3. What is actually broken today

`TextTools.toCharField()` walks `text.charAt(i)`, so every UTF-16 code unit
becomes its own cell:

```
"e" + U+0301 + "x"   ->  3 cells  [U+0065][U+0301][U+0078]
"A" + U+1F600 + "B"  ->  4 cells  [U+0041][U+D83D][U+DE00][U+0042]
```

`Plate.paintDocumentContents()` then draws each row with a single `drawString`.
The combining mark contributes zero advance, so a row that the grid thinks is 3
cells wide renders 2 cells wide, and **everything to the right of a PGA slides
left by one column**. Emoji hit the same path as two lone surrogates.

A detail worth recording: `FontMetrics.charWidth(U+0301)` returns **8**, not 0 —
per-character metrics and actual `stringWidth` layout already disagree, so any
fix that measures cells individually must not trust `charWidth`.

---

## 4. Why the cell cannot stay 16 bits

The cheap option — keep `char[][]`, intern each cluster to an unused code point,
expand at the I/O and render boundaries — was considered and **rejected**.

A `char` holds 65,536 values, of which 2,048 are surrogates: it addresses
**63,488 of Unicode's 1,114,112 code points, or 5.7%**. Sentinels must come out
of that same cramped space:

| Candidate range | Slots | Safe? |
|---|---:|---|
| Lone surrogates `D800–DFFF` | 2,048 | Yes — never legal standalone content |
| BMP noncharacters `FDD0–FDEF`, `FFFE/FFFF` | 34 | Yes |
| PUA `E000–F8FF` | 6,400 | **No** — Nerd Font / Powerline glyphs live here and JavE users paste them |

That is ~2,082 usable handles against an **unbounded** set of possible clusters
(zalgo stacks arbitrarily deep). The RGI emoji set alone runs to several
thousand sequences, so pasting an emoji palette exhausts the budget. Worse,
**the table can never be evicted from**, because undo snapshots hold sentinels —
so it only grows, and it grows toward a hard wall.

The deeper point: a 16-bit cell cannot store a plain 😀 as one cell *no matter
how clever the table is*. The cell width is the actual defect; PGAs are just
where it became visible.

---

## 5. The two real candidates

### `int[][]` — cell is a code point, or a negative cluster handle

```java
int[][] cells;
//  'A'            ->  65          direct
//  U+1F600 emoji  ->  0x1F600     direct, no table entry
//  "x" + U+20D7   ->  -1          handle into the cluster table
```

Valid code points span `0..0x10FFFF`, so **any negative value is unambiguously a
handle** — ~2.1 billion of them. The ceiling problem disappears, and every
single-code-point character (including all emoji) is stored directly with no
table involvement at all.

### `String[][]` — cell is the cluster itself

```java
String[][] cells;
//  "A"   "😀"   "x⃗"      all direct, no table, nothing to serialize
```

Conceptually the simplest model: what you see in the debugger is what the cell
holds, clusters are arbitrarily long by construction, and there is no side
structure with a lifetime.

---

## 6. Evidence

### 6.1 Refactor surface — idiom census over the 56 files that touch `char[][]`

| Idiom | Sites | `int[][]` | `String[][]` |
|---|---:|---|---|
| `char[][]` / `char[]` declarations | 191 | breaks (mechanical) | breaks (mechanical) |
| `char`-typed locals from cells | 58 | breaks (mechanical) | breaks (mechanical) |
| `(char)` casts | 39 | breaks (mechanical) | breaks (mechanical) |
| `new String(...)` | 11 | breaks (mechanical) | breaks (mechanical) |
| char literal compare `== ' '` | 175 | **survives** | breaks — **needs judgment** |
| relational compare `< 'a'` | 25 | **survives** | breaks — **needs judgment** |
| char arithmetic `c + '\r'` | 6 | **survives** | breaks — **needs judgment** |
| `switch` / `case 'x'` | 4 | **survives** | breaks — **needs judgment** |
| **Total sites touched** | | **~299** | **~509** |

Both are compiler-caught — neither can fail silently. The difference is *kind*,
not just count: `int[][]`'s 299 are almost entirely type-name substitutions,
while `String[][]` adds 210 sites where someone must decide what an ordering
comparison or an arithmetic shift *means* for a multi-character cell. Those 210
land in `Rot13`, `UpperCase`, `LowerCase`, `AsciiAntialiasing`,
`AsciiGreyscaleTable`, and `DynamicalGreyScaleTableCreator`.

Verified: Java's implicit `char`→`int` widening means this compiles and runs
**verbatim** under `int[][]`, straight out of `Rot13.java`:

```java
int c = cells[y][x];
if ((c < 'a' || c > 'm') && (c < 'A' || c > 'M')) {
   if (c >= 'n' && c <= 'z' || c >= 'N' && c <= 'Z') cells[y][x] = c - '\r';
} else {
   cells[y][x] = c + '\r';
}
```

Rendering a row stays a one-liner too — `String` has had an `int[]` code-point
constructor since Java 5: `new String(row, 0, row.length)`.

### 6.2 Runtime cost

2,000 full-grid passes over a 200×80 grid (scan-for-blank + case arithmetic +
write-back, 48,000 cell-ops per pass), after warmup:

| | time | vs. `char[][]` |
|---|---:|---:|
| `char[][]` | 15.9 ms | 1.00× |
| `int[][]` | 15.3 ms | **0.96×** |
| `String[][]` | 82.4 ms | **5.19×** |

`int[][]` is a hair *faster* than today (no narrowing on write-back).
`String[][]` costs **5×**, from `equals` dispatch and `charAt` indirection —
and that is the *favourable* case, with all single-char cells pre-interned. This
lands directly on flood fill, rotate, mirror, and the per-frame repaint scan.

### 6.3 Memory, one 200×80 grid

| | bytes | note |
|---|---:|---|
| `char[][]` | 33,280 | 2 B/cell |
| `int[][]` | 65,280 | 4 B/cell |
| `String[][]` | 129,280 | 8 B/cell in refs alone, plus ~40 B per distinct `String` |

Undo multiplies this: every snapshot retains another full array. `String[][]`
carries 2× the reference footprint of `int[][]` before counting objects.

---

## 7. The cluster table (the honest cost of `int[][]`)

This is `int[][]`'s only real disadvantage, so it deserves a concrete design
rather than a hand-wave.

- **Scope:** one process-wide append-only `ClusterTable`. Handles are dense
  negatives; `-1` is the first cluster.
- **Only Group-A-and-friends land in it.** Every single-code-point character —
  all of BMP, all emoji, all CJK — is stored directly. The table holds only
  genuine multi-code-point sequences, so it stays small in practice.
- **Never serialized.** `AsciiPacker` and every file writer *expand* handles to
  real text on the way out and re-intern on the way in. The table is a
  session-local interning cache, not persistent state. This keeps document
  formats self-describing and means a handle can never leak to disk or clipboard.
- **Never evicted — but not for the reason first given here.** An earlier
  revision argued "undo snapshots hold handles, so eviction would need
  refcounting". **That premise is wrong**: `CompressedDocumentState` stores each
  snapshot as an `AsciiPacker` string, and per the bullet above `AsciiPacker`
  expands handles to text. **The undo stack holds text, not handles.**

  The correct characterisation is better news. Table growth is bounded by the
  number of **distinct cluster texts seen in the session** — not by the number of
  operations, documents, or undo steps, since `intern` deduplicates and
  undo/redo of the same content adds nothing. Handles are live only in
  in-memory plates (document, layers, selection, internal clipboard, tool
  previews).

  At ~100 B/entry, a realistic session interning a few hundred distinct clusters
  costs tens of kilobytes. Reaching 100,000 entries — ~15 MB — requires
  deliberately pasting large volumes of *distinct* multi-mark content. So
  append-only is safe in practice, and the 2.1-billion handle space is
  unreachable rather than one emoji palette away.

  Eviction is therefore **deferred, not foreclosed**: because live handles reside
  only in enumerable in-memory plates, the escape hatch is a mark-sweep
  compaction at a safe point (e.g. document close) rather than fragile
  refcounting. See `PLAN-CELL-MODEL-PHASE1.md` for the instrumentation that
  would tell us it is ever needed.
- **Cost to a reader:** the `cell < 0` convention is a thing you must know.
  Mitigate by never letting raw cells escape — funnel access through
  `CharacterPlate.textAt(x, y)` / `setText(x, y, String)` so the encoding stays
  inside one class.

`String[][]` has none of this. That is a genuine simplicity win, and it is the
strongest argument on its side.

---

## 8. Serialization — a wash

`AsciiPacker` is a char-stream RLE with `%` escapes, one char per cell. It backs
the undo stack (`CompressedDocumentState`) **and** reaches disk via `.jcf`
clipart (15 files shipped), `.jmov` animation, saved patterns, and the
JavaScript/ActionScript exporters.

Both options force a format change, and to the same degree — the format's
one-`char`-per-cell assumption is what breaks, not the choice of in-memory type.
Either way it needs a version marker and an escape for "next cell is a
multi-code-point sequence". `AsciiPacker.decode` already throws a
"download a new release" error on an unknown algorithm byte, so bumping `A`/`B`
to a new letter degrades legibly on old builds.

**Open question for the reader:** must new builds still *read* existing `.jcf`
and `.jmov` files? Decoding old `A`/`B` streams unchanged is cheap to keep — the
old format is simply "every cell is one BMP code point", which is a valid
`int[][]` document. Recommend keeping the read path.

---

## 9. Recommendation

**`int[][]`.** It wins on every measured axis except one:

| | `int[][]` | `String[][]` |
|---|---|---|
| Cluster ceiling | none (2.1 B) | none |
| Emoji / non-BMP | direct, no table | direct |
| Sites touched | ~299 | ~509 |
| Sites needing judgment | ~0 | ~210 |
| Runtime | 0.96× | 5.19× |
| Memory (refs) | 4 B/cell | 8 B/cell + objects |
| Side table | **required** | **none** |
| Inseparability | structural — an `int` cannot be split | conventional — a `String` *can* be split, so every future algorithm must remember not to |

The last row is the one I weight most heavily, and it is the same argument that
motivated the whole exercise. The requirement is *"treat them as an inseparable
unit."* An `int` is inseparable by construction. A `String` is separable, so
`String[][]` spends 210 edits re-establishing by hand an invariant that
`int[][]` gets from the type system — and then relies on everyone who writes a
new algorithm afterwards to keep re-establishing it.

**This was written as an open question — "if per-cell colour is on the roadmap,
say so now, because it inverts this analysis." It is on the roadmap** (fg/bg
colour, with xterm/VTE, HTML, and/or custom markup as the serialised forms).
The answer arrived, and it reshapes the question rather than inverting it. See
§10 — the short version is that `String[][]` cannot hold colour either, so the
recommendation above stands and colour is layered beside it, not inside it.

### Out of scope, deliberately

- **Group B rendering.** Wide clusters will overdraw. Needs multi-column cells,
  a separate feature. A policy decision is still required — see §11.
- **CJK / East Asian width.** Pre-existing (`中` = 1.75 today), same root cause,
  same fix, not this change.

---

## 10. Per-cell colour — how it changes the picture

Confirmed as a roadmap item: foreground/background colour per cell, exported as
xterm/VTE escapes, HTML, and/or custom markup.

### 10.1 It does not rescue `String[][]`

The obvious reading — "rich cells were the case for `String[][]`, so switch" —
does not survive contact. A `String` holds the *cluster* but has nowhere to put
a foreground colour, a background colour, or SGR attributes. Under colour,
`String[][]` is not the rich option; it is an incomplete option that also costs
4–5×. It drops out of contention entirely.

What colour actually demands is a **record** per cell: glyph + fg + bg + attrs.
So the real question becomes how to lay that record out.

### 10.2 The Java-specific constraint

Terminal emulators written in Rust or C++ (alacritty, kitty, wezterm) store an
array of cell structs, which is contiguous memory — one cache line covers
several cells. **Java has no value types before Valhalla**, so `Cell[][]` is an
array of *references*: every cell access is a pointer chase, and every undo
snapshot deep-copies 16,000 objects. The idiomatic terminal-emulator layout is
the one layout Java handles worst.

Measured, same 200×80 grid and workload as §6.2, now reading style per cell:

| Layout | throughput | vs. best | undo snapshot | bytes/cell |
|---|---:|---:|---:|---:|
| `int[][]` glyph + `int[][]` style (struct-of-arrays) | 16.9 ms | **1.00×** | **1.00×** | **8** |
| `long[][]` packed (glyph high 32, style low 32) | 19.8 ms | 1.17× | ~1.00× | 8 |
| `Cell[][]` objects (array-of-structs) | 22.2 ms | 1.31× | **2.60×** | ~40 |
| `String[][]` — *glyph only, no colour* | 68.2 ms | 4.03× | — | 8 + objects |

> **Two corrections to the table above, both against this document's earlier
> position.**
>
> 1. The **undo-snapshot column is moot**. `CompressedDocumentState` stores each
>    snapshot as an `AsciiPacker`-encoded `String`, not as a cloned array — so no
>    layout deep-copies the grid per undo step, and the 2.60× never applies.
> 2. The `long[][]` row was measured on a glyph-dominated workload that favours
>    struct-of-arrays. Re-measured fairly in §10.6, the two are a **dead tie**.
>
> **Memory is also explicitly off the table** — the project owner has no concern
> at these sizes, and the absolute figures in §10.6 bear that out. What remains
> is failure modes, which is where §10.5 and §10.6 land.

### 10.3 Recommended layout

Two parallel planes, both `int[][]`:

```java
int[][] glyph;   // >= 0 : code point       |  < 0 : cluster-table handle
int[][] style;   //    0 : default style    |  > 0 : style-table handle
```

- **The glyph plane is exactly the `int[][]` recommended in §9, unchanged.** The
  diligence above is not invalidated by colour; it settles the glyph question,
  and colour attaches beside it.
- **Style is a handle, not inline colour.** Scope is bounded by "whatever
  xterm/VTE carries", which settles the record's shape:

  ```java
  record Style(int fg, int bg, int underlineColor,  // truecolor, or -1 = default
               int attrs,                            // bold/dim/italic/underline
                                                     // (single|double|curly|dotted|dashed),
                                                     // blink/reverse/hidden/strike/overline
               String hyperlinkUri, String hyperlinkId) {}   // OSC 8
  ```

  **OSC 8 hyperlinks settle the inline-vs-handle question outright**: a URI is an
  unbounded string, so it cannot be packed into cell bits under any layout. Style
  must be a handle. Inlining even the colour part would need three or four extra
  planes (fg, bg and underline colour are 3 bytes each) and would force another
  grid-wide change every time an attribute is added.
- **Style 0 is the default**, so an uncoloured document is a plane of zeros — and
  the plane can be lazily allocated, meaning **existing monochrome ASCII art pays
  nothing at all** in memory or time.
- **It reuses the cluster-table mechanism** from §7 rather than inventing a
  second concept: both planes are "an `int` cell with an append-only interned
  side table". One idea, applied twice.

### 10.4 It makes the exporters easier, not harder

Handle equality *is* the "do I need to emit a new escape sequence" test. Writing
xterm/VTE SGR or HTML means walking cells and emitting a style change only when
the style handle differs from the previous cell — an `int` comparison, rather
than structural comparison of colour records. Run-length encoding by style falls
out of the representation for free, which is precisely what compact ANSI and
HTML output require.

### 10.5 `long[][]` — packing glyph and style into one cell

```java
long[][] cells;   // high 32 : glyph (code point, or negative cluster handle)
                  // low  32 : style handle
```

Note this is *not* inline colour — truecolor fg + bg + attrs is 24+24+8 = 56 bits
which, with a 21-bit code point, does not fit in 64. Inline packing only works if
colour is capped at xterm-256 (21+8+8+8 = 45 bits). Since VTE and HTML both do
truecolor, style stays a handle either way, so `long[][]` and struct-of-arrays
have **identical capability**. The difference is purely layout.

#### Performance: a dead tie

Four operation shapes an editor actually performs, 3,000 passes over 200×80:

| Operation | SoA (2× `int[][]`) | `long[][]` | Winner |
|---|---:|---:|---|
| glyph-only transform (rot13, fill, case) | 17.7 ms | 27.2 ms | SoA **1.54×** |
| render pass (reads glyph *and* style) | 15.1 ms | 15.0 ms | even |
| block copy / paste (whole cells) | 8.1 ms | 7.5 ms | long 1.08× |
| rotate 180 (whole cells, in place) | 19.8 ms | 11.0 ms | long **1.79×** |
| **total** | **60.7 ms** | **60.7 ms** | **tie** |

`long[][]` wins wherever whole cells move (one `arraycopy`/swap instead of two);
SoA wins glyph-only work (no read-modify-write, half the memory bandwidth, and
style bytes never enter cache). They cancel exactly. **Performance decides
nothing here** — which means the decision has to be made on failure modes.

#### The genuine argument *for* `long[][]`

Glyph and style must always travel together: rotating the canvas must rotate the
colours with it. `long[][]` makes that **structural** — one array, and a cell
cannot move without its style. Struct-of-arrays makes it a **convention**, to be
upheld at each of the 440 `getContent()` call sites (65 of which index the raw
array directly).

That is the same argument this document used in §9 to prefer `int` over `String`
— inseparability should come from the type, not from discipline — so consistency
demands it be taken seriously here rather than waved off.

#### The decisive argument *against*

§6.1's central finding was that 175 `== ' '` comparisons, 25 relational compares,
and 6 arithmetic sites **survive `int[][]` unchanged**, because `char` widens to
`int`. Under `long[][]` those same sites still compile — `char` widens to `long`
too — but are **silently wrong**, because the low 32 bits now hold a style handle:

```java
long cell = ((long) ' ' << 32);     // an ordinary, unstyled space
if (cell == ' ') { ... }            // compiles clean under -Xlint:all. Evaluates FALSE.
```

Verified: `javac -Xlint:all` emits nothing. So `long[][]` converts ~206
mechanical no-ops into ~206 **silent runtime landmines** spread across 56 files.
That is the failure mode that disqualified `String[][]`, except worse — `String`
at least failed at compile time. A refactor this size can absorb a large number
of loud errors; it cannot absorb 206 quiet ones.

Secondary: SoA can **omit the style plane entirely** for monochrome documents
(4 B/cell, zero allocation), while `long[][]` charges every existing piece of
ASCII art 8 B/cell for colour it does not use. Minor at these sizes, but free.

#### Resolution

Keep struct-of-arrays, and answer the atomicity objection in the **API** rather
than the layout: `CharacterPlate` owns both planes and exposes cell-level
`copy` / `move` / `fill` / `clear`, so the 65 raw-indexing sites go through it
instead of walking arrays themselves. That work is required regardless.

And once access is funnelled through that API, **the layout becomes an
implementation detail** — the planes can be fused into `long[][]` later without
touching a single caller, if desync turns out to hurt in practice. The reverse
is not true: starting packed and unpacking later cannot undo silent-comparison
damage already merged. So SoA is also the reversible choice, in the direction
that matters.

### 10.6 `Cell[][]` — a cell *type* that hides the representation

The proposal: refactor to `Cell[][]` regardless, since a `Cell` can hold the
`int`/`long`/`String` internally and callers stop caring which.

**The instinct is right and is adopted — but it applies to the API, not the
array.** These are two different things, and only one of them is free.

#### `Cell` as the API type: free

A `Cell` record materialised by an accessor over plane storage costs nothing —
escape analysis scalarises it away entirely:

| | 200×80 | 1000×1000 |
|---|---:|---:|
| planes, primitive accessors | 0.005 ms/pass | 0.235 ms/pass |
| planes, **`Cell` record per access** | 0.005 ms/pass (**1.01×**) | 0.235 ms/pass (**1.00×**) |
| `Cell[][]` object storage | 0.006 ms/pass (1.09×) | 0.310 ms/pass (1.32×) |

So `plate.cellAt(x, y) -> Cell` and `plate.set(x, y, Cell)` give **100% of the
abstraction benefit at 0% of the cost**, and the storage stays swappable
underneath. (Caveat: scalarisation is reliable in tight loops but not
*guaranteed* — if a `Cell` escapes or reaches an uninlined virtual call it will
be allocated. Keep primitive accessors available for bulk paths.)

#### `Cell` as the array element: forces a bad choice

Making `Cell` the *storage* is the one move that cannot be undone later, and it
forces an unpleasant fork — measured on a write-heavy pass (recolour every cell):

| Storage | write pass | hazard |
|---|---:|---|
| planes (`int[][]`) | 5.9 ms (1.00×) | none — values copy |
| `Cell[][]` **mutable** | 23.0 ms (3.87×) | **aliasing** |
| `Cell[][]` **immutable** record | 135.2 ms (**22.73×**) | none, but allocates on every write |

The aliasing hazard is not theoretical, and it is a *correctness* problem rather
than a performance one:

```java
cells[0][1] = cells[0][0];   // an ordinary copy -- exactly what paste/fill/rotate do
cells[0][1].glyph = 'B';     // edits the SOURCE too; both names point at one object
```

Demonstrated: copy `A` to the next cell, edit the copy, and the source reads `B`.
With planes the same two lines copy a value and cannot alias. Avoiding this means
either immutable cells at **22.73×** on writes, or a rule — "never assign a `Cell`
reference without cloning" — upheld by discipline across the 440 `getContent()`
sites. That is the same shape of argument as §9 and §10.5: an invariant that
should come from the type, not from remembering.

Note `Cell[][]` does *not* share `long[][]`'s silent-comparison flaw — `someCell
== ' '` is a compile error, so the refactor still fails loudly. On failure mode
it sits between planes and `long[][]`.

#### Verdict

Adopt `Cell` as the type the codebase passes around; keep planes as storage.
That is precisely the "callers stop caring about the representation" outcome the
proposal is after — and because callers genuinely stop caring, the storage can
later become `long[][]` or `Cell[][]` without touching them, should this analysis
prove wrong.

### 10.7 Consequences for the rest of this document

- §6 and §7 stand as written; they are about the glyph plane.
- §8 (serialisation) gets larger: the packed format must now carry style runs as
  well as clusters. This strengthens the §8 recommendation to bump the algorithm
  byte and keep a read path for old `A`/`B` streams — those decode as "every cell
  is one BMP code point, style 0", which is a valid document in the new model.
- Colour is a substantially bigger feature than PGA packing (tools, palette UI,
  colour picker, export formats). **Recommend sequencing them:** land the glyph
  plane as `int[][]` first, with the style plane designed-for but not populated,
  then add colour as its own project. The `int[][]` refactor does not need to
  wait for colour design to settle.

---

## 11. Open questions

1. ~~**Group B policy.**~~ **Resolved: store the cluster in one cell and accept
   overdraw.** With width parked (§12.2), this is the only option whose *stored
   data* is already correct for when wide-glyph handling lands — stripping to a
   base code point or leaving components in separate cells would both need the
   data redone later. The visual glitch is a known, bounded follow-up; data loss
   is not.
2. **NFC on input?** Normalizing at the text→grid boundary collapses
   `e`+U+0301 into a single `U+00E9`, avoiding a table entry entirely. But the
   precomposed set is frozen at 12,216 code points and is *lacunary* — `e`+cedilla
   composes to U+0229, `a`+cedilla composes to nothing; U+20D7 composes with no
   base at all. Recommend normalizing (it is free and shrinks the table) while
   relying on nothing.
3. **Old-format read compatibility** — see §8.
4. ~~**Per-cell colour on the roadmap?**~~ **Answered: yes**, and now **parked**
   — see Scope and §12.1.
5. ~~**Colour depth.**~~ **Deferred with the rest of colour.** The style-handle
   layout is indifferent to truecolor / 256 / palette, so nothing about this
   phase depends on it.
6. ~~**Sequencing.**~~ **Confirmed: glyph plane only.** Colour, styling,
   hyperlinks and wide glyphs are separate later projects.

---

## 12. Design space reserved for the parked work

The scope note promises that colour, styling, hyperlinks and wide glyphs are
"left room". That is a claim about the encoding, so it is worth stating exactly
what is being reserved and what would forfeit it.

### 12.1 Colour, styling, hyperlinks — additive, nothing to reserve

These arrive as a **sibling plane** (`int[][] style`) plus an interned `Style`
record, per §10.3. The glyph plane is untouched by them; a document with no
style plane is simply an unstyled document. Nothing in this phase needs to
anticipate them beyond *not* stuffing style bits into the glyph plane — which
§10.5 already rules out on independent grounds.

**Verdict: no reservation required.** The space is free by construction.

### 12.2 Glyphs wider than one column — the one that *does* need reserving

This is the constraining item, because the eventual mechanism has to live in the
glyph plane itself. The standard approach, used by essentially every terminal
emulator, is **continuation cells**: a glyph of advance *N* occupies its own cell
and the following *N−1* cells hold a marker meaning "I am the tail of the glyph
to my left". That marker must be a glyph-plane value distinguishable from both a
code point and a cluster handle.

So the negative range must be **partitioned now**, not consumed:

| Range | Meaning | Capacity |
|---|---|---:|
| `>= 0` | Unicode code point (`0..0x10FFFF`) | — |
| `-1 .. -0x3FFF_FFFF` | cluster-table handle | ~1.07 billion |
| `-0x4000_0000 .. Integer.MIN_VALUE` | **reserved, unused this phase** | ~1.07 billion |

Cluster handles are allocated from `-1` downward and must never enter the
reserved band. `Integer.MIN_VALUE` is earmarked for `CONTINUATION`. This costs
nothing today — the handle space is over a billion either way — and it is the
one decision that would be genuinely expensive to retrofit, since every
persisted document would need rewriting.

Two further cheap measures that keep the option open:

- **Give the cluster record a `columns` field now**, populated as `1` for this
  phase. The advance is already computable at intern time (`stringWidth /
  charWidth('M')`, per the Scope note), so the wide-glyph work becomes "start
  trusting a field that is already there" rather than a schema change.
- **Expose `columns()` on the `Cell` API**, returning `1` unconditionally.
  Callers written this phase are then already shaped correctly, and the renderer
  can keep its fast `x * charWidth` path so long as that arithmetic lives behind
  a single helper rather than being spread across the paint code.

### 12.3 What would forfeit the space

- Allocating cluster handles across the whole negative range.
- Packing style bits into the glyph plane "just for now".
- Writing the cell↔column mapping as an inlined `x * charWidth` in each paint
  and hit-test site, rather than through one helper.

### 12.4 Definition of done for this phase

PGA handling is correct when, for **Group A** clusters (`Mn`, `Me`, and stacks —
the ones measured at advance exactly 1.00 in §2.1):

1. A pasted cluster occupies exactly one cell.
2. Move, copy, delete, and select treat it as one indivisible unit.
3. It round-trips unchanged through save/load, clipboard, and undo/redo.
4. It renders composed within its own cell, leaving the rest of the row aligned
   — i.e. the §3 defect is gone.
5. Cursor motion and backspace step over it as one unit.

**Group B** clusters (emoji, flags, Indic, jamo) satisfy 1–3 and 5, but not 4:
they will overdraw their neighbours until the parked wide-glyph work lands. That
is the accepted trade in question 1 above, and it is a rendering defect only —
the stored document is already correct.

---

## Appendix — reproducing the numbers

Grapheme segmentation and advance ratios:

```java
BreakIterator bi = BreakIterator.getCharacterInstance();
bi.setText(s);
int n = -1; for (int p = bi.first(); p != BreakIterator.DONE; p = bi.next()) n++;

FontMetrics fm = g.getFontMetrics(new Font(Font.MONOSPACED, Font.PLAIN, 14));
double advance = fm.stringWidth(s) / (double) fm.charWidth('M');
```

Idiom census:

```bash
FILES=$(grep -rl 'char\[\]\[\]' src/main/java)
echo "$FILES" | xargs grep -En "[!=]= *'" | wc -l    # survives int[][], breaks String[][]
echo "$FILES" | xargs grep -En 'char +[a-zA-Z_]+ *=' | wc -l   # breaks under both
```

Caveat on the census: counts are line-based greps over the 56 grid-touching
files, so they include a small number of lines that do not actually index a
cell. The ratio between the two columns is the reliable signal, not the absolute
totals.

Font caveat: `SF Mono` is not installed under that name here and silently
resolved to `Dialog`; it was dropped from the table above rather than reported.
