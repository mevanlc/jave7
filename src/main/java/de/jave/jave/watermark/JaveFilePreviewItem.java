package de.jave.jave.browser;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public class JaveFilePreviewItem {
   private final Icon icon;
   private final File file;
   private final String label;
   private final JaveFileType fileType;
   private Image previewImage;
   private Dimension originalSize;

   public JaveFilePreviewItem(Icon icon, File file, String label, JaveFileType fileType) {
      Ensure.ensureArgumentNotNull(icon);
      Ensure.ensureArgumentNotNull(file);
      Ensure.ensureArgumentNotNull(label);
      Ensure.ensureArgumentNotNull(fileType);
      this.icon = icon;
      this.file = file;
      this.label = label;
      this.fileType = fileType;
   }

   public File getFile() {
      return this.file;
   }

   public JaveFileType getFileType() {
      return this.fileType;
   }

   public Icon getIcon() {
      return this.icon;
   }

   public String getLabel() {
      return this.label;
   }

   public Image getPreviewImage() {
      return this.previewImage;
   }

   public Dimension getOriginalSize() {
      return this.originalSize;
   }

   public void setPreview(Image previewImage, Dimension originalSize) {
      this.previewImage = previewImage;
      this.originalSize = originalSize;
   }
}
