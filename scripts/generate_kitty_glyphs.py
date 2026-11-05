#!/usr/bin/env -S uv run --script
# /// script
# requires-python = ">=3.12"
# dependencies = []
# ///
"""Generate font-independent cell geometry from the audited Unicode inventory.

Usage: uv run scripts/generate_kitty_glyphs.py /path/to/kitty-own-glyphs.md
The input is the 2026-11-05 inventory of kitty 0.50.0 (1098 codepoints).
Geometry is expressed in cell fractions; rasterization belongs to Java2D.
"""

import argparse
import json
import re
from pathlib import Path

OUTPUT = Path(__file__).resolve().parents[1] / "src/main/java/de/jave/jave/rendering/kitty-glyphs.tsv"
DIRECTIONS = {"UP": (0,), "RIGHT": (1,), "DOWN": (2,), "LEFT": (3,), "VERTICAL": (0, 2), "HORIZONTAL": (1, 3)}
WEIGHTS = {"LIGHT": 1, "SINGLE": 1, "HEAVY": 2, "DOUBLE": 3}
POINTS = {
    "UPPER LEFT": (0, 0), "UPPER CENTRE": (.5, 0), "UPPER RIGHT": (1, 0),
    "UPPER MIDDLE LEFT": (0, 1 / 3), "UPPER MIDDLE RIGHT": (1, 1 / 3),
    "MIDDLE LEFT": (0, .5), "MIDDLE CENTRE": (.5, .5), "MIDDLE RIGHT": (1, .5),
    "LOWER MIDDLE LEFT": (0, 2 / 3), "LOWER MIDDLE RIGHT": (1, 2 / 3),
    "LOWER LEFT": (0, 1), "LOWER CENTRE": (.5, 1), "LOWER RIGHT": (1, 1),
}
CORNERS = ("UPPER LEFT", "UPPER RIGHT", "LOWER LEFT", "LOWER RIGHT")
SIDES = ("LEFT", "UPPER", "RIGHT", "LOWER")


def op(name, *args):
    return name + " " + " ".join(format(x, ".9g") for x in args)


def rect(x0, y0, x1, y1):
    return op("rect", x0, y0, x1, y1)


def polygon(points):
    return op("polygon", *(value for point in points for value in point))


def corner_triangle(corner):
    return polygon([POINTS[CORNERS[corner]], POINTS[CORNERS[corner ^ 1]], POINTS[CORNERS[corner ^ 2]]])


def edge_triangle(side, inverse=False):
    return op("triangle", side, int(inverse))


def grid(name, rows, separated=False):
    digits = name.rsplit("-", 1)[1]
    return op("grid", 2, rows, sum(1 << (int(n) - 1) for n in digits), int(separated))


def box(name):
    body = name.removeprefix("BOX DRAWINGS ")
    if "DIAGONAL" in body:
        weight = 3 if "DOUBLE" in body else 1
        body = body.split("DIAGONAL ", 1)[1]
        if body == "CROSS":
            return op("line", 1, 0, 0, 1, 1) + ";" + op("line", 1, 0, 1, 1, 0)
        if body == "DIAMOND":
            return op("line", 1, .5, 0, 0, .5, .5, 1, 1, .5, .5, 0)
        return ";".join(op("line", weight, *(v for p in segment.split(" TO ") for v in POINTS[p])) for segment in body.split(" AND "))
    if "ARC" in body:
        return op("round", {"DOWN AND RIGHT": 0, "DOWN AND LEFT": 1, "UP AND RIGHT": 2, "UP AND LEFT": 3}[body.split("ARC ")[1]])
    if "DASH" in body:
        count = 2 if "DOUBLE DASH" in body else 3 if "TRIPLE" in body else 4
        return op("dash", int("VERTICAL" in body), count, 2 if "HEAVY" in body else 1)
    weights = [0, 0, 0, 0]
    default = WEIGHTS.get(body.split()[0], 1)
    for clause in body.split(" AND "):
        words = clause.split()
        weight = next((WEIGHTS[word] for word in words if word in WEIGHTS), default)
        for word in words:
            for direction in DIRECTIONS.get(word, ()):
                weights[direction] = weight
    assert any(weights), name
    return op("box", *weights)


