package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.util.ValueSegmentation;
import de.jave.image.monochrome.GMonochromeImage;

public class ThresholdDithering implements IGreyscaleDithering {
   @Override
   public GMonochromeImage dither(GGreyscaleImage image) {
      GMonochromeImage g = new GMonochromeImage(image.getWidth(), image.getHeight());
      int max = image.getMaxValue();
      int min = image.getMinValue();
      int threshold = (max + min) / 2;

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            if (image.get(x, y) >= threshold) {
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
      GGreyscaleImage g = new GGreyscaleImage(image.getWidth(), image.getHeight());
      ValueSegmentation segmentation = new ValueSegmentation(0, 255, depth);

      for (int y = 0; y < image.getHeight(); y++) {
         for (int x = 0; x < image.getWidth(); x++) {
            int value = image.get(x, y);
            g.set(x, y, segmentation.getSegmentValue(value));
         }
      }

      return g;
   }

   @Override
   public String getName() {
      return "Threshold";
   }
}
