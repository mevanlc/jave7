package de.jave.image.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import net.dizzy.commons.core.util.Ensure;

public class BufferedImageDisplayableImage implements IDisplayableImage {
   private final BufferedImage image;
   private final Dimension viewSize;

   public BufferedImageDisplayableImage(BufferedImage image) {
      this(image, new Dimension(image.getWidth(), image.getHeight()));
   }

   public BufferedImageDisplayableImage(BufferedImage image, Dimension viewSize) {
      Ensure.ensureArgumentNotNull(image);
      Ensure.ensureArgumentNotNull(viewSize);
      this.image = image;
      this.viewSize = viewSize;
   }

   @Override
   public Dimension getSize() {
      return this.viewSize;
   }

   @Override
   public void paint(Graphics g, int x, int y, int width, int height) {
      g.drawImage(this.image, x, y, width, height, null);
   }
}
