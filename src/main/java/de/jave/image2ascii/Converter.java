package de.jave.image2ascii;

import de.jave.image.GImage;
import de.jave.image.ImageLoader;
import de.jave.image.Rotation;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.GreyscaleImageFactory;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.progress.NonCancelable;
import net.disy.commons.core.progress.NullProgressMonitor;

public class Converter {
   public static CharacterPlate convert(
      File imageFile,
      Rotation rotate,
      double shapeFactor,
      int resultWidth,
      AbstractImage2AsciiAlgorithm algorithm,
      boolean normalize,
      boolean invert,
      double gamma,
      double highlight,
      double shadow,
      double sharpen,
      IGreyscaleDithering dithering
   ) throws ConversionException {
      BufferedImage image = loadBufferedImage(imageFile);
      Dimension originalSize = new Dimension(image.getWidth(), image.getHeight());
      originalSize = adjustToRotation(rotate, originalSize);
      Dimension scaledSize = algorithm.getImageSizeForSettings(originalSize, resultWidth, shapeFactor);
      scaledSize = adjustToRotation(rotate, scaledSize);
      Image scaledImage = image.getScaledInstance(scaledSize.width, scaledSize.height, 4);
      GGreyscaleImage image2 = convertToGreyScaleImage(scaledImage, scaledSize);
      if (rotate != Rotation.NONE) {
         image2 = (GGreyscaleImage)image2.rotate(rotate);
      }

      image2 = image2.convert(normalize, invert, gamma, highlight, shadow);
      if (sharpen > 0.0) {
         image2 = image2.sharpen(sharpen);
      }

      GImage gi = image2;
      if (algorithm.isMonochromeImageRequired() && dithering != null) {
         gi = dithering.dither(image2);
      }

      try {
         return algorithm.convert(gi, new NullProgressMonitor(), NonCancelable.getInstance());
      } catch (InterruptedException var24) {
         throw new UnreachableCodeReachedException(var24);
      }
   }

   private static Dimension adjustToRotation(Rotation rotate, Dimension size) {
      return rotate != Rotation.LEFT && rotate != Rotation.RIGHT ? size : getSwapped(size);
   }

   private static Dimension getSwapped(Dimension originalSize) {
      return new Dimension(originalSize.width, originalSize.height);
   }

   private static BufferedImage loadBufferedImage(File imageFile) throws ConversionException {
      try {
         return ImageLoader.loadImage(imageFile);
      } catch (IOException var3) {
         throw new ConversionException("Error loading image '" + imageFile.getAbsolutePath() + "'.", var3);
      }
   }

   private static GGreyscaleImage convertToGreyScaleImage(Image scaledImage, Dimension imageSize) throws ConversionException {
      int width = imageSize.width;
      int height = imageSize.height;
      int[] pixels = new int[width * height];
      PixelGrabber grabber = new PixelGrabber(scaledImage, 0, 0, width, height, pixels, 0, width);

      try {
         grabber.grabPixels();
      } catch (InterruptedException var7) {
         throw new ConversionException("Internal Error", var7);
      }

      if ((grabber.getStatus() & 128) != 0) {
         throw new ConversionException("Internal Error: image fetch aborted or errored");
      } else {
         return new GreyscaleImageFactory().createGreyscaleImage(pixels, width, height);
      }
   }
}
