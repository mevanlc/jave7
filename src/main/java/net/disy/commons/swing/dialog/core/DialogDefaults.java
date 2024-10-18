package net.disy.commons.swing.dialog.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.util.Ensure;

public class DialogDefaults {
   private static final DialogDefaults instance = new DialogDefaults();
   private final List<Image> frameIconImages = new ArrayList<>();

   public static DialogDefaults getInstance() {
      return instance;
   }

   private DialogDefaults() {
   }

   public void setFrameIconImage(Image frameIconImage) {
      this.frameIconImages.clear();
      if (frameIconImage != null) {
         this.frameIconImages.add(frameIconImage);
      }
   }

   @Deprecated
   public Image getFrameIconImage() {
      return this.frameIconImages.isEmpty() ? null : this.frameIconImages.get(0);
   }

   public void setFrameIconImages(List<? extends Image> frameIconImages) {
      Ensure.ensureArgumentNotNull(frameIconImages);
      this.frameIconImages.clear();
      this.frameIconImages.addAll(frameIconImages);
   }

   public List<Image> getFrameIconImages() {
      return this.frameIconImages;
   }
}
