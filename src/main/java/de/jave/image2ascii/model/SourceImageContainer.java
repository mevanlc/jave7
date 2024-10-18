package de.jave.image2ascii.model;

import de.jave.image.greyscale.GGreyscaleImage;
import java.awt.image.BufferedImage;

public class SourceImageContainer {
   private final GGreyscaleImage rawImage;
   private final BufferedImage sourceImage;
   private final GGreyscaleImage rawPreviewImage;

   public SourceImageContainer(GGreyscaleImage rawImage, GGreyscaleImage rawPreviewImage, BufferedImage sourceImage) {
      this.rawImage = rawImage;
      this.rawPreviewImage = rawPreviewImage;
      this.sourceImage = sourceImage;
   }

   public GGreyscaleImage getRawConversionImage() {
      return this.rawImage;
   }

   public GGreyscaleImage getRawPreviewImage() {
      return this.rawPreviewImage;
   }

   public BufferedImage getOriginalImage() {
      return this.sourceImage;
   }
}
