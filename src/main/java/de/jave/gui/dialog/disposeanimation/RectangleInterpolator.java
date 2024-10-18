package de.jave.gui.dialog.disposeanimation;

import java.awt.Rectangle;
import net.disy.commons.animatedinterpolation.AbstractInterpolator;

public class RectangleInterpolator extends AbstractInterpolator<Rectangle> {
   public Rectangle interpolate(Rectangle startValue, Rectangle endValue, double t) {
      return new Rectangle(
         this.interpolate(startValue.x, endValue.x, t),
         this.interpolate(startValue.y, endValue.y, t),
         this.interpolate(startValue.width, endValue.width, t),
         this.interpolate(startValue.height, endValue.height, t)
      );
   }
}
