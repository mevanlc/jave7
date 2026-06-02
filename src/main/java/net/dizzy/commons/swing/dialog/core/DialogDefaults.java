package net.dizzy.commons.swing.dialog.core;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public final class DialogDefaults {
   private static final DialogDefaults INSTANCE = new DialogDefaults();
   private List<Image> frameIconImages = new ArrayList<>();

   private DialogDefaults() {
   }

   public static DialogDefaults getInstance() {
      return INSTANCE;
   }

   public void setFrameIconImages(List<? extends Image> frameIconImages) {
      this.frameIconImages = new ArrayList<>(frameIconImages);
   }

   public List<Image> getFrameIconImages() {
      return new ArrayList<>(frameIconImages);
   }
}
