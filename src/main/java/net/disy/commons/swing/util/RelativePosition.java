package net.disy.commons.swing.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

public abstract class RelativePosition {
   public static final RelativePosition TOP_LEFT = new RelativePosition() {
      @Override
      protected Point createLocation(Rectangle windowBounds, Rectangle ownerBounds) {
         return new Point(ownerBounds.x - windowBounds.width, ownerBounds.y);
      }
   };
   public static final RelativePosition RIGHT = new RelativePosition() {
      @Override
      protected Point createLocation(Rectangle windowBounds, Rectangle ownerBounds) {
         return new Point((int)ownerBounds.getMaxX(), (int)(ownerBounds.getCenterY() - (double)(windowBounds.height / 2)));
      }
   };
   public static final RelativePosition CENTER = new RelativePosition() {
      @Override
      protected Point createLocation(Rectangle windowBounds, Rectangle ownerBounds) {
         return new Point(
            (int)(ownerBounds.getCenterX() - (double)(windowBounds.width / 2)), (int)(ownerBounds.getCenterY() - (double)(windowBounds.height / 2))
         );
      }
   };

   private RelativePosition() {
   }

   public void place(Window window) {
      Window owner = window.getOwner();
      if (owner != null && owner.isVisible()) {
         window.setLocation(this.createLocation(window.getBounds(), window.getOwner().getBounds()));
      } else {
         GuiUtilities.centerOnScreen(window);
      }
   }

   protected abstract Point createLocation(Rectangle var1, Rectangle var2);
}