def private(cp, description):
    if 0xE0B0 <= cp <= 0xE0BF:
        i = cp - 0xE0B0
        if i < 4:
            return op("separator", i)
        if i < 8:
            return op("half", 2 if i in (4, 5) else 0, int(i % 2 == 0), 0)
        if i % 2:
            return op("line", 1, 0, 0 if i in (9, 15) else 1, 1, 1 if i in (9, 15) else 0)
        return corner_triangle({8: 2, 10: 3, 12: 0, 14: 1}[i])
    if cp in (0xE0D6, 0xE0D7):
        return op("separator", 4 if cp == 0xE0D6 else 5)
    if 0xEE00 <= cp <= 0xEE05:
        return op("progress", (cp - 0xEE00) % 3, int(cp >= 0xEE03))
    if 0xEE06 <= cp <= 0xEE0B:
        start, end = [(235, 305), (270, 390), (315, 470), (360, 540), (80, 220), (170, 270)][cp - 0xEE06]
        return op("circle", 0, start, end)
    if cp >= 0xF5EE:
        edges = 0
        for word, bit in (("top", 1), ("right", 2), ("bottom", 4), ("left", 8)):
            if word in description:
                edges |= bit
        return op("commit", edges, int(description.startswith("Filled")))
    if cp <= 0xF5D1:
        return op("box", 0, 1, 0, 1) if cp == 0xF5D0 else op("box", 1, 0, 1, 0)
    if cp <= 0xF5D5:
        return op("fade", cp - 0xF5D2)
    operations = []
    if description.startswith("Vertical line"):
        operations.append(op("box", 1, 0, 1, 0))
    if description.startswith("Horizontal line"):
        operations.append(op("box", 0, 1, 0, 1))
    for glyph, corner in (("╭", 0), ("╮", 1), ("╰", 2), ("╯", 3)):
        if glyph in description:
            operations.append(op("round", corner))
    assert operations, description
    return ";".join(operations)


