package de.jave.lib;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class Toolbox {
   private static Random rand;

   private Toolbox() {
   }

   public static String getStackTrace(Throwable th) {
      StringWriter strout = new StringWriter();
      PrintWriter prnout = new PrintWriter(strout);
      th.printStackTrace(prnout);
      prnout.flush();
      return strout.toString();
   }

   public static final URL toURL(File file) throws MalformedURLException {
      String path = file.getAbsolutePath();
      if (File.separatorChar != '/') {
         path = path.replace(File.separatorChar, '/');
      }

      if (!path.startsWith("/")) {
         path = "/" + path;
      }

      if (!path.endsWith("/") && file.isDirectory()) {
         path = path + "/";
      }

      return new URL("file", "", path);
   }

   public static final String toJavaString(String s) {
      StringBuffer sb = new StringBuffer("\"");

      for (int i = 0; i < s.length(); i++) {
         char ch = s.charAt(i);
         if (ch == '"') {
            sb.append("\\\"");
         } else if (ch == '\\') {
            sb.append("\\\\");
         } else {
            sb.append(ch);
         }
      }

      sb.append('"');
      return sb.toString();
   }

   public static final String getDateString() {
      Calendar rightNow = Calendar.getInstance();
      int sec = rightNow.get(13);
      int min = rightNow.get(12);
      String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
      String minStr = min < 10 ? "0" + min : String.valueOf(min);
      return rightNow.get(1) + "/" + (rightNow.get(2) + 1) + "/" + rightNow.get(5) + " " + rightNow.get(11) + ":" + minStr + ":" + secStr;
   }

   public static String colorToHexString(Color color) {
      String red = Integer.toHexString(color.getRed());
      String green = Integer.toHexString(color.getGreen());
      String blue = Integer.toHexString(color.getBlue());
      if (red.length() == 1) {
         red = "0" + red;
      }

      if (green.length() == 1) {
         green = "0" + green;
      }

      if (blue.length() == 1) {
         blue = "0" + blue;
      }

      return red + green + blue;
   }

   public static Color hexStringToColor(String color) {
      if (color.length() == 6) {
         color = color.toLowerCase();
      } else {
         if (color.length() != 7) {
            throw new RuntimeException("Wrong format for color: " + color);
         }

         color = color.substring(1).toLowerCase();
      }

      int red = 16 * hexToInt(color.charAt(0)) + hexToInt(color.charAt(1));
      int green = 16 * hexToInt(color.charAt(2)) + hexToInt(color.charAt(3));
      int blue = 16 * hexToInt(color.charAt(4)) + hexToInt(color.charAt(5));
      return new Color(red, green, blue);
   }

   protected static final int hexToInt(char ch) {
      if (ch >= '0' && ch <= '9') {
         return ch - 48;
      } else if (ch >= 'a' && ch <= 'f') {
         return 10 + ch - 97;
      } else {
         throw new RuntimeException("Wrong format for hexadecimal number: " + ch);
      }
   }

   public static double round(double x, int length) {
      int mult = (int)Math.pow(10.0, length);
      return Math.rint(x * (double)mult) / (double)mult;
   }

   public static Color toColor(int value, int min, int max) {
      if (value > max) {
         value = max;
      }

      value = max - value;
      double hue = ((double)value - (double)min) / ((double)max - (double)min) * 2.0 / 3.0;
      return new Color(Color.HSBtoRGB((float)hue, 1.0F, 0.7F));
   }

   public static int random(int min, int max) {
      if (rand == null) {
         rand = new Random();
      }

      return min + Math.abs(rand.nextInt()) % (max - min + 1);
   }

   public static double random() {
      if (rand == null) {
         rand = new Random();
      }

      return rand.nextDouble();
   }

   public static String randomString() {
      return randomString(2000);
   }

   public static String randomString(int maxLength) {
      if (rand == null) {
         rand = new Random();
      }

      int length = Math.abs(rand.nextInt() % maxLength);
      char[] b = new char[length];

      for (int i = 0; i < b.length; i++) {
         int r = Math.abs(rand.nextInt()) % 38;
         if (r >= 26) {
            b[i] = ' ';
         } else {
            b[i] = (char)(r + 65);
         }
      }

      return new String(b);
   }

   public static void sortStringsBubbleSort(String[] strings) {
      for (int i = 0; i < strings.length - 1; i++) {
         for (int j = i + 1; j < strings.length; j++) {
            if (strings[i].compareTo(strings[j]) > 0) {
               String t = strings[i];
               strings[i] = strings[j];
               strings[j] = t;
            }
         }
      }
   }

   public static String fill(String source, char fill, int length) {
      if (source.length() >= length) {
         return source;
      } else {
         int f = length - source.length();

          StringBuilder sourceBuilder = new StringBuilder(source);
          for (int i = 0; i < f; i++) {
            sourceBuilder.append(fill);
         }
          source = sourceBuilder.toString();

          return source;
      }
   }

   public static String fill(int source, char fill, int length) {
      String s = String.valueOf(source);
      if (s.length() >= length) {
         return s;
      } else {
         int f = length - s.length();

          StringBuilder sBuilder = new StringBuilder(s);
          for (int i = 0; i < f; i++) {
            sBuilder.insert(0, fill);
         }
          s = sBuilder.toString();

          return s;
      }
   }

   public static String fill(double source, char fill, int length) {
      String s = String.valueOf(source);
      if (s.length() >= length) {
         return s;
      } else {
         int f = length - s.length();

          StringBuilder sBuilder = new StringBuilder(s);
          for (int i = 0; i < f; i++) {
            sBuilder.insert(0, fill);
         }
          s = sBuilder.toString();

          return s;
      }
   }

   public static String format(double num, int decimals, boolean plusMinus, char fill, int length) {
      String result = String.valueOf(round(num, decimals));
      if (plusMinus && num > 0.0) {
         result = "+" + result;
      }

      if (result.length() >= length) {
         return result;
      } else {
         int f = length - result.length();

          StringBuilder resultBuilder = new StringBuilder(result);
          for (int i = 0; i < f; i++) {
            resultBuilder.insert(0, fill);
         }
          result = resultBuilder.toString();

          return result;
      }
   }

   public static int nextPrim(int start) {
      while (!isPrim(start)) {
         start++;
      }

      return start;
   }

   public static boolean isPrim(int number) {
      if (number < 2) {
         return false;
      } else {
         int max = (int)Math.sqrt(number);
         int c = 1;

         while (c++ < max) {
            if ((double)number / (double)c == (double)(number / c)) {
               return false;
            }
         }

         return true;
      }
   }

   public static String byteSizeToString(long size) {
      String result = "";
      int GB = (int)(size / 1073741824L);
      if (GB > 0) {
         result = "" + GB;
         if (GB < 10) {
            int i = (int)((double)size / 1.0737418E9F * 100.0) % 100;
            if (i != 0) {
               if (i < 10) {
                  result = result + ".0" + i;
               } else {
                  result = result + "." + i;
               }
            }
         } else if (GB < 100) {
            result = result + "." + (int)((double)size / 1.0737418E9F * 10.0) % 10;
         }

         return result + " GByte";
      } else {
         int MB = (int)(size / 1048576L);
         if (MB > 0) {
            result = "" + MB;
            if (MB < 10) {
               int i = (int)((double)size / 1048576.0 * 100.0) % 100;
               if (i != 0) {
                  if (i < 10) {
                     result = result + ".0" + i;
                  } else {
                     result = result + "." + i;
                  }
               }
            } else if (MB < 100) {
               result = result + "." + (int)((double)size / 1048576.0 * 10.0) % 10;
            }

            return result + " MByte";
         } else {
            int KB = (int)(size / 1024L);
            if (KB > 0) {
               result = "" + KB;
               if (KB < 10) {
                  int i = (int)((double)size / 1024.0 * 100.0) % 100;
                  if (i != 0) {
                     if (i < 10) {
                        result = result + ".0" + i;
                     } else {
                        result = result + "." + i;
                     }
                  }
               } else if (KB < 100) {
                  result = result + "." + (int)((double)size / 1024.0 * 10.0) % 10;
               }

               return result + " kByte";
            } else {
               return size + " Byte";
            }
         }
      }
   }

   public static String urlDecoder(String encoded) {
      StringBuffer decoded = new StringBuffer();
      int len = encoded.length();

      for (int i = 0; i < len; i++) {
         if (encoded.charAt(i) == '%' && i + 2 < len) {
            int d1 = Character.digit(encoded.charAt(i + 1), 16);
            int d2 = Character.digit(encoded.charAt(i + 2), 16);
            if (d1 != -1 && d2 != -1) {
               decoded.append((char)((d1 << 4) + d2));
            }

            i += 2;
         } else if (encoded.charAt(i) == '+') {
            decoded.append(' ');
         } else {
            decoded.append(encoded.charAt(i));
         }
      }

      return decoded.toString();
   }

   public static String toUU(String s) {
      return s.replace(' ', '+');
   }

   private static void quickSort(int[] a, int lo0, int hi0) {
      int lo = lo0;
      int hi = hi0;
      if (hi0 > lo0) {
         int mid = a[(lo0 + hi0) / 2];

         while (lo <= hi) {
            while (lo < hi0 && a[lo] < mid) {
               lo++;
            }

            while (hi > lo0 && a[hi] > mid) {
               hi--;
            }

            if (lo <= hi) {
               swap(a, lo, hi);
               lo++;
               hi--;
            }
         }

         if (lo0 < hi) {
            quickSort(a, lo0, hi);
         }

         if (lo < hi0) {
            quickSort(a, lo, hi0);
         }
      }
   }

   private static void swap(int[] a, int i, int j) {
      int t = a[i];
      a[i] = a[j];
      a[j] = t;
   }

   public static void quickSort(int[] a) {
      quickSort(a, 0, a.length - 1);
   }

   public static int compareToIgnoreCase(String s1, String s2) {
      int n1 = s1.length();
      int n2 = s2.length();
      int i1 = 0;

      for (int i2 = 0; i1 < n1 && i2 < n2; i2++) {
         char c1 = s1.charAt(i1);
         char c2 = s2.charAt(i2);
         if (c1 != c2) {
            c1 = Character.toUpperCase(c1);
            c2 = Character.toUpperCase(c2);
            if (c1 != c2) {
               c1 = Character.toLowerCase(c1);
               c2 = Character.toLowerCase(c2);
               if (c1 != c2) {
                  return c1 - c2;
               }
            }
         }

         i1++;
      }

      return n1 - n2;
   }

   public static double roundSignificant(double x, int length) {
      if (length <= 0) {
         throw new IllegalArgumentException("Not a valid value for significant ciffers: " + length);
      } else if (x == 0.0) {
         return x;
      } else {
         long mult = 1L;
         if (x < 0.0) {
            mult *= -1L;
            x *= -1.0;
         }

         double f = Math.pow(10.0, length - 1);
         if (x < f) {
            while (x < f) {
               x *= 10.0;
               mult *= 10L;
            }

            return Math.rint(x) / (double)mult;
         } else {
            while (x >= f) {
               x /= 10.0;
               mult *= 10L;
            }

            x *= 10.0;
            mult /= 10L;
            return Math.rint(x) * (double)mult;
         }
      }
   }

   public static boolean bothNullOrEquals(Object value1, Object value2) {
      return value1 == null ? value2 == null : value1.equals(value2);
   }

   public static final int abs(int i) {
      return i < 0 ? -i : i;
   }

   public static final int min(int a, int b) {
      return a < b ? a : b;
   }

   public static final int max(int a, int b) {
      return a > b ? a : b;
   }

   public static final int max(int a, int b, int c) {
      return max(a, max(b, c));
   }

   public static final int max(int a, int b, int c, int d) {
      return max(max(a, b), max(c, d));
   }
}
