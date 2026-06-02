package de.jave.ascii.plate.ruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import net.dizzy.commons.swing.layout.util.LayoutDirection;

public class VerticalRulerRenderingStrategy implements IRulerRenderingStrategy {
   private static final AffineTransform ROTATE_LEFT = new AffineTransform(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F);

   @Override
   public void renderMouseLocation(Graphics g, Dimension size, Point mousePoint) {
      g.drawLine(0, mousePoint.y, size.width, mousePoint.y);
   }

   @Override
   public Dimension getPreferredSize(AsciiRulerProperties properties, Dimension mainComponentSize) {
      return new Dimension(11, mainComponentSize.height);
   }

   @Override
   public boolean isRelevantMouseLocationChange(Point oldPoint, Point newPoint) {
      return oldPoint == null || newPoint == null || newPoint.y != oldPoint.y;
   }

   @Override
   public LayoutDirection getLayoutDirection() {
      return LayoutDirection.VERTICAL;
   }

   @Override
   public void paintRuler(Graphics2D g, Dimension size, AsciiRulerProperties properties) {
      Rectangle clipBounds = g.getClipBounds();
      g.setColor(properties.getBackgroundColor());
      g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
      double characterHeight = properties.getCharacterHeight();
      g.setColor(properties.getForegroundColor());
      g.setFont(properties.getFont());
      Point origin = properties.getPlateOrigin();
      int startCharacterIndex = (int)((double)(clipBounds.y - origin.y) / characterHeight);
      if (startCharacterIndex < 0) {
         startCharacterIndex = 0;
      }

      int endCharacterIndex = (int)((double)(clipBounds.y - origin.y + clipBounds.height) / characterHeight);
      if (properties.getDocumentSize() != null && properties.getDocumentSize().height <= endCharacterIndex) {
         endCharacterIndex = properties.getDocumentSize().height - 1;
      }

      int width = size.width;

      for (int characterIndex = startCharacterIndex; characterIndex <= endCharacterIndex + 1; characterIndex++) {
         int y = origin.y + (int)((double)characterIndex * characterHeight);
         if (characterIndex % 10 == 0) {
            g.drawLine(width - 4, y, width - 1, y);
            AffineTransform previousTransformation = g.getTransform();
            g.transform(ROTATE_LEFT);
            String label = String.valueOf(characterIndex);
            int stringWidth = g.getFontMetrics().stringWidth(label);
            g.drawString(label, -y - stringWidth - 2, 9);
            g.setTransform(previousTransformation);
         } else if (characterIndex % 5 == 0) {
            g.drawLine(width - 5, y, width - 1, y);
         } else {
            g.drawLine(width - 2, y, width - 1, y);
         }
      }
   }
}