def geometry(cp, name):
    if 0xE000 <= cp <= 0xF8FF:
        return private(cp, name)
    if 0x2800 <= cp <= 0x28FF:
        return op("braille", cp - 0x2800)
    if "BLOCK OCTANT-" in name:
        return grid(name, 4)
    if "BLOCK SEXTANT-" in name:
        return grid(name, 3, "SEPARATED" in name)
    if "SEPARATED BLOCK QUADRANT-" in name:
        return grid(name, 2, True)
    if 0x1CE90 <= cp <= 0x1CE9F:
        i = cp - 0x1CE90
        return rect(i % 4 / 4, i // 4 / 4, (i % 4 + 1) / 4, (i // 4 + 1) / 4)
    if 0x1CEA0 <= cp <= 0x1CEAF:
        return op("strip", cp - 0x1CEA0)
    if 0x1CC30 <= cp <= 0x1CC3F:
        return op("circleTile", cp - 0x1CC30)
    offsets = {
        0x1CC1B: [(0, .5, 1, .5), (.75, 0, .75, .5)],
        0x1CC1C: [(0, .5, 1, .5), (.75, .5, .75, 1)],
        0x1CC1D: [(.5, 0, .5, .25), (0, .25, .5, .25)],
        0x1CC1E: [(.5, .75, .5, 1), (0, .75, .5, .75)],
        0x1CE16: [(.5, 0, .5, 1), (.5, .25, 1, .25)],
        0x1CE17: [(.5, 0, .5, 1), (.5, .75, 1, .75)],
        0x1CE18: [(.5, 0, .5, 1), (0, .25, .5, .25)],
        0x1CE19: [(.5, 0, .5, 1), (0, .75, .5, .75)],
    }
    if cp in offsets:
        return ";".join(op("line", 1, *line) for line in offsets[cp])
    if name.startswith("BOX DRAWINGS"):
        return box(name)
    if name.startswith("QUADRANT "):
        mask = sum(1 << i for i, corner in enumerate(CORNERS) if corner in name)
        return op("grid", 2, 2, mask, 0)
    if "BLOCK DIAGONAL" in name:
        corner, segment = name.split(" BLOCK DIAGONAL ")
        a, b = (POINTS[p] for p in segment.split(" TO "))
        return op("mosaic", CORNERS.index(corner), *a, *b)
    if "TRIANGULAR" in name and "SHADE" not in name:
        sides = [i for i, side in enumerate(SIDES) if side in name]
        if "THREE QUARTERS" in name:
            return edge_triangle(next(i for i in range(4) if i not in sides), True)
        return ";".join(edge_triangle(side) for side in sides)
    if "SHADE" in name or name in ("CHECKER BOARD FILL", "INVERSE CHECKER BOARD FILL", "HEAVY HORIZONTAL FILL"):
        return op("shade", cp)
    if name in ("UPPER LEFT TO LOWER RIGHT FILL", "UPPER RIGHT TO LOWER LEFT FILL"):
        return op("hatch", int("UPPER RIGHT" in name))
    if name == "FULL BLOCK":
        return rect(0, 0, 1, 1)
    if "ONE EIGHTH BLOCK-" in name:
        rows = "HORIZONTAL" in name
        return ";".join(rect(0, (int(n)-1)/8, 1, int(n)/8) if rows else rect((int(n)-1)/8, 0, int(n)/8, 1) for n in name.split("-")[1])
    if "ONE EIGHTH BLOCK" in name and " AND " in name:
        return ";".join(rect(0, 0, .125, 1) if side == "LEFT" else rect(.875, 0, 1, 1) if side == "RIGHT" else rect(0, 0, 1, .125) if side == "UPPER" else rect(0, .875, 1, 1) for side in name.removesuffix(" ONE EIGHTH BLOCK").split(" AND "))
    if "BLOCK" in name:
        fractions = {"ONE EIGHTH": .125, "ONE QUARTER": .25, "THREE EIGHTHS": .375, "HALF": .5, "FIVE EIGHTHS": .625, "THREE QUARTERS": .75, "SEVEN EIGHTHS": .875, "ONE THIRD": 1/3, "TWO THIRDS": 2/3}
        if name in ("UPPER CENTRE ONE QUARTER BLOCK", "LOWER CENTRE ONE QUARTER BLOCK"):
            return rect(.25, 0 if name.startswith("UPPER") else .5, .75, .5 if name.startswith("UPPER") else 1)
        if name in ("MIDDLE LEFT ONE QUARTER BLOCK", "MIDDLE RIGHT ONE QUARTER BLOCK"):
            return rect(0 if "LEFT" in name else .5, .25, .5 if "LEFT" in name else 1, .75)
        for text, fraction in fractions.items():
            if text in name:
                if name.startswith("UPPER"):
                    return rect(0, 0, 1, fraction)
                if name.startswith("LOWER"):
                    return rect(0, 1-fraction, 1, 1)
                if name.startswith("LEFT"):
                    return rect(0, 0, fraction, 1)
                if name.startswith("RIGHT"):
                    return rect(1-fraction, 0, 1, 1)
    if cp in (0x25C9, 0x25CB, 0x25CF):
        return op("circle", {0x25CB: 0, 0x25CF: 1, 0x25C9: 2}[cp], 0, 360)
    if cp in (0x25D6, 0x25D7):
        return op("half", 0 if cp == 0x25D6 else 2, 1, 0)
    if 0x25DC <= cp <= 0x25E1:
        start, end = [(180, 270), (270, 360), (360, 450), (450, 540), (180, 360), (0, 180)][cp-0x25DC]
        return op("circle", 0, start, end)
    if 0x25E2 <= cp <= 0x25E5:
        return corner_triangle((3, 2, 0, 1)[cp-0x25E2])
    if 0x1FBE0 <= cp <= 0x1FBE3 or 0x1FBE8 <= cp <= 0x1FBEB:
        return op("half", (3, 0, 1, 2)[cp % 8], int(cp >= 0x1FBE8), 1)
    if 0x1FBEC <= cp <= 0x1FBEF:
        return op("quarterCircle", (1, 2, 3, 0)[cp-0x1FBEC])
    raise ValueError(f"No geometry for U+{cp:X}: {name}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("inventory", type=Path)
    args = parser.parse_args()
    source = args.inventory.read_text()
    rows = {}
    for line in source.splitlines():
        match = re.match(r"\| `U\+([0-9A-F]+)` \| `[^`]*` \| (.*)", line)
        if match:
            cp = int(match[1], 16)
            name = match[2].split(" | ", 1)[0].removesuffix(" |").replace(r"\|", "|")
            if cp == 0x1FB93:
                name = "RESERVED: LEFT HALF BLOCK AND RIGHT HALF INVERSE MEDIUM SHADE"
            assert cp not in rows, hex(cp)
            rows[cp] = name
    coverage = json.loads(re.search(r"```json\n(.*?)\n```", source, re.S)[1])
    expected = {cp for start, end in coverage["inclusive_hex_ranges"] for cp in range(int(start, 16), int(end, 16) + 1)}
    assert rows.keys() == expected and len(rows) == 1098
    output = ["# Font-independent cell geometry; generated by scripts/generate_kitty_glyphs.py.",
              "# Coverage: kitty 0.50.0 audit, 2026-11-05, source 852fef34534b232b4496be2d0fe09586bfa44bc1.",
              "# Unicode names and private-use descriptions from kitty-own-glyphs.md; geometry uses cell fractions.",
              "# codepoint<TAB>drawing operations<TAB>name"]
    for cp, name in sorted(rows.items()):
        output.append(f"{cp:X}\t{geometry(cp, name).strip()}\t{name}")
    OUTPUT.write_text("\n".join(output) + "\n")
    print(f"Generated {len(rows)} glyph definitions in {OUTPUT}")


if __name__ == "__main__":
    main()
