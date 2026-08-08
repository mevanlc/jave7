package de.jave.text;

import de.jave.lib.cell.Cell;
import de.jave.lib.cell.GraphemeSplitter;
import java.awt.Dimension;
import java.text.Normalizer;
import java.util.List;
import java.util.StringTokenizer;

public class TextTools {
   private TextTools() {
   }

   public static final int count(String s, char ch) {
      int result = 0;

      for (int index = s.indexOf(ch); index != -1; index = s.indexOf(ch, index + 1)) {
         result++;
      }

      return result;
   }

   public static final String fillFront(String text, char fill, int length) {
       StringBuilder textBuilder = new StringBuilder(text);
       while (textBuilder.length() < length) {
         textBuilder.insert(0, fill);
      }
       text = textBuilder.toString();

       return text;
   }

   public static final String firstLetterUp(String text) {
      if (text != null && text.length() != 0) {
         char[] chars = text.toCharArray();
         chars[0] = Character.toUpperCase(chars[0]);

         for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == ' ') {
               chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
         }

         return new String(chars);
      } else {
         return text;
      }
   }

   public static final String[] toStringArray(int[][] glyphs) {
      int height = glyphs.length;
      String[] result = new String[height];

      for (int y = 0; y < height; y++) {
         result[y] = trimRight(toString(glyphs[y]));
      }

      return result;
   }

   public static String trimRight(String line) {
      int i0 = line.length() - 1;

      while (i0 >= 0 && line.charAt(i0) <= ' ') {
         i0--;
      }

      return line.substring(0, i0 + 1);
   }

   public static final String toString(int[][] glyphs) {
      int height = glyphs.length;
      int width = height == 0 ? 0 : glyphs[0].length;
      StringBuffer sb = new StringBuffer(height * width + height);

      for (int y = 0; y < height; y++) {
         sb.append(trimRight(toString(glyphs[y])));
         if (y < height - 1) {
            sb.append('\n');
         }
      }

      return sb.toString();
   }

   public static String toString(int[] glyphs) {
      StringBuilder result = new StringBuilder(glyphs.length);
      for (int glyph : glyphs) {
         result.append(new Cell(glyph).text());
      }
      return result.toString();
   }

   public static final Dimension getDimensionOf(String text) {
      String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
      String[] lines = normalized.split("\\n", -1);
      int h = lines.length;
      int w = 0;
      for (String line : lines) {
         w = Math.max(w, GraphemeSplitter.split(line).size());
      }
      return new Dimension(w, h);
   }

   public static final int[][] toCharField(String[] lines) {
      int h = lines.length;
      int w = 0;
      int[][] encodedLines = new int[h][];

      for (int y = 0; y < h; y++) {
         encodedLines[y] = toGlyphs(lines[y]);
         w = Math.max(w, encodedLines[y].length);
      }

      int[][] glyphs = new int[h][w];

      for (int y = 0; y < h; y++) {
         System.arraycopy(encodedLines[y], 0, glyphs[y], 0, encodedLines[y].length);
         for (int x = encodedLines[y].length; x < w; x++) {
            glyphs[y][x] = ' ';
         }
      }

      return glyphs;
   }

   public static int[] toGlyphs(String text) {
      String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
      List<String> graphemes = GraphemeSplitter.split(normalized);
      int[] glyphs = new int[graphemes.size()];
      for (int i = 0; i < graphemes.size(); i++) {
         glyphs[i] = Cell.fromText(graphemes.get(i)).glyph();
      }
      return glyphs;
   }

   public static final int[][] toCharField(String text) {
      String normalized = Normalizer.normalize(text, Normalizer.Form.NFC);
      int[][] glyphs = toCharField(normalized.split("\\n", -1));
      if (glyphs.length > 0 && glyphs[0].length == 0) {
         glyphs = new int[glyphs.length][1];
         for (int y = 0; y < glyphs.length; y++) {
            glyphs[y][0] = ' ';
         }
      }
      return glyphs;
   }

   public static final String[] toStringArray(String text) {
      return Normalizer.normalize(text, Normalizer.Form.NFC).split("\\n", -1);
   }

   public static String center(String s, int length) {
      int d = length - s.length();
      int a = d / 2;
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < a; i++) {
         sb.append(' ');
      }

      sb.append(s);

      for (int i = a; i < d; i++) {
         sb.append(' ');
      }

      return sb.toString();
   }

   public static void print(int[][] c) {
      for (int y = 0; y < c.length; y++) {
         for (int x = 0; x < c[y].length; x++) {
            System.out.print(c[y][x] + "\t");
         }

         System.out.println();
      }
   }

   public static void print(double[][] c) {
      for (int y = 0; y < c.length; y++) {
         for (int x = 0; x < c[y].length; x++) {
            System.out.print(c[y][x] + "\t");
         }

         System.out.println();
      }
   }

   public static int editDistance(String s1, String s2) {
      int n = s1.length();
      int m = s2.length();
      int[][] c = new int[n + 1][m + 1];
      int i = 0;

      while (i <= m) {
         c[0][i] = i++;
      }

      i = 0;

      while (i <= n) {
         c[i][0] = i++;
      }

      for (int ix = 1; ix <= n; ix++) {
         for (int j = 1; j <= m; j++) {
            i = c[ix - 1][j] + 1;
            int c2 = c[ix][j - 1] + 1;
            int c3 = c[ix - 1][j - 1];
            if (s1.charAt(ix - 1) != s2.charAt(j - 1)) {
               c3++;
            }

            c3 = c3 <= c2 ? c3 : c2;
            c[ix][j] = c3 <= i ? c3 : i;
         }
      }

      return c[n][m];
   }

   public static int editDistance(String s1, String s2, int max) {
      int n = s1.length();
      int m = s2.length();
      if (n - m <= max && m - n <= max) {
         int[][] c = new int[n + 1][m + 1];
         int i = 0;

         while (i <= m) {
            c[0][i] = i++;
         }

         i = 0;

         while (i <= n) {
            c[i][0] = i++;
         }

         for (int ix = 1; ix <= n; ix++) {
            for (int j = 1; j <= m; j++) {
               i = c[ix - 1][j] + 1;
               int c2 = c[ix][j - 1] + 1;
               int c3 = c[ix - 1][j - 1];
               if (s1.charAt(ix - 1) != s2.charAt(j - 1)) {
                  c3++;
               }

               c3 = c3 <= c2 ? c3 : c2;
               c[ix][j] = c3 <= i ? c3 : i;
            }
         }

         return c[n][m];
      } else {
         return max;
      }
   }

   public static int editDistanceGebhard(String s1, String s2) {
      int n = s1.length();
      int m = s2.length();
      int[][] c = new int[n + 1][m + 1];
      int i = 0;

      while (i <= m) {
         c[0][i] = i++;
      }

      i = 0;

      while (i <= n) {
         c[i][0] = i++;
      }

      for (int ix = 1; ix <= n; ix++) {
         for (int j = 1; j <= m; j++) {
            i = c[ix - 1][j] + 1;
            int c2 = c[ix][j - 1] + 1;
            int c3 = c[ix - 1][j - 1];
            if (s1.charAt(ix - 1) != s2.charAt(j - 1)) {
               if (Character.toLowerCase(s1.charAt(ix - 1)) == Character.toLowerCase(s2.charAt(j - 1))) {
                  c3++;
               } else {
                  c3 += 2;
               }
            }

            c3 = c3 <= c2 ? c3 : c2;
            c[ix][j] = c3 <= i ? c3 : i;
         }
      }

      return c[n][m];
   }

   public static double levenshteinDistance(String s1, String s2, double wInsert, double wDelete, double wCase, double wSubst) {
      int n = s1.length();
      int m = s2.length();
      double[][] c = new double[n + 1][m + 1];

      for (int i = 0; i <= m; i++) {
         c[0][i] = (double)i * wInsert;
      }

      for (int i = 0; i <= n; i++) {
         c[i][0] = (double)i * wDelete;
      }

      for (int i = 1; i <= n; i++) {
         for (int j = 1; j <= m; j++) {
            double c1 = c[i - 1][j] + wDelete;
            double c2 = c[i][j - 1] + wInsert;
            double c3 = c[i - 1][j - 1];
            char ch1 = s1.charAt(i - 1);
            char ch2 = s2.charAt(j - 1);
            if (ch1 != ch2) {
               if (Character.toUpperCase(ch1) == Character.toUpperCase(ch2)) {
                  c3 += wCase;
               } else {
                  c3 += wSubst;
               }
            }

            c3 = c3 <= c2 ? c3 : c2;
            c[i][j] = c3 <= c1 ? c3 : c1;
         }
      }

      print(c);
      return c[n][m];
   }

   public static String trim(String s) {
      if (s.length() == 0) {
         return "";
      } else {
         StringBuffer s2 = new StringBuffer(s.length());
         StringTokenizer st = new StringTokenizer(s, "\n\r", true);

         while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (!t.equals("\n") && !t.equals("\r")) {
               int i0 = t.length() - 1;

               while (i0 >= 0 && t.charAt(i0) <= ' ') {
                  i0--;
               }

               t = t.substring(0, i0 + 1);
            }

            s2.append(t);
         }

         s = s2.toString();
         int i0 = 0;
         char ch = s.charAt(0);

         while (i0 < s.length() - 1 && (ch == '\n' || ch == '\r')) {
            ch = s.charAt(++i0);
         }

         int i1 = s.length() - 1;
         ch = s.charAt(i1);

         while (i1 > i0 && (ch == '\n' || ch == '\r')) {
            ch = s.charAt(--i1);
         }

         return s.substring(i0, i1 + 1);
      }
   }
}
