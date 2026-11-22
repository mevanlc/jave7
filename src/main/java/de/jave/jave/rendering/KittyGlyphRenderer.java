package de.jave.jave.rendering;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Font-independent cell drawings for the codepoints in the kitty 0.50.0 inventory. */
public final class KittyGlyphRenderer {
   private static final Map<Integer, List<Operation>> DRAWINGS = loadDrawings();

   private KittyGlyphRenderer() {
   }

   public static boolean supports(int codePoint) {
      return DRAWINGS.containsKey(codePoint);
   }

   static Set<Integer> supportedCodePoints() {
      return DRAWINGS.keySet();
   }

   /** Returns true even for intentionally blank drawings, such as U+2800. */
   public static boolean draw(Graphics graphics, int codePoint, int x, int y, int width, int height) {
      List<Operation> drawing = DRAWINGS.get(codePoint);
      if (drawing == null || !(graphics instanceof Graphics2D original)) {
         return false;
      }
      if (width > 0 && height > 0) {
         Graphics2D copy = (Graphics2D)original.create();
         try {
            copy.translate(x, y);
            copy.clipRect(0, 0, width, height);
            Canvas canvas = new Canvas(copy, width, height, codePoint < 0x2500 || codePoint > 0x257F);
            for (Operation operation : drawing) {
               canvas.draw(operation);
            }
         } finally {
            copy.dispose();
         }
      }
      return true;
   }

   private static Map<Integer, List<Operation>> loadDrawings() {
      Map<Integer, List<Operation>> drawings = new HashMap<>();
      var resource = KittyGlyphRenderer.class.getResourceAsStream("kitty-glyphs.tsv");
      if (resource == null) {
         throw new IllegalStateException("Missing kitty glyph drawings");
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
         for (String line; (line = reader.readLine()) != null;) {
            if (line.isBlank() || line.startsWith("#")) {
               continue;
            }
            String[] fields = line.split("\t", 3);
            int codePoint = Integer.parseInt(fields[0], 16);
            List<Operation> operations = Arrays.stream(fields[1].split(";")).map(Operation::parse).toList();
            if (drawings.put(codePoint, operations) != null) {
               throw new IllegalStateException("Duplicate kitty glyph: " + fields[0]);
            }
         }
      } catch (IOException | IllegalArgumentException e) {
         throw new IllegalStateException("Cannot read kitty glyph drawings", e);
      }
      return Map.copyOf(drawings);
   }

   private record Operation(String kind, double[] args) {
      static Operation parse(String text) {
         String[] words = text.strip().split(" +");
         double[] args = new double[words.length - 1];
         for (int i = 1; i < words.length; i++) {
            args[i - 1] = Double.parseDouble(words[i]);
         }
         return new Operation(words[0], args);
      }
   }

   private static final class Canvas {
      private final Graphics2D graphics;
      private final int width;
      private final int height;
      private final int light;
      private final boolean smooth;

      Canvas(Graphics2D graphics, int width, int height, boolean smooth) {
         this.graphics = graphics;
         this.width = width;
         this.height = height;
         this.light = Math.max(1, (int)Math.round(Math.min(width / 6.0, height / 12.0)));
         this.smooth = smooth;
      }

      void draw(Operation operation) {
         double[] a = operation.args();
         switch (operation.kind()) {
            case "rect" -> rect(a[0], a[1], a[2], a[3]);
            case "polygon" -> fill(polygon(a));
            case "box" -> box(a);
            case "line" -> line((int)a[0], Arrays.copyOfRange(a, 1, a.length));
            case "round" -> roundedCorner((int)a[0]);
            case "dash" -> dashedLine(a[0] != 0, (int)a[1], (int)a[2]);
            case "grid" -> grid((int)a[0], (int)a[1], (int)a[2], a[3] != 0);
            case "braille" -> braille((int)a[0]);
            case "triangle" -> triangle((int)a[0], a[1] != 0);
            case "mosaic" -> mosaic((int)a[0], a[1], a[2], a[3], a[4]);
            case "circle" -> circle((int)a[0], a[1], a[2]);
            case "half" -> halfCircle((int)a[0], a[1] != 0, a[2] != 0);
            case "quarterCircle" -> quarterCircle((int)a[0]);
            case "circleTile" -> circleTile((int)a[0]);
            case "separator" -> separator((int)a[0]);
            case "progress" -> progress((int)a[0], a[1] != 0);
            case "commit" -> commit((int)a[0], a[1] != 0);
            case "fade" -> fade((int)a[0]);
            case "shade" -> shade((int)a[0]);
            case "hatch" -> hatch(a[0] != 0);
            case "strip" -> strip((int)a[0]);
            default -> throw new IllegalStateException("Unknown kitty drawing operation: " + operation.kind());
         }
      }

