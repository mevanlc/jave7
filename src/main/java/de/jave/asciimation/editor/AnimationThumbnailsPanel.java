package de.jave.asciimation.editor;

import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class AnimationThumbnailsPanel extends JComponent {
   private static final Dimension THUMBNAIL_SIZE = new Dimension(100, 60);
   private final AnimationEditorModel model;

   public AnimationThumbnailsPanel(final AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      MouseListener mouseListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            int frameIndex = AnimationThumbnailsPanel.this.getFrameAt(evt.getPoint());
            if (frameIndex >= 0 && frameIndex < AnimationThumbnailsPanel.this.model.getAnimationFile().getFrameCount()) {
               ListSelectionModel selectionModel = AnimationThumbnailsPanel.this.model.getFrameSelectionModel();
               if (evt.isMetaDown()) {
                  if (!selectionModel.isSelectedIndex(frameIndex)) {
                     selectionModel.setSelectionInterval(frameIndex, frameIndex);
                  }

                  JPopupMenu menu = AnimationEditorPopupMenuFactory.createFramePopupMenu(model);
                  menu.show(AnimationThumbnailsPanel.this, evt.getX(), evt.getY());
               } else if (evt.isControlDown()) {
                  if (selectionModel.isSelectedIndex(frameIndex)) {
                     selectionModel.removeSelectionInterval(frameIndex, frameIndex);
                  } else {
                     selectionModel.addSelectionInterval(frameIndex, frameIndex);
                  }
               } else {
                  selectionModel.setSelectionInterval(frameIndex, frameIndex);
               }
            }
         }

         @Override
         public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
               int frameIndex = AnimationThumbnailsPanel.this.getFrameAt(evt.getPoint());
               if (frameIndex >= 0 && frameIndex < AnimationThumbnailsPanel.this.model.getAnimationFile().getFrameCount()) {
                  AnimationThumbnailsPanel.this.model.getCurrentFrameIndexModel().setCurrentFrameIndex(frameIndex);
               }
            }
         }
      };
      this.addMouseListener(mouseListener);
      this.setBackground(Color.black);
      model.getFrameSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            AnimationThumbnailsPanel.this.repaint();
         }
      });
      model.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationThumbnailsPanel.this.repaint();
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationThumbnailsPanel.this.repaint();
         }
      });
   }

   public JComponent getContent() {
      return this;
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(THUMBNAIL_SIZE.width * 3, THUMBNAIL_SIZE.height);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Dimension size = this.getSize();
      g.setColor(this.getBackground());
      g.fillRect(0, 0, size.width, size.height);
      int centerX = size.width / 2;
      int currentIndex = this.model.getCurrentFrameIndexModel().getCurrentFrameIndex();
      int startIndex = currentIndex - (int)Math.ceil((double)(centerX - THUMBNAIL_SIZE.width / 2) / (double)THUMBNAIL_SIZE.width);
      int endIndex = currentIndex + (int)Math.ceil((double)(centerX - THUMBNAIL_SIZE.width / 2) / (double)THUMBNAIL_SIZE.width);

      for (int frameIndex = startIndex; frameIndex <= endIndex; frameIndex++) {
         Image image = this.createThumbnailImage(frameIndex);
         if (image != null) {
            Rectangle rectangle = this.getFrameBounds(frameIndex);
            Rectangle imageBounds = new Rectangle(
               rectangle.x + (rectangle.width - image.getWidth(null)) / 2,
               rectangle.y + (rectangle.height - image.getHeight(null)) / 2,
               image.getWidth(null),
               image.getHeight(null)
            );
            g.drawImage(image, imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height, null);
            if (this.model.getFrameSelectionModel().isSelectedIndex(frameIndex)) {
               g.setColor(Color.black);
               g.setXORMode(SystemColor.textHighlight);
               g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
               g.setPaintMode();
            }
         }
      }
   }

   private Image createThumbnailImage(int frameIndex) {
      JaveAnimationFrame frame = this.getFrameIfInRange(frameIndex);
      if (frame == null) {
         return null;
      } else {
         int[][] chContent = frame.getContent();
         CharacterPlate content = new CharacterPlate(chContent);
         int[][] chSelection = frame.getSelection();
         if (chSelection != null) {
            new CharacterPlate(chSelection).pasteInto(content, frame.getSelectionX(), frame.getSelectionY());
         }

         Color bg = this.model.getAnimationFile().getProperties().getBackgroundColor();
         Color fg = this.model.getAnimationFile().getProperties().getForegroundColor();
         BufferedImage image = AsciiToThumbnailConverter.ascii2ImageQuick(content, 2, bg, fg);
         int imageWidth = image.getWidth();
         int imageHeight = image.getHeight() * 2;
         double scaleX = (double)(THUMBNAIL_SIZE.width - 4) / (double)imageWidth;
         double scaleY = (double)(THUMBNAIL_SIZE.height - 4) / (double)imageHeight;
         double scale = Math.min(scaleX, scaleY);
         int newWidth = (int)((double)imageWidth * scale);
         int newHeight = (int)((double)imageHeight * scale);
         return image.getScaledInstance(newWidth, newHeight, 4);
      }
   }

   private Rectangle getFrameBounds(int frameIndex) {
      Dimension size = this.getSize();
      int x = (frameIndex - this.model.getCurrentFrameIndexModel().getCurrentFrameIndex()) * THUMBNAIL_SIZE.width + size.width / 2 - THUMBNAIL_SIZE.width / 2;
      int y = 0;
      int width = THUMBNAIL_SIZE.width;
      int height = size.height;
      return new Rectangle(x, 0, width, height);
   }

   private int getFrameAt(Point point) {
      int currentIndex = this.model.getCurrentFrameIndexModel().getCurrentFrameIndex();
      int offset = point.x - (this.getSize().width / 2 - THUMBNAIL_SIZE.width / 2);
      return offset >= 0 ? currentIndex + offset / THUMBNAIL_SIZE.width : currentIndex + offset / THUMBNAIL_SIZE.width - 1;
   }

   private JaveAnimationFrame getFrameIfInRange(int index) {
      JaveAnimationFile animationFile = this.model.getAnimationFile();
      return index >= 0 && index < animationFile.getFrameCount() ? animationFile.getFrame(index) : null;
   }
}
