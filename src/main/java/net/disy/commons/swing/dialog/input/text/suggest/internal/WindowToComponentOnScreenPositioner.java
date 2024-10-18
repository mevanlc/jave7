package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JWindow;
import net.disy.commons.swing.util.GuiUtilities;

public class WindowToComponentOnScreenPositioner {
   public static void adjustPosition(JComponent hookComponent, JWindow window) {
      Rectangle componentBounds = getBoundsOnScreen(hookComponent);
      Rectangle screenBounds = GuiUtilities.calculateScreenBounds(hookComponent);
      Dimension windowSize = window.getSize();
      int y;
      if (doesNotFitBelow(windowSize, componentBounds, screenBounds) && isMoreSpaceAboveThanBelow(componentBounds, screenBounds)) {
         y = componentBounds.y - windowSize.height;
      } else {
         y = componentBounds.y + componentBounds.height;
      }

      int x = Math.min(componentBounds.x, screenBounds.x + screenBounds.width - windowSize.width);
      window.setLocation(x, y);
   }

   private static boolean isMoreSpaceAboveThanBelow(Rectangle componentBounds, Rectangle screenBounds) {
      int spaceAbove = Math.max(0, componentBounds.y - screenBounds.y);
      int spaceBelow = getSpaceBelow(componentBounds, screenBounds);
      return spaceAbove > spaceBelow;
   }

   private static int getSpaceBelow(Rectangle componentBounds, Rectangle screenBounds) {
      return Math.max(0, screenBounds.y + screenBounds.height - componentBounds.y - componentBounds.height);
   }

   private static boolean doesNotFitBelow(Dimension windowSize, Rectangle componentBounds, Rectangle screenBounds) {
      return getSpaceBelow(componentBounds, screenBounds) < windowSize.height;
   }

   private static Rectangle getBoundsOnScreen(JComponent component) {
      return new Rectangle(component.getLocationOnScreen(), component.getSize());
   }
}
