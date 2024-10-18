package de.jave.javeplayer;

import java.awt.Color;

public class JavePlayerUtilities {
   private JavePlayerUtilities() {
   }

   public static final String colorToHex(Color color) {
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

      return "#" + red + green + blue;
   }

   public static Color hexToColor(String color) {
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

   private static final int hexToInt(char ch) {
      if (ch >= '0' && ch <= '9') {
         return ch - 48;
      } else if (ch >= 'a' && ch <= 'f') {
         return 10 + ch - 97;
      } else {
         throw new RuntimeException("Wrong format for hexadecimal number: " + ch);
      }
   }
}
