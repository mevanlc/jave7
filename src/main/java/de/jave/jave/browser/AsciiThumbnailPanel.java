package de.jave.jave.browser;

import de.jave.core.io.FileSelectionModel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import net.dizzy.commons.core.model.listener.IChangeListener;

public abstract class AsciiThumbnailPanel extends JComponent {
   private static final int SPACING = 2;
   public static final Dimension THUMBNAIL_SIZE = new Dimension(110, 120);
   private final JComponent content;
   private final ThumbnailsModel model = new ThumbnailsModel();
   private final FileSelectionModel selectionModel;

   public AsciiThumbnailPanel(final FileSelectionModel selectionModel) {
      this.selectionModel = selectionModel;
      selectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiThumbnailPanel.this.repaint();
         }
      });
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent evt) {
            JaveFilePreviewItem imageDia = AsciiThumbnailPanel.this.getDiaAt(evt.getPoint());
            if (imageDia != null) {
               if (evt.getClickCount() == 1) {
                  selectionModel.setSelected(imageDia.getFile(), !selectionModel.isSelected(imageDia.getFile()));
               }
            }
         }

         @Override
         public void mouseClicked(MouseEvent evt) {
            JaveFilePreviewItem imageDia = AsciiThumbnailPanel.this.getDiaAt(evt.getPoint());
            if (imageDia != null) {
               if (evt.getClickCount() == 2 && !evt.isMetaDown()) {
                  AsciiThumbnailPanel.this.handleDoubleClick(imageDia);
               }
            }
         }
      });
      JScrollPane scrollPane = new JScrollPane(this);
      scrollPane.getVerticalScrollBar().setBlockIncrement(THUMBNAIL_SIZE.height + 2);
      scrollPane.getVerticalScrollBar().setUnitIncrement(THUMBNAIL_SIZE.height + 2);
      scrollPane.getHorizontalScrollBar().setBlockIncrement(THUMBNAIL_SIZE.width + 2);
      scrollPane.getHorizontalScrollBar().setUnitIncrement(THUMBNAIL_SIZE.width + 2);
      this.content = scrollPane;
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiThumbnailPanel.this.thumbNailsChanged();
         }
      });
   }

   private JaveFilePreviewItem getDiaAt(Point point) {
      int dx = THUMBNAIL_SIZE.width + 2;
      int dy = THUMBNAIL_SIZE.height + 2;
      int xCount = this.getSize().width / dx;
      if (xCount <= 0) {
         xCount = 1;
      }

      if (point.x >= 0 && point.x <= xCount * dx) {
         int row = point.y / dy;
         int col = point.x / dx;
         int startRow = 0;
         row += 0;
         int index = row * xCount + col;
         JaveFilePreviewItem[] thumbnails = this.model.getThumbnails();
         return index >= thumbnails.length ? null : thumbnails[index];
      } else {
         return null;
      }
   }

   protected abstract void handleDoubleClick(JaveFilePreviewItem var1);

   @Override
   public Dimension getPreferredSize() {
      Dimension d = this.getSize();
      int dx = THUMBNAIL_SIZE.width + 2;
      int dy = THUMBNAIL_SIZE.height + 2;
      int xCount = d.width / dx;
      if (xCount <= 0) {
         xCount = 1;
      }

      int yCount = (int)Math.ceil((double)this.model.getSize() / (double)xCount);
      int preferredWidth = dx * xCount;
      int preferredHeight = dy * yCount;
      if (preferredWidth < 300) {
         preferredWidth = 300;
      }

      if (preferredHeight < 300) {
         preferredHeight = 300;
      }

      return new Dimension(preferredWidth, preferredHeight);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   private void thumbNailsChanged() {
      this.revalidate();
      this.repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      JaveFilePreviewItem[] thumbnails = this.model.getThumbnails();
      int count = thumbnails.length;
      int dx = THUMBNAIL_SIZE.width + 2;
      int dy = THUMBNAIL_SIZE.height + 2;
      int xCount = d.width / dx;
      if (xCount <= 0) {
         xCount = 1;
      }

      if (count != 0) {
         int y = 0;
         int i = 0;

         for (int var11 = 0 * xCount; var11 < count && y < d.height; var11++) {
            JaveFilePreviewItem dia = thumbnails[var11];
            if (dia.getFileType() == JaveFileType.TEXT) {
               new JaveFilePreviewItemRenderer().paint(dia, g, THUMBNAIL_SIZE, this, this.selectionModel);
            } else if (dia.getFileType() == JaveFileType.ANIMATION) {
               new JaveAnimationFilePreviewItemRenderer().paint(dia, g, THUMBNAIL_SIZE, this, this.selectionModel);
            } else if (dia.getFileType() == JaveFileType.RASTER_IMAGE) {
               new JaveFilePreviewItemRenderer().paint(dia, g, THUMBNAIL_SIZE, this, this.selectionModel);
            } else {
               if (dia.getFileType() != JaveFileType.VT) {
                  throw new UnsupportedOperationException();
               }

               new JaveAnimationFilePreviewItemRenderer().paint(dia, g, THUMBNAIL_SIZE, this, this.selectionModel);
            }

            g.translate(dx, 0);
            if ((var11 + 1) % xCount == 0) {
               g.translate(-dx * xCount, dy);
               y += dy;
            }
         }
      }
   }

   public JComponent getContent() {
      return this.content;
   }

   public int getImageDiaCount() {
      return this.getModel().getSize();
   }

   public ThumbnailsModel getModel() {
      return this.model;
   }
}
