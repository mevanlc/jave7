package de.jave.image.swing;

import de.jave.image.GImage;
import de.jave.image.gui.BufferedImageDisplayableImage;
import de.jave.image.gui.GImageDisplayableImage;
import de.jave.image.gui.IDisplayableImage;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import net.dizzy.commons.core.model.ObjectModel;

public class ImagePanel implements IInteractiveGraphicsEditable {
   private final JComponent imageComponent;
   private final JComponent content;
   private final ObjectModel<IDisplayableImage> imageModel;
   private final ObjectModel<IXorPainter> xorPainterModel;

   public ImagePanel(BufferedImage image) {
      this(new BufferedImageDisplayableImage(image));
   }

   public ImagePanel(GImage image) {
      this(new GImageDisplayableImage(image));
   }

   public ImagePanel() {
      this((IDisplayableImage)null);
   }

   public ImagePanel(Dimension sizeLimit) {
      this(null, sizeLimit);
   }

   public ImagePanel(IDisplayableImage image) {
      this(image, new Dimension(-1, -1));
   }

   public ImagePanel(IDisplayableImage image, Dimension sizeLimit) {
      this.imageModel = new ObjectModel<>(image);
      this.xorPainterModel = new ObjectModel<>();
      this.imageComponent = new ImagePanelComponent(this.imageModel, this.xorPainterModel, sizeLimit);
      this.content = new JScrollPane(this.imageComponent);
      this.setImage(image);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void clear() {
      this.setImage((IDisplayableImage)null);
   }

   public void setImage(BufferedImage image) {
      this.setImage(new BufferedImageDisplayableImage(image));
   }

   public void setImage(GImage image) {
      this.setImage(new GImageDisplayableImage(image));
   }

   public void setImage(IDisplayableImage image) {
      this.imageModel.setValue(image);
   }

   @Override
   public void addMouseListener(MouseListener mouseListener) {
      this.imageComponent.addMouseListener(mouseListener);
   }

   @Override
   public void addMouseMotionListener(MouseMotionListener mouseListener) {
      this.imageComponent.addMouseMotionListener(mouseListener);
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
      this.imageComponent.addKeyListener(keyListener);
   }

   @Override
   public void setXorPainter(IXorPainter xorPainter) {
      this.xorPainterModel.setValue(xorPainter);
   }

   @Override
   public void setCursor(Cursor cursor) {
      this.imageComponent.setCursor(cursor);
   }
}