      private void antialias(boolean enabled) {
         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            enabled && smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      }

      private int pixel(double fraction, int size) {
         return (int)Math.floor(fraction * size + 0.000001);
      }

      private void rect(double x0, double y0, double x1, double y1) {
         antialias(false);
         int x = pixel(x0, width);
         int y = pixel(y0, height);
         graphics.fillRect(x, y, pixel(x1, width) - x, pixel(y1, height) - y);
      }

      private Shape polygon(double... points) {
         Path2D path = new Path2D.Double();
         path.moveTo(points[0] * width, points[1] * height);
         for (int i = 2; i < points.length; i += 2) {
            path.lineTo(points[i] * width, points[i + 1] * height);
         }
         path.closePath();
         return path;
      }

      private void fill(Shape shape) {
         antialias(true);
         graphics.fill(shape);
      }

      private void stroke(Shape shape, double thickness) {
         antialias(true);
         graphics.setStroke(new BasicStroke((float)thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
         graphics.draw(shape);
      }

      private void line(int weight, double... points) {
         if (weight == 3) {
            // Double diagonals use two parallel paths, with a gap in between.
            double dx = (points[2] - points[0]) * width;
            double dy = (points[3] - points[1]) * height;
            double length = Math.hypot(dx, dy);
            double nx = length == 0 ? 0 : -dy / length * light;
            double ny = length == 0 ? 0 : dx / length * light;
            for (int sign : new int[]{-1, 1}) {
               Path2D path = new Path2D.Double();
               path.moveTo(points[0] * width + sign * nx, points[1] * height + sign * ny);
               path.lineTo(points[2] * width + sign * nx, points[3] * height + sign * ny);
               stroke(path, light);
            }
            return;
         }
         Path2D path = new Path2D.Double();
         double thickness = weight == 2 ? 2 * light : light;
         // Pixel-align straight lines while leaving diagonal geometry continuous.
         boolean vertical = points.length == 4 && points[0] == points[2];
         boolean horizontal = points.length == 4 && points[1] == points[3];
         for (int i = 0; i < points.length; i += 2) {
            double x = points[i] * width;
            double y = points[i + 1] * height;
            if (vertical) x = Math.floor(x) + ((int)thickness % 2) * .5;
            if (horizontal) y = Math.floor(y) + ((int)thickness % 2) * .5;
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
         }
         stroke(path, thickness);
      }

      private void box(double[] weights) {
         Area drawing = new Area();
         double cx = width / 2;
         double cy = height / 2;
         for (int direction = 0; direction < 4; direction++) {
            int weight = (int)weights[direction];
            if (weight == 0) continue;
            double thickness = weight == 2 ? 2 * light : light;
            double[] offsets = weight == 3 ? new double[]{-light, light} : new double[]{0};
            for (double offset : offsets) {
               boolean vertical = direction % 2 == 0;
               int before = vertical ? 3 : 0;
               int after = vertical ? 1 : 2;
               double reach = 0;
               if (weights[before] != 0 || weights[after] != 0) {
                  if (weights[before] == 3 || weights[after] == 3) {
                     reach = light;
                     if (weight == 3 && (weights[before] == 0 || weights[after] == 0)) {
                        double sign = weights[after] != 0 ? 1 : -1;
                        reach = -sign * offset;
                     }
                  } else {
                     reach = Math.max(weights[before] == 2 ? light : light / 2.0,
                        weights[after] == 2 ? light : light / 2.0);
                  }
               }
               double perpendicular = (vertical ? cx : cy) + offset - Math.floor(thickness / 2);
               double end = vertical ? cy : cx;
               double start = direction == 0 || direction == 3 ? 0 : end - reach;
               double length = direction == 0 || direction == 3 ? end + reach : (vertical ? height : width) - start;
               drawing.add(new Area(vertical
                  ? new Rectangle2D.Double(perpendicular, start, thickness, length)
                  : new Rectangle2D.Double(start, perpendicular, length, thickness)));
            }
         }
         antialias(false);
         graphics.fill(drawing);
      }

      private void roundedCorner(int corner) {
         double cx = width / 2 + light % 2 * .5;
         double cy = height / 2 + light % 2 * .5;
         int sx = corner % 2 == 0 ? 1 : -1;
         int sy = corner < 2 ? 1 : -1;
         // Leave a straight end at every cell boundary, including odd cell sizes.
         double r = Math.max(0, Math.min(Math.min(cx, width - cx), Math.min(cy, height - cy)) - 1);
         Path2D path = new Path2D.Double();
         path.moveTo(cx, sy > 0 ? height : 0);
         path.lineTo(cx, cy + sy * r);
         path.quadTo(cx, cy, cx + sx * r, cy);
         path.lineTo(sx > 0 ? width : 0, cy);
         antialias(false);
         // Use the same fill rasterization as box()'s straight rectangles.
         graphics.fill(new BasicStroke(light, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND).createStrokedShape(path));
      }

      private void dashedLine(boolean vertical, int count, int weight) {
         for (int i = 0; i < count; i++) {
            double a = (i + .15) / count;
            double b = (i + .85) / count;
            line(weight, vertical ? new double[]{.5, a, .5, b} : new double[]{a, .5, b, .5});
         }
      }

      private void grid(int columns, int rows, int mask, boolean separated) {
         antialias(false);
         int gapX = separated ? Math.max(1, width / 8) : 0;
         int gapY = separated ? Math.max(1, height / 8) : 0;
         int availableWidth = Math.max(0, width - (columns + 1) * gapX);
         int availableHeight = Math.max(0, height - (rows + 1) * gapY);
         for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
               if ((mask & (1 << (row * columns + col))) == 0) continue;
               if (separated) {
                  int w = availableWidth / columns;
                  int h = availableHeight / rows;
                  graphics.fillRect(gapX + col * (w + gapX), gapY + row * (h + gapY), w, h);
               } else {
                  rect((double)col / columns, (double)row / rows, (double)(col + 1) / columns, (double)(row + 1) / rows);
               }
            }
         }
      }

