#!/usr/bin/env -S uv run --script
# /// script
# requires-python = ">=3.10"
# dependencies = []
# ///
"""Render an ASCII-art text file to PostScript with a rainbow gradient.

Each non-space character gets its own RGB color (HSV hue stepping across the
art at a configurable angle). Output is plain-text PostScript suitable for
`ps2pdf`, Preview.app, or `lpr` directly. With --pdf the script also runs the
system `ps2pdf` (Ghostscript) to produce a PDF alongside.
"""
from __future__ import annotations

import argparse
import colorsys
import math
import shutil
import subprocess
import sys
from pathlib import Path

LETTER_W, LETTER_H = 612.0, 792.0  # points (1pt = 1/72 in)

LENGTH_UNITS = {
    "pt": 1.0,
    "in": 72.0,
    "mm": 72.0 / 25.4,
    "cm": 72.0 / 2.54,
}


def parse_length_pt(s: str) -> float:
    """Parse a length like '0.25in', '36', '6mm' into points."""
    raw = s.strip().lower()
    for suffix, mult in LENGTH_UNITS.items():
        if raw.endswith(suffix):
            return float(raw[: -len(suffix)]) * mult
    return float(raw)  # bare number = points


def ps_escape(ch: str) -> str:
    return ch.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")


DEFAULT_FONT_PATH_DIRS = [
    str(Path.home() / "Library" / "Fonts"),
    "/Library/Fonts",
    "/System/Library/Fonts",
    "/System/Library/Fonts/Supplemental",
]


def default_font_path() -> str:
    """Colon-separated directories to feed gs's -sFONTPATH (existing dirs only)."""
    return ":".join(d for d in DEFAULT_FONT_PATH_DIRS if Path(d).is_dir())


def _gs_query(ps: str, font_path: str = "") -> list[str] | None:
    """Run a PostScript snippet under gs and return printed (name) lines."""
    gs = shutil.which("gs")
    if not gs:
        return None
    cmd = [gs, "-q", "-dNODISPLAY", "-dBATCH"]
    if font_path:
        cmd.append(f"-sFONTPATH={font_path}")
    cmd += ["-c", ps]
    try:
        result = subprocess.run(cmd, capture_output=True, text=True, timeout=15)
    except (subprocess.TimeoutExpired, OSError):
        return None
    if result.returncode != 0:
        return None
    fonts = sorted({
        line[1:-1]
        for line in (raw.strip() for raw in result.stdout.splitlines())
        if line.startswith("(") and line.endswith(")")
    })
    return fonts or None


def _fc_list(mono_only: bool) -> list[str] | None:
    """Use fontconfig's fc-list to enumerate system fonts by PostScript name."""
    fc = shutil.which("fc-list")
    if not fc:
        return None
    args = [fc]
    if mono_only:
        args.append(":spacing=100")
    args += ["-f", "%{postscriptname}\n"]
    try:
        result = subprocess.run(args, capture_output=True, text=True, timeout=15)
    except (subprocess.TimeoutExpired, OSError):
        return None
    if result.returncode != 0:
        return None
    names = sorted({line.strip() for line in result.stdout.splitlines() if line.strip()})
    return names or None


def list_all_fonts(font_path: str = "") -> list[str] | None:
    """Union of gs's built-in fonts and fc-list system fonts."""
    gs = _gs_query("(*) {== } 256 string /Font resourceforall", font_path) or []
    fc = _fc_list(mono_only=False) or []
    combined = sorted(set(gs) | set(fc))
    return combined or None


def list_mono_fonts(font_path: str = "") -> list[str] | None:
    """Union of gs's mono built-ins (i/M width test) and fc-list :spacing=100 fonts."""
    gs_mono = _gs_query(
        "(*) { /n exch def "
        "n findfont 100 scalefont setfont "
        "(i) stringwidth pop "
        "(M) stringwidth pop "
        "sub abs 0.5 lt { n == } if "
        "} 256 string /Font resourceforall",
        font_path,
    ) or []
    fc_mono = _fc_list(mono_only=True) or []
    combined = sorted(set(gs_mono) | set(fc_mono))
    return combined or None


