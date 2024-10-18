package de.jave.jave.application.resources;

import net.disy.commons.swing.image.ImageProvider;

public class JaveImageProvider extends ImageProvider {
   private static final JaveImageProvider instance = new JaveImageProvider();

   public static final JaveImageProvider getInstance() {
      return instance;
   }

   private JaveImageProvider() {
      super("de/jave");
   }
}