      private void braille(int mask) {
         antialias(false);
         int[] columns = {0, 0, 0, 1, 1, 1, 0, 1};
         int[] rows = {0, 1, 2, 0, 1, 2, 3, 3};
         int w = Math.max(1, width / 4);
         int h = Math.max(1, height / 8);
         for (int bit = 0; bit < 8; bit++) {
            if ((mask & (1 << bit)) != 0) {
               int x = (columns[bit] * 2 + 1) * width / 4;
               int y = (rows[bit] * 2 + 1) * height / 8;
               graphics.fillRect(x, y, w, h);
            }
         }
      }

      private void triangle(int side, boolean inverse) {
         Shape shape = switch (side) {
            case 0 -> polygon(0, 0, .5, .5, 0, 1);
            case 1 -> polygon(0, 0, 1, 0, .5, .5);
            case 2 -> polygon(1, 0, 1, 1, .5, .5);
            default -> polygon(0, 1, .5, .5, 1, 1);
         };
         if (inverse) {
            Area area = new Area(new Rectangle2D.Double(0, 0, width, height));
            area.subtract(new Area(shape));
            shape = area;
         }
         fill(shape);
      }

      private void mosaic(int corner, double ax, double ay, double bx, double by) {
         // Clip the cell rectangle to the side of the diagonal containing the named corner.
         double[][] corners = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
         double[] chosen = corners[corner];
         double sign = Math.signum((bx - ax) * (chosen[1] - ay) - (by - ay) * (chosen[0] - ax));
         double[][] square = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
         Path2D path = new Path2D.Double();
         boolean first = true;
         for (int i = 0; i < 4; i++) {
            double[] p = square[i];
            double[] q = square[(i + 1) % 4];
            double dp = sign * ((bx - ax) * (p[1] - ay) - (by - ay) * (p[0] - ax));
            double dq = sign * ((bx - ax) * (q[1] - ay) - (by - ay) * (q[0] - ax));
            if (dp >= 0) {
               if (first) path.moveTo(p[0] * width, p[1] * height);
               else path.lineTo(p[0] * width, p[1] * height);
               first = false;
            }
            if ((dp < 0) != (dq < 0)) {
               double t = dp / (dp - dq);
               double x = (p[0] + t * (q[0] - p[0])) * width;
               double y = (p[1] + t * (q[1] - p[1])) * height;
               if (first) path.moveTo(x, y);
               else path.lineTo(x, y);
               first = false;
            }
         }
         path.closePath();
         fill(path);
      }

