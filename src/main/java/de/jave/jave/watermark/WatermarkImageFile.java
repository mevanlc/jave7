package de.jave.jave.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import net.disy.commons.core.util.Ensure;

public class WatermarkImageFile {
   private final File file;
   private final BufferedImage image;

   public WatermarkImageFile(BufferedImage image) {
      this(image, null);
   }

   public WatermarkImageFile(BufferedImage image, File file) {
      Ensure.ensureArgumentNotNull(image);
      this.image = image;
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public BufferedImage getImage() {
      return this.image;
   }
}
