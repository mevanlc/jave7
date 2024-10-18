package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.util.ValueSegmentation;
import de.jave.image.monochrome.GMonochromeImage;

public class SimpleRandomDithering extends AbstractRandomDithering {
   @Override
   public GMonochromeImage dither(GGreyscaleImage image) {
      this.seed(image);
      GMonochromeImage g = new GMonochromeImage(image.getWidth(), image.getHeight());

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            int value = image.get(x, y);
            value += 64 - this.random(128);
            if (value >= 128) {
               g.set(x, y, 1);
            } else {
               g.set(x, y, 0);
            }
         }
      }

      return g;
   }

   @Override
   public GGreyscaleImage dither(GGreyscaleImage image, int depth) {
      this.seed(image);
      ValueSegmentation segmentation = new ValueSegmentation(0, 255, depth);
      GGreyscaleImage g = new GGreyscaleImage(image.getWidth(), image.getHeight());

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            int value = image.get(x, y);
            value += this.random(255 / depth) - 255 / depth / 2;
            g.set(x, y, segmentation.getSegmentValue(value));
         }
      }

      return g;
   }

   @Override
   public String getName() {
      return "Random";
   }
}
