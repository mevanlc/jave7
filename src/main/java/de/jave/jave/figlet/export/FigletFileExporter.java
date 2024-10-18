package de.jave.jave.figlet.export;

import de.jave.jave.LineAlgorithm;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FigletFileExporter {
   private static final int[][] UMLAUTS = new int[][]{{196, 65}, {214, 79}, {220, 85}, {228, 97}, {246, 111}, {252, 117}, {223, 32}};

   public static void export(FigletExportModel model, File file) {
      CharacterPlate plate = model.getCharacterPlate();
      char[][] raster = model.getRaster();
      boolean[] charUsed = new boolean[128];
      CharacterPlate[] figChars = new CharacterPlate[255];
      int figCharWidth = model.getCharacterWidth();
      int figCharHeight = model.getCharacterHeight();
      int figCharDescent = model.getCharacterDescent();
      int figCharVSpacing = model.getVerticalSpacing();
      int figCharHSpacing = model.getHorizontalSpacing();

      for (int y = 0; y < raster.length; y++) {
         for (int x = 0; x < raster[0].length; x++) {
            if (raster[y][x] != 0) {
               figChars[raster[y][x]] = plate.getCopy(
                  new Rectangle(x * figCharHSpacing + x * figCharWidth, y * figCharVSpacing + y * figCharHeight, figCharWidth, figCharHeight)
               );
               char[][] ch = figChars[raster[y][x]].getContent();

               for (int yy = 0; yy < figCharHeight; yy++) {
                  for (int xx = 0; xx < figCharWidth; xx++) {
                     if (ch[yy][xx] <= 128) {
                        charUsed[ch[yy][xx]] = true;
                     }
                  }
               }
            }
         }
      }

      char endmark = '#';

      while (endmark < 128 && charUsed[endmark]) {
         endmark++;
      }

      char hardblank = (char)(endmark + 1);

      while (hardblank < 128 && charUsed[hardblank]) {
         hardblank++;
      }

      for (int i = 0; i < 26; i++) {
         if (figChars[i + 65] == null) {
            figChars[i + 65] = figChars[i + 97].getClone();
         } else if (figChars[i + 97] == null) {
            figChars[i + 97] = figChars[i + 65].getClone();
         }
      }

      for (int ix = 0; ix < UMLAUTS.length; ix++) {
         if (figChars[UMLAUTS[ix][0]] == null) {
            figChars[UMLAUTS[ix][0]] = figChars[UMLAUTS[ix][1]].getClone();
         }
      }

      for (int ixx = 32; ixx < 127; ixx++) {
         if (figChars[ixx] == null) {
            figChars[ixx] = new CharacterPlate(figCharWidth, figCharHeight);
         }
      }

      if (figChars[32].isEmpty()) {
         LineAlgorithm.drawLineBresenham(figChars[32], 0, 0, 0, figCharHeight - 1, hardblank);
      }

      for (int ixxx = 0; ixxx < 255; ixxx++) {
         if (figChars[ixxx] != null) {
            Insets insets = figChars[ixxx].getEmptyInsets();
            if (insets.right > 0) {
               insets.right--;
            }

            int w = figCharWidth - insets.right + 2;
            figChars[ixxx].setSize(w, figCharHeight);
            if (ixxx != 32 && figChars[ixxx].isEmpty()) {
               figChars[ixxx].set(0, 0, (char)ixxx);
            }

            LineAlgorithm.drawLineBresenham(figChars[ixxx], w - 2, 0, w - 2, figCharHeight - 1, endmark);
            figChars[ixxx].setForce(w - 1, figCharHeight - 1, endmark);
         }
      }

      int printDirection = 0;
      int baseLine = figCharHeight - figCharDescent;
      int maxLength = figCharWidth + 2;
      String comment = model.getComment();
      Dimension dim = TextTools.getDimensionOf(comment);
      int commentLines = dim.height;
      int codetagCount = 0;

      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(file));
         bw.write(
            "flf2a"
               + hardblank
               + ' '
               + figCharHeight
               + ' '
               + baseLine
               + ' '
               + maxLength
               + ' '
               + model.getOldLayout()
               + ' '
               + commentLines
               + ' '
               + 0
               + ' '
               + model.getFullLayout()
               + ' '
               + 0
         );
         bw.newLine();
         if (commentLines > 0) {
            bw.write(comment);
            bw.newLine();
         }

         for (int ixxxx = 32; ixxxx < 127; ixxxx++) {
            bw.write(figChars[ixxxx].toString());
            bw.newLine();
         }

         for (int ixxxx = 0; ixxxx < UMLAUTS.length; ixxxx++) {
            bw.write(figChars[UMLAUTS[ixxxx][0]].toString());
            bw.newLine();
         }

         bw.close();
      } catch (Exception var22) {
         System.err.println(var22);
         var22.printStackTrace(System.err);
         System.err.println("Error saving File: " + var22);
      }
   }
}
