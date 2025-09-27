package net.disy.commons.swing.image;

public class DisyCommonsSwingImageProvider extends ImageProvider {
   private static final DisyCommonsSwingImageProvider instance = new DisyCommonsSwingImageProvider();

   public static DisyCommonsSwingImageProvider getInstance() {
      return instance;
   }

   private DisyCommonsSwingImageProvider() {
      super("net/disy/commons/swing/icons");
   }
}
