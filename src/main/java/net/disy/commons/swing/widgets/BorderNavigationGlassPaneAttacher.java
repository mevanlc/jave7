package net.disy.commons.swing.navigate;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.util.GuiUtilities;

public class BorderNavigationGlassPaneAttacher {
   public static void attachNavigationGlassPane(
      JComponent component, int navigationBorderWidth, int diagonalBorderRatio, NavigationDirection direction, INavigationHandler handler
   ) {
      Ensure.ensureArgumentNotNull(direction);
      Ensure.ensureArgumentNotNull(handler);
      Window frame = GuiUtilities.getWindowFor(component);
      if (frame instanceof RootPaneContainer) {
         RootPaneContainer parentFrame = (RootPaneContainer)frame;
         Point parentLocation = parentFrame.getLayeredPane().getLocationOnScreen();
         Point componentLocation = component.getLocationOnScreen();
         int dx = componentLocation.x - parentLocation.x;
         int dy = componentLocation.y - parentLocation.y;
         Shape area = getNavigationArea(component, navigationBorderWidth, diagonalBorderRatio, direction, dx, dy);
         BorderNavigationGlassPane glassPane = new BorderNavigationGlassPane(area, direction, handler);
         parentFrame.setGlassPane(glassPane);
         glassPane.setVisible(true);
      }
   }

   public static Shape createNavigationArea(JComponent component, int navigationBorderWidth, int diagonalBorderRatio, NavigationDirection direction) {
      return getNavigationArea(component, navigationBorderWidth, diagonalBorderRatio, direction, 0, 0);
   }

   private static Shape getNavigationArea(
      JComponent component, int navigationBorderWidth, int diagonalBorderRatio, NavigationDirection direction, int dx, int dy
   ) {
      Dimension componentSize = component.getSize();
      int diagonalLength = Math.min(componentSize.width, componentSize.height) / diagonalBorderRatio;
      switch (direction) {
         case EAST:
            return new Rectangle(
               dx + componentSize.width - navigationBorderWidth, dy + diagonalLength, navigationBorderWidth, componentSize.height - 2 * diagonalLength
            );
         case NORTH:
            return new Rectangle(dx + diagonalLength, dy, componentSize.width - 2 * diagonalLength, navigationBorderWidth);
         case SOUTH:
            return new Rectangle(
               dx + diagonalLength, dy + componentSize.height - navigationBorderWidth, componentSize.width - 2 * diagonalLength, navigationBorderWidth
            );
         case WEST:
            return new Rectangle(dx, dy + diagonalLength, navigationBorderWidth, componentSize.height - 2 * diagonalLength);
         case NORTH_EAST:
            return new Polygon(
               new int[]{
                  dx + componentSize.width,
                  dx + componentSize.width - diagonalLength,
                  dx + componentSize.width - diagonalLength,
                  dx + componentSize.width - navigationBorderWidth,
                  dx + componentSize.width - navigationBorderWidth,
                  dx + componentSize.width,
                  dx + componentSize.width
               },
               new int[]{dy, dy, dy + navigationBorderWidth, dy + navigationBorderWidth, dy + diagonalLength, dy + diagonalLength, dy},
               7
            );
         case NORTH_WEST:
            return new Polygon(
               new int[]{dx, dx + diagonalLength, dx + diagonalLength, dx + navigationBorderWidth, dx + navigationBorderWidth, dx, dx},
               new int[]{dy, dy, dy + navigationBorderWidth, dy + navigationBorderWidth, dy + diagonalLength, dy + diagonalLength, dy},
               7
            );
         case SOUTH_EAST:
            return new Polygon(
               new int[]{
                  dx + componentSize.width,
                  dx + componentSize.width - diagonalLength,
                  dx + componentSize.width - diagonalLength,
                  dx + componentSize.width - navigationBorderWidth,
                  dx + componentSize.width - navigationBorderWidth,
                  dx + componentSize.width,
                  dx + componentSize.width
               },
               new int[]{
                  dy + componentSize.height,
                  dy + componentSize.height,
                  dy + componentSize.height - navigationBorderWidth,
                  dy + componentSize.height - navigationBorderWidth,
                  dy + componentSize.height - diagonalLength,
                  dy + componentSize.height - diagonalLength,
                  dy + componentSize.height
               },
               7
            );
         case SOUTH_WEST:
            return new Polygon(
               new int[]{dx, dx + diagonalLength, dx + diagonalLength, dx + navigationBorderWidth, dx + navigationBorderWidth, dx, dx},
               new int[]{
                  dy + componentSize.height,
                  dy + componentSize.height,
                  dy + componentSize.height - navigationBorderWidth,
                  dy + componentSize.height - navigationBorderWidth,
                  dy + componentSize.height - diagonalLength,
                  dy + componentSize.height - diagonalLength,
                  dy + componentSize.height
               },
               7
            );
         default:
            throw new UnreachableCodeReachedException();
      }
   }
}
