package de.jave.image.monochrome.algorithm;

import de.jave.image.monochrome.GMonochromeImage;

public class EdgeReduction {
   public void perform(GMonochromeImage image) {
      int width = image.getWidth();
      int height = image.getHeight();
      boolean done = false;

      while (!done) {
         done = true;

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
               if (image.get(x, y) == 0
                  && (x == 0 || image.get(x - 1, y) == 1)
                  && image.get(x + 1, y) == 0
                  && (y > 0 && image.get(x, y - 1) == 0 || y < height - 1 && image.get(x, y + 1) == 0)) {
                  image.set(x, y, 1);
                  done = false;
               }
            }
         }

         for (int y = 1; y < height; y++) {
            for (int xx = 0; xx < width; xx++) {
               if (image.get(xx, y) == 0
                  && (y == height - 1 || image.get(xx, y + 1) == 1)
                  && image.get(xx, y - 1) == 0
                  && (xx > 0 && image.get(xx - 1, y) == 0 || xx < width - 1 && image.get(xx + 1, y) == 0)) {
                  image.set(xx, y, 1);
                  done = false;
               }
            }
         }

         for (int y = 0; y < height; y++) {
            for (int xxx = 1; xxx < width; xxx++) {
               if (image.get(xxx, y) == 0
                  && (xxx == width - 1 || image.get(xxx + 1, y) == 1)
                  && image.get(xxx - 1, y) == 0
                  && (y > 0 && image.get(xxx, y - 1) == 0 || y < height - 1 && image.get(xxx, y + 1) == 0)) {
                  image.set(xxx, y, 1);
                  done = false;
               }
            }
         }

         for (int y = 0; y < height - 1; y++) {
            for (int xxxx = 0; xxxx < width; xxxx++) {
               if (image.get(xxxx, y) == 0
                  && (y == 0 || image.get(xxxx, y - 1) == 1)
                  && image.get(xxxx, y + 1) == 0
                  && (xxxx > 0 && image.get(xxxx - 1, y) == 0 || xxxx < width - 1 && image.get(xxxx + 1, y) == 0)) {
                  image.set(xxxx, y, 1);
                  done = false;
               }
            }
         }
      }
   }
}
