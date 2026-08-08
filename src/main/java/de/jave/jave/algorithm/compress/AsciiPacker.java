package de.jave.jave.algorithm.compress;

import de.jave.lib.CharacterPlate;
import de.jave.lib.cell.Cell;
import de.jave.lib.cell.GlyphEncoding;

public class AsciiPacker {
   private AsciiPacker() {
   }

   public static String encodeOptimized(CharacterPlate ch) {
      return encodeOptimized(ch.glyphPlane());
   }

   public static String encode(CharacterPlate ch) {
      return encode(ch.glyphPlane());
   }

   public static String encodeOptimized(int[][] ch) {
      int h = ch.length;
      if (h == 0) {
         return "B0 0";
      } else {
         int w = ch[0].length;
         if (w == 0) {
            return "B0 " + h;
         } else if (requiresC(ch)) {
            return encodeC(ch);
         } else {
            String code = encodeA(ch);
            String prefix = "A";
            String codeB = encodeB(ch);
            if (codeB.length() < code.length()) {
               prefix = "B";
               code = codeB;
            }

            return prefix + w + " " + h + " " + code;
         }
      }
   }

   private static String encodeB(int[][] ch) {
      StringBuffer sb = new StringBuffer();
      encodeB(sb, ch);
      return sb.toString();
   }

   private static String encodeA(int[][] ch) {
      StringBuffer sb = new StringBuffer();
      encodeA(sb, ch);
      return sb.toString();
   }

   public static String encode(int[][] ch) {
      int h = ch.length;
      if (h == 0) {
         return "B0 0";
      } else {
         int w = ch[0].length;
         if (w == 0) {
            return "B0 " + h;
         } else if (requiresC(ch)) {
            return encodeC(ch);
         } else {
            StringBuffer sb = new StringBuffer(w * h + 10);
            if (w * h < 41) {
               sb.append('B');
               sb.append(w);
               sb.append(' ');
               sb.append(h);
               sb.append(' ');
               encodeB(sb, ch);
            } else {
               sb.append('A');
               sb.append(w);
               sb.append(' ');
               sb.append(h);
               sb.append(' ');
               encodeA(sb, ch);
            }

            return sb.toString();
         }
      }
   }

   public static final int[][] decode(String code) {
      if (code != null && code.length() >= 4) {
         char algorithm = code.charAt(0);
         switch (algorithm) {
            case 'A':
               return decodeA(code);
            case 'B':
               return decodeB(code);
            case 'C':
               return decodeC(code);
            default:
               throw new RuntimeException(
                  "Unable to decode compressed ASCII: Algorithm '" + algorithm + "' not known by this program version! " + "Please download a new release."
               );
         }
      } else {
         throw new RuntimeException("Unable to decode compressed ASCII:The compressed code doesn not seem to be valid!");
      }
   }

   private static final int[][] decodeA(String code) {
      int length = code.length();
      int index = 1;
      int w = 0;

      for (char ch = code.charAt(index++); ch >= '0' && ch <= '9'; ch = code.charAt(index++)) {
         w *= 10;
         w += ch - '0';
      }

      int h = 0;

      for (char var14 = code.charAt(index++); var14 >= '0' && var14 <= '9'; var14 = code.charAt(index++)) {
         h *= 10;
         h += var14 - '0';
      }

      int[][] chars = new int[h][w];
      int x = 0;
      int y = 0;

      while (index < length) {
         char var15 = code.charAt(index++);
         if (var15 != '%') {
            chars[y][x++] = var15;
         } else {
            var15 = code.charAt(index++);
            if (var15 == '%') {
               chars[y][x++] = '%';
            } else if (var15 == '0') {
               for (int i = x; i < w; i++) {
                  chars[y][x++] = ' ';
               }

               y++;
               x = 0;
            } else {
               int count = var15 - '0';

               for (var15 = code.charAt(index++); var15 >= '0' && var15 <= '9'; var15 = code.charAt(index++)) {
                  count *= 10;
                  count += var15 - '0';
               }

               if (var15 == '%') {
                  var15 = code.charAt(index++);
               }

               for (int i = 0; i < count; i++) {
                  chars[y][x++] = var15;
               }
            }
         }
      }

      while (x < w) {
         chars[y][x++] = ' ';
      }

      for (int yy = y + 1; yy < h; yy++) {
         for (int xx = 0; xx < w; xx++) {
            chars[yy][xx] = ' ';
         }
      }

      return chars;
   }

