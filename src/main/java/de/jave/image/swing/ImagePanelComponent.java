package de.jave.image.swing;

import de.jave.image.gui.IDisplayableImage;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.color.SwingColors;

public final class ImagePanelComponent extends JComponent {
   private final ObjectModel<IDisplayableImage> imageModel;
   private final Dimension sizeLimit;
   private final ObjectModel<IXorPainter> xorPainterModel;

   public ImagePanelComponent(ObjectModel<IDisplayableImage> imageModel, ObjectModel<IXorPainter> xorPainterModel, Dimension sizeLimit) {
      Ensure.ensureArgumentNotNull(imageModel);
      Ensure.ensureArgumentNotNull(xorPainterModel);
      Ensure.ensureArgumentNotNull(sizeLimit);
      this.imageModel = imageModel;
      this.sizeLimit = sizeLimit;
      this.xorPainterModel = xorPainterModel;
      imageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ImagePanelComponent.this.revalidate();
            ImagePanelComponent.this.repaint();
         }
      });
      xorPainterModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ImagePanelComponent.this.repaint();
         }
      });
   }

   @Override
   public Dimension getPreferredSize() {
      IDisplayableImage image = this.imageModel.getValue();
      Dimension size = image == null ? new Dimension(100, 100) : image.getSize();
      int width = size.width;
      int height = size.height;
      if (this.sizeLimit.width > 0 && width > this.sizeLimit.width) {
         width = this.sizeLimit.width;
      }

      if (this.sizeLimit.height > 0 && height > this.sizeLimit.height) {
         height = this.sizeLimit.height;
      }

      return new Dimension(width, height);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   protected void paintComponent(Graphics g) {
      IDisplayableImage image = this.imageModel.getValue();
      Dimension canvasSize = this.getSize();
      Graphics2D graphics = (Graphics2D)g;
      graphics.setColor(this.getBackground());
      graphics.fillRect(0, 0, canvasSize.width, canvasSize.height);
      if (image != null) {
         this.renderImage(graphics, image, canvasSize);
      } else {
         this.renderNoImage(graphics);
      }

      IXorPainter xorPainter = this.xorPainterModel.getValue();
      if (xorPainter != null) {
         xorPainter.paintXor(g);
      }
   }

   private void renderNoImage(Graphics2D graphics) {
      graphics.setColor(SwingColors.getControlDkShadowColor());
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      graphics.setFont(new Font("Dialog", 0, 10));
      graphics.drawString("No image loaded.", 5, 12);
   }

   private void renderImage(Graphics2D graphics, IDisplayableImage image, Dimension canvasSize) {
      int w = image.getSize().width;
      int h = image.getSize().height;
      if (w > canvasSize.width) {
         w = canvasSize.width;
      }

      if (h > canvasSize.height) {
         h = canvasSize.height;
      }

      int x0 = (canvasSize.width - w) / 2;
      int y0 = (canvasSize.height - h) / 2;
      image.paint(graphics, x0, y0, w, h);
   }
}