def parse_angle_spec(spec: str) -> list[float]:
    """Parse --angle as either 'N' or 'F,T,S' (range: from, to-inclusive, step)."""
    parts = [p.strip() for p in spec.split(",")]
    if len(parts) == 1:
        return [float(parts[0])]
    if len(parts) != 3:
        raise argparse.ArgumentTypeError(
            f"--angle must be 'N' or 'F,T,S' (got {spec!r})"
        )
    f, t, s = (float(x) for x in parts)
    if s == 0:
        raise argparse.ArgumentTypeError("--angle step must be non-zero")
    if (t - f) * s < 0:
        raise argparse.ArgumentTypeError(
            f"--angle step sign disagrees with direction from {f} to {t}"
        )
    # Iterate by integer index to avoid floating-point drift, and include the
    # endpoint when it lands within half a step of t.
    eps = abs(s) * 1e-9
    out: list[float] = []
    i = 0
    while True:
        n = f + i * s
        if (s > 0 and n > t + eps) or (s < 0 and n < t - eps):
            break
        out.append(n)
        i += 1
    if not out:
        raise argparse.ArgumentTypeError(f"--angle {spec!r} produced no values")
    return out


def build_ps(
    lines: list[str],
    *,
    font: str,
    size: float,
    leading: float,
    saturation: float,
    value: float,
    hue_start: float,
    hue_end: float,
    angle_deg: float,
    aspect: float,
    page_w: float,
    page_h: float,
    faux_bold: float,
    title: str,
) -> str:
    max_cols = max(len(l) for l in lines)
    n_lines = len(lines)
    line_h = size * leading

    # Angle is the tilt of iso-hue lines from vertical, measured the way you'd
    # read it on the printed page: 0 = vertical |, +45 = /, -45 = \, ±90 = —.
    # Gradient direction (perpendicular to iso-hue) in screen coords is then
    # (cos A, sin A) with screen-y pointing down.
    a = math.radians(angle_deg)
    cos_a, sin_a = math.cos(a), math.sin(a)
    # Use physical units (cw vs lh) so the angle is geometrically correct
    # rather than grid-cell-correct. cw ≈ aspect * size; lh = leading * size.
    cw_phys = aspect * size
    lh_phys = line_h

    def proj(col: int, row: int) -> float:
        return col * cw_phys * cos_a + row * lh_phys * sin_a

    # Normalize projection over the bounding rectangle so the full hue range
    # spans the actual extent of the art at this angle.
    corners = [proj(c, r) for c in (0, max_cols - 1) for r in (0, n_lines - 1)]
    pmin, pmax = min(corners), max(corners)
    span = pmax - pmin if pmax > pmin else 1.0

    def hue_for(col: int, row: int) -> float:
        t = (proj(col, row) - pmin) / span
        return hue_start + (hue_end - hue_start) * t

    out: list[str] = []
    out.append("%!PS-Adobe-3.0")
    out.append(f"%%BoundingBox: 0 0 {int(page_w)} {int(page_h)}")
    out.append(f"%%Title: {title}")
    out.append("%%Pages: 1")
    out.append("%%EndComments")
    out.append("%%Page: 1 1")
    # Tell viewers/printers the page size (matters for landscape).
    out.append(f"<< /PageSize [{page_w:g} {page_h:g}] >> setpagedevice")
    out.append(f"/{font} findfont {size} scalefont setfont")
    # Faux-bold: build the glyph outline with charpath, then both stroke and
    # fill that exact path. Using charpath for the fill (instead of show) means
    # stroke and fill share the same unhinted outline, eliminating the hinting-
    # induced halo you'd get from mixing show (hinted) with stroke (unhinted).
    if faux_bold > 0:
        out.append(
            "/bshow { "
            "/s exch def "
            "currentpoint /cy exch def /cx exch def "
            "newpath cx cy moveto "
            "s true charpath "
            "gsave "
            f"{faux_bold:g} setlinewidth 1 setlinejoin 1 setlinecap stroke "
            "grestore "
            "fill "
            "} bind def"
        )
    show_op = "bshow" if faux_bold > 0 else "show"
    # Advance width of one monospace char (computed at runtime so any font works).
    out.append("/cw ( ) stringwidth pop def")
    out.append(f"/blockw {max_cols} cw mul def")
    out.append(f"/blockh {n_lines * line_h} def")
    out.append(f"/x0 {page_w:g} blockw sub 2 div def")
    # Top baseline so the block is vertically centered.
    out.append(f"/y0 {page_h:g} blockh add 2 div {line_h} sub def")
    out.append(f"/lh {line_h} def")

    for row, line in enumerate(lines):
        for col, ch in enumerate(line):
            if ch == " ":
                continue
            h = hue_for(col, row)
            r, g, b = colorsys.hsv_to_rgb(h, saturation, value)
            out.append(
                f"{r:.3f} {g:.3f} {b:.3f} setrgbcolor "
                f"x0 {col} cw mul add y0 {row} lh mul sub moveto "
                f"({ps_escape(ch)}) {show_op}"
            )

    out.append("showpage")
    out.append("%%EOF")
    return "\n".join(out) + "\n"


