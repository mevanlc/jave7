package de.jave.image.gui;

import de.jave.image.GImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import net.dizzy.commons.core.util.Ensure;

public class GImageDisplayableImage implements IDisplayableImage {
   private final GImage image;

   public GImageDisplayableImage(GImage image) {
      Ensure.ensureArgumentNotNull(image);
      this.image = image;
   }

   @Override
   public Dimension getSize() {
      return this.image.getSize();
   }

   @Override
   public void paint(Graphics g, int x, int y, int width, int height) {
      BufferedImage buffer = new BufferedImage(this.image.getWidth(), this.image.getHeight(), 4);
      Graphics bufferGraphics = buffer.getGraphics();
      this.image.paint(bufferGraphics, 0, 0);
      bufferGraphics.dispose();
      g.drawImage(buffer, x, y, width, height, null);
   }
}
