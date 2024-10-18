package net.disy.commons.swing.directmanipulation;

import java.awt.Point;
import net.disy.commons.core.util.Ensure;

@Deprecated
public class ManipulationMarker implements IManipulationMaker {
   private final Point point;

   public ManipulationMarker(Point point) {
      Ensure.ensureArgumentNotNull(point);
      this.point = point;
   }

   @Override
   public Point getPoint() {
      return this.point;
   }
}
