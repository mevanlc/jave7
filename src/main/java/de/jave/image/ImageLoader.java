package de.jave.image;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.GreyscaleImageFactory;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
   public static BufferedImage loadImage(File file) throws IOException {
      if (!file.exists()) {
         throw new FileNotFoundException("The image file '" + file.getAbsolutePath() + "' does not exist");
      } else {
         try {
            return ImageIO.read(file);
         } catch (Exception var3) {
            throw new IOException("Error loading image file '" + file.getAbsolutePath() + "'", var3);
         }
      }
   }

   public static GGreyscaleImage loadAsGreyScaleImage(File file) throws IOException {
      BufferedImage image = loadImage(file);
      int oldWidth = image.getWidth();
      int oldHeight = image.getHeight();
      int[] pixels2 = new int[oldWidth * oldHeight];
      PixelGrabber pg2 = new PixelGrabber(image, 0, 0, oldWidth, oldHeight, pixels2, 0, oldWidth);

      try {
         pg2.grabPixels();
      } catch (InterruptedException var7) {
         throw new IOException("Error loading image", var7);
      }

      if ((pg2.getStatus() & 128) != 0) {
         throw new IOException("Error loading image - aborted");
      } else {
         return new GreyscaleImageFactory().createGreyscaleImage(pixels2, oldWidth, oldHeight);
      }
   }
}
