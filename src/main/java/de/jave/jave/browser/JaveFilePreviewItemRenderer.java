package de.jave.jave.browser;

import de.jave.core.io.FileSelectionModel;
import de.jave.jave.JaveGlobalRessources;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.SystemColor;

public class JaveFilePreviewItemRenderer {
   private static final int DEFAULT_WIDTH = 110;
   private static final int DEFAULT_HEIGHT = 120;

   public static Dimension getPreferredImageSize() {
      return new Dimension(102, 90);
   }

   public void paint(JaveFilePreviewItem data, Graphics g, Dimension size, Component imageObserver, FileSelectionModel selectionModel) {
      Rectangle labelBounds = new Rectangle(0, 0, size.width, data.getIcon().getIconHeight() + 3);
      Rectangle diaBounds = new Rectangle(0, data.getIcon().getIconHeight() + 3, size.width, size.height - data.getIcon().getIconHeight() - 3);
      this.paintBackground(data, g, labelBounds, diaBounds, selectionModel);
      this.paintLabel(data, g, labelBounds, imageObserver, selectionModel);
      int w = diaBounds.width - 9;
      int h = diaBounds.height - 12;
      int x0 = size.width / 2 - 1;
      int y0 = (size.height - 13) / 2 + 1 + data.getIcon().getIconHeight();
      if (data.getPreviewImage() == null) {
         g.setColor(SystemColor.controlShadow);
         g.drawLine(diaBounds.x + 6, diaBounds.y + 6, diaBounds.x + diaBounds.width - 6, diaBounds.y + diaBounds.height - 6);
         g.drawLine(diaBounds.x + 6, diaBounds.y + diaBounds.height - 6, diaBounds.x + diaBounds.width - 6, diaBounds.y + 6);
      } else {
         int iWidth = data.getPreviewImage().getWidth(imageObserver);
         int iHeight = data.getPreviewImage().getHeight(imageObserver);
         if (iWidth <= w && iHeight <= h) {
            w = iWidth;
            h = iHeight;
         } else {
            double scaleX = (double)w / (double)iWidth;
            double scaleY = (double)h / (double)iHeight;
            double scale = scaleX < scaleY ? scaleX : scaleY;
            w = (int)((double)iWidth * scale);
            h = (int)((double)iHeight * scale);
         }

         g.drawImage(data.getPreviewImage(), x0 - w / 2, y0 - h / 2, w, h, imageObserver);
      }

      int x1 = x0 - w / 2 - 1;
      int y1 = y0 - h / 2 - 1;
      int x2 = x1 + w + 2;
      int y2 = y1 + h + 2;
      g.setColor(SystemColor.controlShadow);
      g.drawLine(x1, y1, x2 - 1, y1);
      g.drawLine(x1, y1 + 1, x1, y2 - 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
      g.drawLine(x2 - 1, y2 - 2, x2 - 1, y1 + 1);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(x1 + 1, y1 + 1, x2 - 2, y1 + 1);
      g.drawLine(x1 + 1, y1 + 2, x1 + 1, y2 - 2);
      Dimension originalSize = data.getOriginalSize();
      if (originalSize != null) {
         String labelW = String.valueOf(originalSize.width);
         FontMetrics fm = g.getFontMetrics();
         int labelWidth = fm.stringWidth(labelW);
         int labelHeight = fm.getAscent();
         g.setColor(SystemColor.control);
         g.fillRect((size.width - labelWidth) / 2 - 1, 5 + data.getIcon().getIconHeight(), labelWidth + 2, labelHeight);
         g.setColor(SystemColor.controlShadow);
         g.drawRect((size.width - labelWidth) / 2 - 1, 5 + data.getIcon().getIconHeight(), labelWidth + 2, labelHeight);
         g.setColor(SystemColor.controlText);
         g.drawString(labelW, (size.width - labelWidth) / 2 + 1, labelHeight + 3 + data.getIcon().getIconHeight());
         String labelH = String.valueOf(originalSize.height);
         labelWidth = fm.stringWidth(labelH);
         g.setColor(SystemColor.control);
         g.fillRect(2, y0 - labelHeight / 2, labelWidth + 2, labelHeight);
         g.setColor(SystemColor.controlShadow);
         g.drawRect(2, y0 - labelHeight / 2, labelWidth + 2, labelHeight);
         g.setColor(SystemColor.controlText);
         g.drawString(labelH, 4, y0 - labelHeight / 2 - 2 + labelHeight);
      }
   }

   private void paintLabel(JaveFilePreviewItem data, Graphics g, Rectangle labelBounds, Component imageObserver, FileSelectionModel selectionModel) {
      data.getIcon().paintIcon(imageObserver, g, 2, 1);
      if (selectionModel.isSelected(data.getFile())) {
         g.setColor(SystemColor.textHighlightText);
      } else {
         g.setColor(SystemColor.textText);
      }

      g.setFont(JaveGlobalRessources.FONT_SMALL);
      FontMetrics fm = g.getFontMetrics();
      String text = data.getLabel();

      while (data.getLabel().length() > 0 && fm.stringWidth(text) > labelBounds.width - data.getIcon().getIconWidth() - 2) {
         text = text.substring(0, text.length() - 1);
      }

      g.drawString(text, 1 + data.getIcon().getIconWidth(), data.getIcon().getIconHeight() - 1);
   }

   protected void paintBackground(JaveFilePreviewItem data, Graphics g, Rectangle labelBounds, Rectangle diaBounds, FileSelectionModel selectionModel) {
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(diaBounds.x + 1, diaBounds.y + 1, diaBounds.x + diaBounds.width - 1, diaBounds.y + 1);
      g.drawLine(diaBounds.x + 1, diaBounds.y + 2, diaBounds.x + 1, diaBounds.y + diaBounds.height - 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(diaBounds.x, diaBounds.y, diaBounds.x + diaBounds.width - 1, diaBounds.y);
      g.drawLine(diaBounds.x, diaBounds.y + 1, diaBounds.x, diaBounds.y + diaBounds.height - 1);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(diaBounds.x + 1, diaBounds.y + diaBounds.height - 2, diaBounds.x + diaBounds.width - 2, diaBounds.y + diaBounds.height - 2);
      g.drawLine(diaBounds.x + diaBounds.width - 2, diaBounds.y + diaBounds.height - 1, diaBounds.x + diaBounds.width - 2, diaBounds.y + 1);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(diaBounds.x, diaBounds.y + diaBounds.height - 1, diaBounds.x + diaBounds.width - 1, diaBounds.y + diaBounds.height - 1);
      g.drawLine(diaBounds.x + diaBounds.width - 1, diaBounds.y + diaBounds.height - 1, diaBounds.x + diaBounds.width - 1, diaBounds.y);
      if (selectionModel.isSelected(data.getFile())) {
         g.setColor(SystemColor.textHighlight);
         g.fillRect(labelBounds.x, labelBounds.y, labelBounds.width, labelBounds.height);
         g.fillRect(diaBounds.x + 2, diaBounds.y + 1, diaBounds.width - 4, diaBounds.height - 3);
      }
   }
}
