package de.jave.image2ascii.dialog;

import de.jave.gui.io.SourceFilePanel;
import de.jave.image.ImageUtilities;
import de.jave.image.gui.BufferedImageDisplayableImage;
import de.jave.image.swing.ImagePanel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class Image2AsciiSourceImagePanel {
   private static final Dimension MAX_SIZE = new Dimension(500, 250);
   private final JComponent content;
   private final ImagePanel imagePanel;

   public Image2AsciiSourceImagePanel(final Image2AsciiSourceImageModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.imagePanel = new ImagePanel();
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            BufferedImage originalImage = model.getSourceImage().getOriginalImage();
            Dimension originalImageSize = new Dimension(originalImage.getWidth(), originalImage.getHeight());
            Dimension viewSize = ImageUtilities.calculateIsometricLimitedSize(originalImageSize, Image2AsciiSourceImagePanel.MAX_SIZE);
            Image2AsciiSourceImagePanel.this.imagePanel.setImage(new BufferedImageDisplayableImage(originalImage, viewSize));
         }
      });
      this.imagePanel.getContent().setPreferredSize(new Dimension(400, 200));
      SourceFilePanel sourceFilePanel = new Image2AsciiSourceFilePanel(model);
      JPanel p = new JPanel(new BorderLayout(2, 2));
      p.add(this.imagePanel.getContent(), "Center");
      p.add(sourceFilePanel.createPanel(), "North");
      this.content = p;
   }

   public JComponent getContent() {
      return this.content;
   }
}