      private void circle(int style, double start, double end) {
         double diameter = Math.max(1, Math.min(width, height) - 2.0 * light);
         double x = (width - diameter) / 2;
         double y = (height - diameter) / 2;
         if (style == 1) {
            fill(new Ellipse2D.Double(x, y, diameter, diameter));
         } else {
            stroke(new Arc2D.Double(x, y, diameter, diameter, -start, start - end, Arc2D.OPEN), light);
            if (style == 2) {
               double inner = diameter * .45;
               fill(new Ellipse2D.Double((width - inner) / 2, (height - inner) / 2, inner, inner));
            }
         }
      }

      private void halfCircle(int side, boolean filled, boolean justified) {
         double x = side == 2 ? -width : 0;
         double y = side == 3 ? -height : 0;
         double w = side % 2 == 0 ? 2.0 * width : width;
         double h = side % 2 != 0 ? 2.0 * height : height;
         if (justified) {
            double radius = Math.min(width / (side % 2 == 0 ? 1.0 : 2.0), height / (side % 2 != 0 ? 1.0 : 2.0));
            w = h = radius * 2;
            x = side == 0 ? width - radius : side == 2 ? -radius : (width - w) / 2;
            y = side == 1 ? height - radius : side == 3 ? -radius : (height - h) / 2;
         }
         Shape ellipse = new Ellipse2D.Double(x, y, w, h);
         if (filled) fill(ellipse);
         else stroke(ellipse, light);
      }

      private void quarterCircle(int corner) {
         double radius = Math.min(width, height);
         double cx = corner % 2 == 0 ? 0 : width;
         double cy = corner < 2 ? 0 : height;
         fill(new Ellipse2D.Double(cx - radius, cy - radius, 2 * radius, 2 * radius));
      }

      private void circleTile(int position) {
         int row = position / 4;
         int col = position % 4;
         boolean quarter = (row == 1 || row == 2) && (col == 1 || col == 2);
         int cells = quarter ? 2 : 4;
         int x = quarter ? col - 1 : col;
         int y = quarter ? row - 1 : row;
         stroke(new Ellipse2D.Double(-x * width + light / 2.0, -y * height + light / 2.0,
            cells * width - light, cells * height - light), light);
      }

      private void separator(int kind) {
         boolean right = kind < 4 ? kind < 2 : kind == 5;
         double edge = right ? 0 : 1;
         double point = 1 - edge;
         if (kind == 1 || kind == 3) {
            line(1, edge, 0, point, .5, edge, 1);
         } else {
            Area area = new Area(polygon(edge, 0, point, .5, edge, 1));
            if (kind >= 4) {
               Area inverse = new Area(new Rectangle2D.Double(0, 0, width, height));
               inverse.subtract(area);
               area = inverse;
            }
            fill(area);
         }
      }

      private void progress(int segment, boolean filled) {
         antialias(false);
         graphics.fillRect(0, 0, width, light);
         graphics.fillRect(0, Math.max(0, height - light), width, light);
         if (segment == 0) graphics.fillRect(0, 0, light, height);
         if (segment == 2) graphics.fillRect(Math.max(0, width - light), 0, light, height);
         if (filled) {
            int left = segment == 0 ? 3 * light : 0;
            int right = segment == 2 ? width - 3 * light : width;
            graphics.fillRect(left, 3 * light, Math.max(0, right - left), Math.max(0, height - 6 * light));
         }
      }

      private void commit(int edges, boolean solid) {
         double radius = Math.max(.5, Math.min(width, height) * .3);
         double cx = width / 2.0;
         double cy = height / 2.0;
         if ((edges & 1) != 0) line(1, .5, 0, .5, (cy - radius) / height);
         if ((edges & 2) != 0) line(1, (cx + radius) / width, .5, 1, .5);
         if ((edges & 4) != 0) line(1, .5, (cy + radius) / height, .5, 1);
         if ((edges & 8) != 0) line(1, 0, .5, (cx - radius) / width, .5);
         Shape node = new Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2);
         if (solid) fill(node);
         else stroke(node, light);
      }

