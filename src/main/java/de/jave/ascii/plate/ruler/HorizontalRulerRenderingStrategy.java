package de.jave.ascii.plate.ruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import net.dizzy.commons.swing.layout.util.LayoutDirection;

public class HorizontalRulerRenderingStrategy implements IRulerRenderingStrategy {
   @Override
   public void renderMouseLocation(Graphics g, Dimension size, Point mousePoint) {
      g.drawLine(mousePoint.x, 0, mousePoint.x, 0 + size.height);
   }

   @Override
   public Dimension getPreferredSize(AsciiRulerProperties properties, Dimension mainComponentSize) {
      return new Dimension(mainComponentSize.width, 11);
   }

   @Override
   public boolean isRelevantMouseLocationChange(Point oldPoint, Point newPoint) {
      return oldPoint == null || newPoint == null || newPoint.x != oldPoint.x;
   }

   @Override
   public LayoutDirection getLayoutDirection() {
      return LayoutDirection.HORIZONTAL;
   }

   @Override
   public void paintRuler(Graphics2D g, Dimension size, AsciiRulerProperties properties) {
      Rectangle clipBounds = g.getClipBounds();
      g.setColor(properties.getBackgroundColor());
      g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
      double characterWidth = properties.getCharacterWidth();
      g.setColor(properties.getForegroundColor());
      g.setFont(properties.getFont());
      Point origin = properties.getPlateOrigin();
      int startCharacterIndex = (int)((double)(clipBounds.x - origin.x) / characterWidth);
      if (startCharacterIndex < 0) {
         startCharacterIndex = 0;
      }

      int endCharacterIndex = (int)((double)(clipBounds.x - origin.x + clipBounds.width) / characterWidth);
      if (properties.getDocumentSize() != null && properties.getDocumentSize().width <= endCharacterIndex) {
         endCharacterIndex = properties.getDocumentSize().width - 1;
      }

      int height = size.height;

      for (int characterIndex = startCharacterIndex; characterIndex <= endCharacterIndex + 1; characterIndex++) {
         int x = origin.x + (int)((double)characterIndex * characterWidth);
         if (characterIndex % 10 == 0) {
            g.drawLine(x, height - 5, x, height - 1);
            g.drawString(String.valueOf(characterIndex), x + 2, 8);
         } else if (characterIndex % 5 == 0) {
            g.drawLine(x, height - 4, x, height - 1);
         } else {
            g.drawLine(x, height - 2, x, height - 1);
         }
      }

      if (properties.isPrintMarginColumnVisible()) {
         int x = (int)((double)properties.getPrintMarginColumn() * characterWidth);
         g.drawLine(x - 2, 0, x + 2, 0);
         g.drawLine(x - 1, 1, x + 1, 1);
         g.drawLine(x - 1, 2, x + 1, 2);
         g.drawLine(x, 3, x, height);
      }
   }
}
