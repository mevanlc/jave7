package de.jave.jave.watermark;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.disy.commons.core.util.Ensure;

public class WatermarkData {
   private final BufferedImage originalImage;
   private Image visibleImage;

   public WatermarkData(BufferedImage originalImage, Image visibleImage) {
      Ensure.ensureArgumentNotNull(originalImage);
      Ensure.ensureArgumentNotNull(visibleImage);
      this.originalImage = originalImage;
      this.visibleImage = visibleImage;
   }

   public BufferedImage getOriginalImage() {
      return this.originalImage;
   }

   public void setVisibleImage(Image visibleImage) {
      this.visibleImage = visibleImage;
   }

   public Image getVisibleImage() {
      return this.visibleImage;
   }

   public Dimension getImageSize() {
      return new Dimension(this.originalImage.getWidth(), this.originalImage.getHeight());
   }
}