   private static final int[][] decodeB(String code) {
      int index = 1;
      int w = 0;

      while (index < code.length()) {
         char ch = code.charAt(index++);
         if (ch < '0' || ch > '9') {
            break;
         }
         w *= 10;
         w += ch - '0';
      }

      int h = 0;

      while (index < code.length()) {
         char ch = code.charAt(index++);
         if (ch < '0' || ch > '9') {
            break;
         }
         h *= 10;
         h += ch - '0';
      }

      int[][] chars = new int[h][w];
      int i = index;
      int l = code.length();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (i >= l) {
               chars[y][x] = ' ';
            } else {
               chars[y][x] = code.charAt(i);
            }

            i++;
         }
      }

      return chars;
   }

   private static void encodeA(StringBuffer sc, int[][] ch) {
      int w = ch[0].length;
      if (w != 0) {
         int h = ch.length;
         if (h != 0) {
            int state = 1;
            int x = 1;
            int y = 0;
            int cc = ch[0][0];
            int count = 1;
            int eolCount = 0;

            while (state != -1) {
               switch (state) {
                  case 1:
                     if (y >= h) {
                        if (cc != ' ') {
                           printA(sc, cc, count);
                        }

                        state = -1;
                     } else if (x >= w) {
                        if (cc != ' ') {
                           printA(sc, cc, count);
                        }

                        eolCount = 1;
                        state = 2;
                        x = 0;
                        y++;
                     } else if (ch[y][x] == cc) {
                        count++;
                        x++;
                     } else {
                        printA(sc, cc, count);
                        cc = ch[y][x];
                        x++;
                        count = 1;
                     }
                     break;
                  case 2:
                     if (y >= h) {
                        state = -1;
                     } else if (x >= w) {
                        eolCount++;
                        x = 0;
                        y++;
                     } else if (ch[y][x] == ' ') {
                        count = 1;
                        cc = ch[y][x];
                        x++;
                        state = 3;
                     } else {
                        printACR(sc, eolCount);
                        cc = ch[y][x];
                        x++;
                        count = 1;
                        state = 1;
                     }
                     break;
                  case 3:
                     if (y >= h) {
                        state = -1;
                     } else if (x >= w) {
                        eolCount++;
                        x = 0;
                        y++;
                        state = 2;
                     } else if (ch[y][x] == ' ') {
                        count++;
                        x++;
                     } else {
                        printACR(sc, eolCount);
                        printA(sc, ' ', count);
                        cc = ch[y][x];
                        x++;
                        count = 1;
                        state = 1;
                     }
               }
            }
         }
      }
   }

   private static final void printACR(StringBuffer sb, int count) {
      for (int i = 0; i < count; i++) {
         sb.append('%');
         sb.append('0');
      }
   }

   private static final void printA(StringBuffer sb, int ch, int count) {
      if (count > 2) {
         sb.append('%');
         sb.append(count);
         if (ch >= '0' && ch <= '9') {
            sb.append('%');
            sb.append((char)ch);
         } else if (ch == '%') {
            sb.append('%');
            sb.append('%');
         } else {
            sb.append((char)ch);
         }
      } else if (count == 2) {
         if (ch == '%') {
            sb.append('%');
            sb.append('%');
            sb.append('%');
            sb.append('%');
         } else {
            sb.append((char)ch);
            sb.append((char)ch);
         }
      } else if (ch == '%') {
         sb.append('%');
         sb.append('%');
      } else {
         sb.append((char)ch);
      }
   }

   private static void encodeB(StringBuffer sc, int[][] chars) {
      int h = chars.length;

      for (int y = 0; y < h; y++) {
         for (int glyph : chars[y]) {
            sc.append((char)glyph);
         }
      }

      int size = sc.length();

      while (size > 0 && sc.charAt(size - 1) == ' ') {
         size--;
      }

      sc.setLength(size);
   }

   private static boolean requiresC(int[][] glyphs) {
      for (int[] row : glyphs) {
         for (int glyph : row) {
            if (!GlyphEncoding.isCodePoint(glyph) && !GlyphEncoding.isCluster(glyph)) {
               throw new IllegalArgumentException("Invalid glyph encoding: " + glyph);
            }
            if (GlyphEncoding.isCluster(glyph) || glyph > Character.MAX_VALUE) {
               return true;
            }
         }
      }
      return false;
   }

   private static String encodeC(int[][] glyphs) {
      int height = glyphs.length;
      int width = height == 0 ? 0 : glyphs[0].length;
      StringBuffer result = new StringBuffer(width * height + 10);
      result.append('C').append(width).append(' ').append(height).append(' ');
      for (int[] row : glyphs) {
         for (int glyph : row) {
            if (GlyphEncoding.isCluster(glyph)) {
               String text = new Cell(glyph).text();
               result.append("%{").append(text.length()).append(':').append(text).append('}');
            } else if (glyph == '%') {
               result.append("%%");
            } else if (GlyphEncoding.isCodePoint(glyph)) {
               result.appendCodePoint(glyph);
            } else {
               throw new IllegalArgumentException("Invalid glyph encoding: " + glyph);
            }
         }
      }
      return result.toString();
   }

   private static int[][] decodeC(String code) {
      Header header = parseHeader(code);
      int[][] glyphs = new int[header.height()][header.width()];
      int payloadIndex = header.payloadIndex();
      for (int y = 0; y < header.height(); y++) {
         for (int x = 0; x < header.width(); x++) {
            if (payloadIndex >= code.length()) {
               glyphs[y][x] = ' ';
               continue;
            }

            int codePoint = code.codePointAt(payloadIndex);
            if (codePoint != '%') {
               glyphs[y][x] = codePoint;
               payloadIndex += Character.charCount(codePoint);
               continue;
            }

            payloadIndex++;
            if (payloadIndex >= code.length()) {
               throw new IllegalArgumentException("Truncated C payload escape");
            }
            char escape = code.charAt(payloadIndex++);
            if (escape == '%') {
               glyphs[y][x] = '%';
               continue;
            }
            if (escape != '{') {
               throw new IllegalArgumentException("Unknown C payload escape: " + escape);
            }

            int separator = code.indexOf(':', payloadIndex);
            if (separator < 0) {
               throw new IllegalArgumentException("Cluster length is missing from C payload");
            }
            int length = Integer.parseInt(code.substring(payloadIndex, separator));
            int textStart = separator + 1;
            int textEnd = textStart + length;
            if (textEnd >= code.length() || code.charAt(textEnd) != '}') {
               throw new IllegalArgumentException("Truncated cluster in C payload");
            }
            glyphs[y][x] = Cell.fromText(code.substring(textStart, textEnd)).glyph();
            payloadIndex = textEnd + 1;
         }
      }
      return glyphs;
   }

   private static Header parseHeader(String code) {
      int index = 1;
      int widthEnd = code.indexOf(' ', index);
      int heightEnd = widthEnd < 0 ? -1 : code.indexOf(' ', widthEnd + 1);
      if (widthEnd < 0 || heightEnd < 0) {
         throw new IllegalArgumentException("Invalid packed-content header");
      }
      int width = Integer.parseInt(code.substring(index, widthEnd));
      int height = Integer.parseInt(code.substring(widthEnd + 1, heightEnd));
      return new Header(width, height, heightEnd + 1);
   }

   private record Header(int width, int height, int payloadIndex) {
   }
}
