package de.jave.jave.algorithm.compress;

import de.jave.lib.CharacterPlate;

public class AsciiPacker {
   private AsciiPacker() {
   }

   public static String encodeOptimized(CharacterPlate ch) {
      return encodeOptimized(ch.getContent());
   }

   public static String encode(CharacterPlate ch) {
      return encode(ch.getContent());
   }

   public static String encodeOptimized(char[][] ch) {
      int h = ch.length;
      if (h == 0) {
         return "B0 0";
      } else {
         int w = ch[0].length;
         if (w == 0) {
            return "B0 " + h;
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

   private static String encodeB(char[][] ch) {
      StringBuffer sb = new StringBuffer();
      encodeB(sb, ch);
      return sb.toString();
   }

   private static String encodeA(char[][] ch) {
      StringBuffer sb = new StringBuffer();
      encodeA(sb, ch);
      return sb.toString();
   }

   public static String encode(char[][] ch) {
      int h = ch.length;
      if (h == 0) {
         return "B0 0";
      } else {
         int w = ch[0].length;
         if (w == 0) {
            return "B0 " + h;
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

   public static final char[][] decode(String code) {
      if (code != null && code.length() >= 4) {
         char algorithm = code.charAt(0);
         switch (algorithm) {
            case 'A':
               return decodeA(code);
            case 'B':
               return decodeB(code);
            default:
               throw new RuntimeException(
                  "Unable to decode compressed ASCII: Algorithm '" + algorithm + "' not known by this program version! " + "Please download a new release."
               );
         }
      } else {
         throw new RuntimeException("Unable to decode compressed ASCII:The compressed code doesn not seem to be valid!");
      }
   }

   private static final char[][] decodeA(String code) {
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

      char[][] chars = new char[h][w];
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

   private static final char[][] decodeB(String code) {
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

      char[][] chars = new char[h][w];
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

   private static void encodeA(StringBuffer sc, char[][] ch) {
      int w = ch[0].length;
      if (w != 0) {
         int h = ch.length;
         if (h != 0) {
            int state = 1;
            int x = 1;
            int y = 0;
            char cc = ch[0][0];
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

   private static final void printA(StringBuffer sb, char ch, int count) {
      if (count > 2) {
         sb.append('%');
         sb.append(count);
         if (ch >= '0' && ch <= '9') {
            sb.append('%');
            sb.append(ch);
         } else if (ch == '%') {
            sb.append('%');
            sb.append('%');
         } else {
            sb.append(ch);
         }
      } else if (count == 2) {
         if (ch == '%') {
            sb.append('%');
            sb.append('%');
            sb.append('%');
            sb.append('%');
         } else {
            sb.append(ch);
            sb.append(ch);
         }
      } else if (ch == '%') {
         sb.append('%');
         sb.append('%');
      } else {
         sb.append(ch);
      }
   }

   private static void encodeB(StringBuffer sc, char[][] chars) {
      int h = chars.length;

      for (int y = 0; y < h; y++) {
         sc.append(chars[y]);
      }

      int size = sc.length();

      while (size > 0 && sc.charAt(size - 1) == ' ') {
         size--;
      }

      sc.setLength(size);
   }
}
