package de.jave.image.greyscale;

public class GreyscaleImageFactory {
   public GGreyscaleImage createGreyscaleImage(int[] pixelData, int width, int height) {
      return this.createGreyscaleImage(pixelData, width, height, 0.299, 0.587, 0.114);
   }

   public GGreyscaleImage createGreyscaleImage(int[] pixelData, int width, int height, double redWeight, double greenWeight, double blueWeight) {
      int redMultiplicator = (int)(redWeight * 1000.0);
      int greenMultiplicator = (int)(greenWeight * 1000.0);
      int blueMultiplicator = (int)(blueWeight * 1000.0);
      GGreyscaleImage image = new GGreyscaleImage(width, height);

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            int originalPixel = pixelData[y * width + x];
            int value = (
                  redMultiplicator * (originalPixel >> 16 & 0xFF)
                     + greenMultiplicator * (originalPixel >> 8 & 0xFF)
                     + blueMultiplicator * (originalPixel & 0xFF)
               )
               / 1000;
            image.set(x, y, value);
         }
      }

      return image;
   }
}
