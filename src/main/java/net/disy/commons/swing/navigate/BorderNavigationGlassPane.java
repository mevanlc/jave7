package net.disy.commons.swing.navigate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.ColorUtilities;
import net.disy.commons.swing.color.SwingColors;

public class BorderNavigationGlassPane extends JComponent {
   private final Shape navigationShape;
   private final NavigationDirection direction;

   public BorderNavigationGlassPane(final Shape navigationShape, final NavigationDirection direction, final INavigationHandler handler) {
      Ensure.ensureArgumentNotNull(navigationShape);
      Ensure.ensureArgumentNotNull(direction);
      Ensure.ensureArgumentNotNull(handler);
      this.navigationShape = navigationShape;
      this.direction = direction;
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            handler.navigateTo(direction);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            BorderNavigationGlassPane.this.dispose();
         }
      });
      this.addMouseMotionListener(new MouseMotionListener() {
         @Override
         public void mouseMoved(MouseEvent e) {
            if (!navigationShape.contains(e.getPoint())) {
               BorderNavigationGlassPane.this.dispose();
            }
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (!navigationShape.contains(e.getPoint())) {
               BorderNavigationGlassPane.this.dispose();
            }
         }
      });
   }

   private void dispose() {
      this.setVisible(false);
   }

   @Override
   protected void paintComponent(Graphics g) {
      Graphics2D graphics = (Graphics2D)g;
      graphics.setColor(this.getNavigationButtonAreaColor());
      graphics.fill(this.navigationShape);
      Rectangle bounds = this.navigationShape.getBounds();
      switch (this.direction) {
         case EAST:
         case WEST:
            this.renderVerticalErrorBar(graphics, this.navigationShape.getBounds());
            break;
         case NORTH:
         case SOUTH:
            this.renderHorizontalArrowBar(graphics, this.navigationShape.getBounds());
            break;
         case NORTH_EAST:
            this.renderHorizontalArrowBar(graphics, new Rectangle(bounds.x, bounds.y, bounds.width, 20));
            this.renderVerticalErrorBar(graphics, new Rectangle(bounds.x + bounds.width - 20, bounds.y, 20, bounds.height));
            break;
         case NORTH_WEST:
            this.renderHorizontalArrowBar(graphics, new Rectangle(bounds.x, bounds.y, bounds.width, 20));
            this.renderVerticalErrorBar(graphics, new Rectangle(bounds.x, bounds.y, 20, bounds.height));
            break;
         case SOUTH_EAST:
            this.renderHorizontalArrowBar(graphics, new Rectangle(bounds.x, bounds.y + bounds.height - 20, bounds.width, 20));
            this.renderVerticalErrorBar(graphics, new Rectangle(bounds.x + bounds.width - 20, bounds.y, 20, bounds.height));
            break;
         case SOUTH_WEST:
            this.renderHorizontalArrowBar(graphics, new Rectangle(bounds.x, bounds.y + bounds.height - 20, bounds.width, 20));
            this.renderVerticalErrorBar(graphics, new Rectangle(bounds.x, bounds.y, 20, bounds.height));
      }
   }

   private void renderHorizontalArrowBar(Graphics2D graphics, Rectangle rectangle) {
      this.renderArrowBar(graphics, rectangle, rectangle.width, 1, 0);
   }

   private void renderVerticalErrorBar(Graphics2D graphics, Rectangle rectangle) {
      this.renderArrowBar(graphics, rectangle, rectangle.height, 0, 1);
   }

   private void renderArrowBar(Graphics2D graphics, Rectangle rectangle, int length, int dx, int dy) {
      int arrowCount = getAppropriateArrowCount(length);
      int centerY = (int)rectangle.getCenterY();
      int centerX = (int)rectangle.getCenterX();
      renderArrow(graphics, centerX, centerY, this.direction);
      int spacing = length / (arrowCount + 1);

      for (int i = 1; i <= arrowCount / 2; i++) {
         renderArrow(graphics, centerX + spacing * i * dx, centerY + spacing * i * dy, this.direction);
         renderArrow(graphics, centerX - spacing * i * dx, centerY - spacing * i * dy, this.direction);
      }
   }

   private static int getAppropriateArrowCount(int length) {
      return length / 75;
   }

   private Color getNavigationButtonAreaColor() {
      Color controlColor = SwingColors.getControlColor();
      return ColorUtilities.getTransparentColor(controlColor, 128);
   }

   private static void renderArrow(Graphics g, int x, int y, NavigationDirection direction) {
      g.setColor(SwingColors.getPanelForegroundColor());
      switch (direction) {
         case EAST:
            g.fillPolygon(new int[]{x + 2, x - 3, x - 3, x + 2}, new int[]{y, y - 5, y + 5, y}, 4);
            return;
         case WEST:
            g.fillPolygon(new int[]{x - 3, x + 2, x + 2, x - 3}, new int[]{y, y - 5, y + 5, y}, 4);
            return;
         case NORTH:
            g.fillPolygon(new int[]{x, x - 5, x + 5, x}, new int[]{y - 2, y + 3, y + 3, y - 2}, 4);
            return;
         case SOUTH:
            g.fillPolygon(new int[]{x, x - 5, x + 5, x}, new int[]{y + 2, y - 3, y - 3, y + 2}, 4);
            return;
         case NORTH_EAST:
            g.fillPolygon(new int[]{x + 2, x - 5, x + 2, x + 2}, new int[]{y - 2, y - 2, y + 5, y - 2}, 4);
            return;
         case NORTH_WEST:
            g.fillPolygon(new int[]{x - 2, x - 2, x + 5, x - 2}, new int[]{y - 2, y + 5, y - 2, y - 2}, 4);
            return;
         case SOUTH_EAST:
            g.fillPolygon(new int[]{x + 2, x - 5, x + 2, x + 2}, new int[]{y + 2, y + 2, y - 5, y + 2}, 4);
            return;
         case SOUTH_WEST:
            g.fillPolygon(new int[]{x - 2, x - 2, x + 5, x - 2}, new int[]{y + 2, y - 5, y + 2, y + 2}, 4);
            return;
         default:
            throw new UnreachableCodeReachedException();
      }
   }
}
