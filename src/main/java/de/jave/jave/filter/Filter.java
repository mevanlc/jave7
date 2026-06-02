package de.jave.jave.filter;

import de.jave.jave.algorithm.GeneralAlgorithm;
import de.jave.lib.CharacterPlate;
import java.awt.Insets;
import net.dizzy.commons.core.util.Ensure;

public class Filter {
   private final FilterMatrix[] postFilters;
   private final FilterMatrix[] preFilters;
   private final FilterMatrix[] midFilters;
   private final FilterMatrix[] lineArtCleanerFilters;
   private final FilterMatrix[] image2asciiSimpleEdgeFilters;

   public Filter(
      FilterMatrix[] preFilters,
      FilterMatrix[] midFilters,
      FilterMatrix[] postFilters,
      FilterMatrix[] lineArtCleanerFilters,
      FilterMatrix[] image2asciiSimpleEdgeFilters
   ) {
      Ensure.ensureArgumentNotNull(preFilters);
      Ensure.ensureArgumentNotNull(midFilters);
      Ensure.ensureArgumentNotNull(postFilters);
      Ensure.ensureArgumentNotNull(lineArtCleanerFilters);
      Ensure.ensureArgumentNotNull(image2asciiSimpleEdgeFilters);
      this.preFilters = preFilters;
      this.midFilters = midFilters;
      this.postFilters = postFilters;
      this.lineArtCleanerFilters = lineArtCleanerFilters;
      this.image2asciiSimpleEdgeFilters = image2asciiSimpleEdgeFilters;
   }

   private static void filter(CharacterPlate plate, FilterMatrix[] filters) {
      Ensure.ensureArgumentNotNull(plate);
      Ensure.ensureArgumentNotNull(filters);
      int height = plate.getHeight();
      int width = plate.getWidth();
      int[][] raster = new int[height][width];

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (plate.get(x, y) != ' ') {
               for (int yy = y - 1; yy <= y + 1; yy++) {
                  for (int xx = x - 1; xx <= x + 1; xx++) {
                     if (yy >= 0 && yy < height && xx >= 0 && xx < width) {
                        raster[yy][xx]++;
                     }
                  }
               }
            }
         }
      }

      for (int y = 0; y < height; y++) {
         for (int xxx = 0; xxx < width; xxx++) {
            if (raster[y][xxx] != 0) {
               for (int f = 0; f < filters.length; f++) {
                  if (filters[f].fits(plate, width, height, xxx, y, raster[y][xxx])) {
                     filters[f].apply(plate, width, height, xxx, y);
                     break;
                  }
               }
            }
         }
      }
   }

   private static void filterQuick(CharacterPlate cp, FilterMatrix[] filters) {
      int h = cp.getHeight();
      int w = cp.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            char character = cp.get(x, y);
            if (character == '#') {
               for (int filterIndex = 0; filterIndex < filters.length; filterIndex++) {
                  if (filters[filterIndex].fits(cp, w, h, x, y, -1)) {
                     filters[filterIndex].apply(cp, w, h, x, y);
                     break;
                  }
               }
            }
         }
      }
   }

   private static void cleanUp(CharacterPlate cp) {
      GeneralAlgorithm.replace(cp, '#', ' ');
   }

   public void filter(CharacterPlate characterPlate, FilterMode mode) {
      int originalWidth = characterPlate.getWidth();
      int originalHeight = characterPlate.getHeight();
      Insets emptyInsets = characterPlate.getEmptyInsets();
      int newWidth = originalWidth + 2 - emptyInsets.left - emptyInsets.right;
      int newHeight = originalHeight + 2 - emptyInsets.top - emptyInsets.bottom;
      if (newWidth > 0 && newHeight > 0) {
         CharacterPlate temporaryPlate = new CharacterPlate(newWidth, newHeight);
         characterPlate.getCopy(emptyInsets.left, emptyInsets.top, newWidth - 1, newHeight - 1).pasteInto(temporaryPlate, 1, 1);
         switch (mode) {
            case SOFT:
               filterQuick(temporaryPlate, this.preFilters);
               filterQuick(temporaryPlate, this.midFilters);
               cleanUp(temporaryPlate);
               filter(temporaryPlate, this.postFilters);
               break;
            case MID:
               filterQuick(temporaryPlate, this.preFilters);
               filterQuick(temporaryPlate, this.midFilters);
               cleanUp(temporaryPlate);
               break;
            case LINE_ART_CLEANER:
               filter(temporaryPlate, this.lineArtCleanerFilters);
               break;
            case IMAGE2ASCII_SIMPLE_EDGE:
               filter(temporaryPlate, this.image2asciiSimpleEdgeFilters);
         }

         temporaryPlate.pasteIntoForce(characterPlate, emptyInsets.left - 1, emptyInsets.top - 1);
      }
   }
}
