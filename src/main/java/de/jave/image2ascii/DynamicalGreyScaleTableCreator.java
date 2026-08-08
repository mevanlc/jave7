package de.jave.image2ascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import net.dizzy.commons.core.util.Ensure;

public class DynamicalGreyScaleTableCreator {
   private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(new AffineTransform(), false, false);

   public static AsciiGreyscaleTable createGreyscaleTable(Font font) {
      font = font.deriveFont(26.0F);
      Rectangle bounds = getMaximumAsciiRenderingBounds(font);
      double[] table = new double[95];
      double[] tableNW = new double[95];
      double[] tableNE = new double[95];
      double[] tableSW = new double[95];
      double[] tableSE = new double[95];
      BufferedImage image = new BufferedImage(bounds.width, bounds.height, 10);

      for (int i = 32; i < 127; i++) {
         renderCharacter(image, bounds, font, (char)i);
         table[i - 32] = sumUp(image, 0, bounds.width - 1, 0, bounds.height - 1);
         tableNW[i - 32] = sumUp(image, 0, bounds.width / 2, 0, bounds.height / 2);
         tableNE[i - 32] = sumUp(image, bounds.width / 2 - 1, bounds.width - 1, 0, bounds.height / 2);
         tableSW[i - 32] = sumUp(image, 0, bounds.width / 2, bounds.height / 2 - 1, bounds.height - 1);
         tableSE[i - 32] = sumUp(image, bounds.width / 2 - 1, bounds.width - 1, bounds.height / 2 - 1, bounds.height - 1);
      }

      return createNormalizedGreyscaleTable(table, tableNW, tableNE, tableSW, tableSE);
   }

   private static double sumUp(BufferedImage image, int minX, int maxX, int minY, int maxY) {
      double sum = 0.0;

      for (int x = minX; x <= maxX; x++) {
         for (int y = minY; y <= maxY; y++) {
            sum += image.getRGB(x, y);
         }
      }

      return sum / (double)(maxX - minX + 1) / (double)(maxY - minY + 1);
   }

   private static void renderCharacter(BufferedImage image, Rectangle size, Font font, char c) {
      Graphics graphics = image.getGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, size.width, size.height);
      graphics.setColor(Color.BLACK);
      graphics.setFont(font);
      graphics.drawString(String.valueOf(c), -size.x, -size.y);
      graphics.dispose();
   }

   private static Rectangle getMaximumAsciiRenderingBounds(Font font) {
      int minX = 0;
      int maxX = 0;
      int minY = 0;
      int maxY = 0;

      for (int i = 32; i < 127; i++) {
         GlyphVector vector = font.createGlyphVector(FONT_RENDER_CONTEXT, new char[]{(char)i});
         Rectangle pixelBounds = vector.getPixelBounds(FONT_RENDER_CONTEXT, 0.0F, 0.0F);
         int x1 = pixelBounds.x;
         int x2 = x1 + pixelBounds.width;
         int y1 = pixelBounds.y;
         int y2 = y1 + pixelBounds.height;
         if (i == 32) {
            minX = x1;
            maxX = x2;
            minY = y1;
            maxY = y2;
         } else {
            if (minX > x1) {
               minX = x1;
            }

            if (minY > y1) {
               minY = y1;
            }

            if (maxX < x2) {
               maxX = x2;
            }

            if (maxY < y2) {
               maxY = y2;
            }
         }
      }

      return new Rectangle(minX, minY, maxX - minX, maxY - minY);
   }

   public static AsciiGreyscaleTable createNormalizedGreyscaleTable(double[] table, double[] tableNW, double[] tableNE, double[] tableSW, double[] tableSE) {
      Ensure.ensureArgumentTrue("Expected 95 table, but was " + table.length, table.length == 95);
      Ensure.ensureArgumentTrue("Expected 95 tableNW, but was " + tableNW.length, tableNW.length == 95);
      Ensure.ensureArgumentTrue("Expected 95 tableSW, but was " + tableSW.length, tableSW.length == 95);
      Ensure.ensureArgumentTrue("Expected 95 tableNE, but was " + tableNE.length, tableNE.length == 95);
      Ensure.ensureArgumentTrue("Expected 95 tableSE, but was " + tableSE.length, tableSE.length == 95);
      double max = table[0];
      double min = table[0];
      double max4 = tableNW[0];
      double min4 = tableNW[0];

      for (int i = 0; i < table.length; i++) {
         double value = table[i];
         double valueNW = tableNW[i];
         double valueNE = tableNE[i];
         double valueSW = tableSW[i];
         double valueSE = tableSE[i];
         if (value > max) {
            max = value;
         }

         if (value < min) {
            min = value;
         }

         if (valueNW > max4) {
            max4 = valueNW;
         }

         if (valueNW < min4) {
            min4 = valueNW;
         }

         if (valueNE > max4) {
            max4 = valueNE;
         }

         if (valueNE < min4) {
            min4 = valueNE;
         }

         if (valueSW > max4) {
            max4 = valueSW;
         }

         if (valueSW < min4) {
            min4 = valueSW;
         }

         if (valueSE > max4) {
            max4 = valueSE;
         }

         if (valueSE < min4) {
            min4 = valueSE;
         }
      }

      int[][] greyscales4 = new int[95][4];
      char[] greyscales = new char[95];

      for (int i = 32; i < 127; i++) {
         greyscales[i - 32] = (char)((int)Math.round((table[i - 32] - min) / (max - min) * 255.0));
         greyscales4[i - 32][0] = (char)((int)Math.round((tableNW[i - 32] - min4) / (max4 - min4) * 255.0));
         greyscales4[i - 32][1] = (char)((int)Math.round((tableNE[i - 32] - min4) / (max4 - min4) * 255.0));
         greyscales4[i - 32][2] = (char)((int)Math.round((tableSW[i - 32] - min4) / (max4 - min4) * 255.0));
         greyscales4[i - 32][3] = (char)((int)Math.round((tableSE[i - 32] - min4) / (max4 - min4) * 255.0));
      }

      int[][] reverseTable = new int[95][5];

      for (int i = 0; i < reverseTable.length; i++) {
         reverseTable[i][0] = greyscales4[i][0];
         reverseTable[i][1] = greyscales4[i][1];
         reverseTable[i][2] = greyscales4[i][2];
         reverseTable[i][3] = greyscales4[i][3];
         reverseTable[i][4] = (char)(i + 32);
      }

      Arrays.sort(reverseTable, new Comparator() {
         @Override
         public int compare(Object arg0, Object arg1) {
            char[] first = (char[])arg0;
            char[] second = (char[])arg1;

            for (int i = 0; i < 4; i++) {
               if (first[i] < second[i]) {
                  return -1;
               }

               if (first[i] > second[i]) {
                  return 1;
               }
            }

            return 0;
         }
      });
      return new AsciiGreyscaleTable(new boolean[96], new boolean[96], greyscales, greyscales4, reverseTable);
   }
}