      private void fade(int direction) {
         Color color = graphics.getColor();
         boolean vertical = direction >= 2;
         int length = vertical ? height : width;
         try {
            antialias(false);
            for (int i = 0; i < length; i++) {
               double alpha = (i + .5) / length;
               if (direction % 2 == 0) alpha = 1 - alpha;
               graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)Math.round(color.getAlpha() * alpha)));
               if (vertical) graphics.fillRect(width / 2 - light / 2, i, light, 1);
               else graphics.fillRect(i, height / 2 - light / 2, 1, light);
            }
         } finally {
            graphics.setColor(color);
         }
      }

      private void shade(int codePoint) {
         Shape clip = graphics.getClip();
         try {
            if (codePoint >= 0x1FB9C && codePoint <= 0x1FB9F) {
               double[][] corners = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
               int i = codePoint - 0x1FB9C;
               double[] a = corners[i];
               double[] b = corners[(i + 1) % 4];
               double[] c = corners[(i + 3) % 4];
               graphics.clip(polygon(a[0], a[1], b[0], b[1], c[0], c[1]));
            }
            boolean inverse = codePoint == 0x1FB90 || codePoint >= 0x1FB91 && codePoint <= 0x1FB94 || codePoint == 0x1FB96;
            if (codePoint >= 0x1FB8C && codePoint <= 0x1FB8F) {
               double[][] halves = {{0, 0, .5, 1}, {.5, 0, 1, 1}, {0, 0, 1, .5}, {0, .5, 1, 1}};
               double[] half = halves[codePoint - 0x1FB8C];
               graphics.clip(new Rectangle2D.Double(half[0] * width, half[1] * height, (half[2] - half[0]) * width, (half[3] - half[1]) * height));
            }
            if (codePoint >= 0x1FB91 && codePoint <= 0x1FB94) {
               switch (codePoint) {
                  case 0x1FB91 -> { rect(0, 0, 1, .5); graphics.clipRect(0, height / 2, width, height - height / 2); }
                  case 0x1FB92 -> { rect(0, .5, 1, 1); graphics.clipRect(0, 0, width, height / 2); }
                  case 0x1FB93 -> { rect(0, 0, .5, 1); graphics.clipRect(width / 2, 0, width - width / 2, height); }
                  case 0x1FB94 -> { rect(.5, 0, 1, 1); graphics.clipRect(0, 0, width / 2, height); }
               }
            }
            int sx = Math.max(1, width / (codePoint == 0x1FB95 || codePoint == 0x1FB96 ? 4 : 12));
            int sy = codePoint == 0x1FB95 || codePoint == 0x1FB96 || codePoint == 0x1FB97 ? Math.max(1, height / 4) : sx;
            antialias(false);
            for (int y = 0, row = 0; y < height; y += sy, row++) {
               for (int x = 0, col = 0; x < width; x += sx, col++) {
                  boolean paint = (row + col) % 2 == 0;
                  if (codePoint == 0x2591) paint = row % 2 == 0 && col % 2 == 0;
                  if (codePoint == 0x2593) paint = !(row % 2 == 0 && col % 2 == 0);
                  if (codePoint == 0x1FB97) paint = row % 2 == 0;
                  if (paint != inverse) graphics.fillRect(x, y, codePoint == 0x1FB97 ? width : sx, sy);
                  if (codePoint == 0x1FB97) break;
               }
            }
         } finally {
            graphics.setClip(clip);
         }
      }

      private void hatch(boolean reverse) {
         int spacing = Math.max(2, width / 4);
         for (int x = -height; x <= width + height; x += spacing) {
            line(1, (double)x / width, reverse ? 1 : 0, (double)(x + height) / width, reverse ? 0 : 1);
         }
      }

      private void strip(int index) {
         double[][] starts = {{.5, 1}, {.25, 1}, {0, .75}, {0, .5}};
         double x0 = starts[index % 4][0];
         double x1 = starts[index % 4][1];
         double y0 = .75;
         double y1 = 1;
         for (int turn = 0; turn < index / 4; turn++) {
            double nx0 = 1 - y1;
            double nx1 = 1 - y0;
            y0 = x0;
            y1 = x1;
            x0 = nx0;
            x1 = nx1;
         }
         rect(x0, y0, x1, y1);
      }
   }
}