def main() -> int:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("input", type=Path, nargs="?", help="ASCII-art text file")
    p.add_argument("--list-fonts", action="store_true",
                   help="list available PostScript font names (gs built-ins + system TTF/OTF) and exit")
    p.add_argument("--list-mono-fonts", action="store_true",
                   help="list only monospace fonts and exit")
    p.add_argument("--no-font-check", action="store_true",
                   help="skip validating --font against the available font list")
    p.add_argument("--font-path", default=default_font_path(),
                   help="colon-separated directories to scan for TTF/OTF fonts "
                        "(passed to gs as -sFONTPATH; default: macOS user/system font dirs)")
    p.add_argument("-o", "--output", type=Path,
                   help="output .ps filename (default: <input-stem>.ps in --out-dir). "
                        "If this contains a path separator it's used verbatim and --out-dir is ignored.")
    p.add_argument("--out-dir", type=Path, default=Path("out"),
                   help="directory to write outputs into (default: ./out/)")
    p.add_argument("--pdf", action="store_true",
                   help="also run ps2pdf to produce a .pdf alongside the .ps")
    p.add_argument("--font", default="Courier",
                   help="PostScript font name; must be monospace (default: Courier)")
    p.add_argument("--bold", action="store_true",
                   help="use the bold variant of --font (appends '-Bold' if not already present)")
    p.add_argument("--faux-bold", type=float, default=0.0, metavar="W",
                   help="extra stroke weight in points layered on top of the glyph fill "
                        "(default 0 = off; try 0.3-1.0). Stacks with --bold.")
    p.add_argument("--size", type=float, default=None,
                   help="font size in points (default: auto-fit to page minus --margin)")
    p.add_argument("--margin", type=parse_length_pt, default=parse_length_pt("0.25in"),
                   help="page margin with unit suffix (pt|in|mm|cm); "
                        "default 0.25in. Bare number = points.")
    p.add_argument("--leading", type=float, default=1.0,
                   help="line-height multiplier (default: 1.0 — tight for ASCII art)")
    p.add_argument("--saturation", type=float, default=1.0)
    p.add_argument("--value", type=float, default=0.9)
    p.add_argument("--hue-start", type=float, default=0.0,
                   help="starting HSV hue 0..1 (default 0.0 = red)")
    p.add_argument("--hue-end", type=float, default=0.83,
                   help="ending HSV hue 0..1 (default 0.83 = violet; avoids wrap to red)")
    p.add_argument("--angle", type=parse_angle_spec, default=[0.0],
                   help="tilt of iso-hue lines from vertical in degrees. "
                        "Single value (e.g. 45) or range 'F,T,S' = from,to-inclusive,step "
                        "(e.g. '0,90,15' renders 0,15,30,...,90). "
                        "0 = | (default), 45 = /, -45 = \\, 90 = horizontal bands. "
                        "Range mode appends '-a<N>' to each output filename.")
    p.add_argument("--aspect", type=float, default=0.6,
                   help="character-width / font-size ratio of the chosen monospace font "
                        "(default 0.6 — correct for Courier; only affects --angle geometry)")
    p.add_argument("-p", "--paper-orientation", default="portrait",
                   choices=["p", "portrait", "l", "landscape"],
                   help="paper orientation (default: portrait)")
    args = p.parse_args()

    if args.list_fonts or args.list_mono_fonts:
        fonts = (list_mono_fonts(args.font_path) if args.list_mono_fonts
                 else list_all_fonts(args.font_path))
        if fonts is None:
            print("error: could not query fonts (need Ghostscript and/or fontconfig)",
                  file=sys.stderr)
            return 2
        for f in fonts:
            print(f)
        return 0

    if args.input is None:
        p.error("input is required (or use --list-fonts)")

    landscape = args.paper_orientation in ("l", "landscape")
    page_w, page_h = (LETTER_H, LETTER_W) if landscape else (LETTER_W, LETTER_H)

    raw = args.input.read_text().rstrip("\n").splitlines()
    if not raw:
        print(f"error: {args.input} is empty", file=sys.stderr)
        return 1

    font = args.font
    if args.bold and not font.endswith("-Bold"):
        font = f"{font}-Bold"

    if not args.no_font_check:
        fonts = list_all_fonts(args.font_path)
        if fonts is None:
            print("warning: could not validate font (need gs and/or fc-list); "
                  "skipping check", file=sys.stderr)
        elif font not in fonts:
            close = [f for f in fonts if font.lower() in f.lower()][:5]
            hint = f" Did you mean: {', '.join(close)}?" if close else ""
            print(f"error: font {font!r} is not known.{hint} "
                  f"Run with --list-fonts to see available fonts, "
                  f"or pass --no-font-check to skip this check.", file=sys.stderr)
            return 3

    # Compute font size: explicit --size, or auto-fit within page minus 2*margin.
    max_cols = max(len(l) for l in raw)
    n_lines = len(raw)
    avail_w = page_w - 2 * args.margin
    avail_h = page_h - 2 * args.margin
    if avail_w <= 0 or avail_h <= 0:
        print(f"error: --margin {args.margin}pt leaves no room on page "
              f"({page_w:g}x{page_h:g}pt)", file=sys.stderr)
        return 1
    fit_w = avail_w / (max_cols * args.aspect)
    fit_h = avail_h / (n_lines * args.leading)
    auto_size = min(fit_w, fit_h)
    if args.size is None:
        size = auto_size
        limit = "width" if fit_w < fit_h else "height"
        print(f"auto-fit size = {size:.2f}pt ({limit}-limited)", file=sys.stderr)
    else:
        size = args.size
        if size > auto_size + 1e-6:
            print(
                f"warning: --size {size:g}pt exceeds the {auto_size:.2f}pt that fits "
                f"within --margin {args.margin:g}pt on a {page_w:g}x{page_h:g}pt page",
                file=sys.stderr,
            )

    angles: list[float] = args.angle
    multi = len(angles) > 1

    if args.output and ("/" in str(args.output) or args.output.is_absolute()):
        base_path = args.output
    else:
        args.out_dir.mkdir(parents=True, exist_ok=True)
        base_name = args.output if args.output else Path(f"{args.input.stem}.ps")
        base_path = args.out_dir / base_name

    ps2pdf = None
    if args.pdf:
        ps2pdf = shutil.which("ps2pdf")
        if not ps2pdf:
            print("error: --pdf requires ps2pdf (install Ghostscript: brew install ghostscript)",
                  file=sys.stderr)
            return 2

    for angle in angles:
        if multi:
            ps_path = base_path.with_name(f"{base_path.stem}-a{angle:g}{base_path.suffix}")
        else:
            ps_path = base_path

        ps = build_ps(
            raw,
            font=font,
            size=size,
            leading=args.leading,
            saturation=args.saturation,
            value=args.value,
            hue_start=args.hue_start,
            hue_end=args.hue_end,
            angle_deg=angle,
            aspect=args.aspect,
            page_w=page_w,
            page_h=page_h,
            faux_bold=args.faux_bold,
            title=f"{args.input.name} (rainbow, angle {angle:g})",
        )
        ps_path.parent.mkdir(parents=True, exist_ok=True)
        ps_path.write_text(ps)
        print(f"wrote {ps_path}")

        if ps2pdf:
            pdf_path = ps_path.with_suffix(".pdf")
            ps2pdf_cmd = [
                ps2pdf,
                f"-dDEVICEWIDTHPOINTS={page_w:g}",
                f"-dDEVICEHEIGHTPOINTS={page_h:g}",
                "-dFIXEDMEDIA",
            ]
            if args.font_path:
                ps2pdf_cmd.append(f"-sFONTPATH={args.font_path}")
            ps2pdf_cmd += [str(ps_path), str(pdf_path)]
            result = subprocess.run(ps2pdf_cmd, capture_output=True, text=True)
            if result.returncode != 0:
                sys.stderr.write(result.stderr)
                return result.returncode
            print(f"wrote {pdf_path}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
