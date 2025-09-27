package net.disy.commons.swing.directmanipulation;

import java.awt.Point;
import java.awt.Rectangle;
import net.disy.commons.core.util.Ensure;

@Deprecated
public class RectangleMarkerProvider implements IManipulationMarkerProvider {
   private final Rectangle rectangle;

   public RectangleMarkerProvider(Rectangle rectangle) {
      Ensure.ensureArgumentNotNull(rectangle);
      this.rectangle = rectangle;
   }

   @Override
   public IManipulationMaker[] getMarkers() {
      Point[] points = new Point[]{
         new Point(this.rectangle.x, this.rectangle.y),
         new Point(this.rectangle.x + this.rectangle.width / 2, this.rectangle.y),
         new Point(this.rectangle.x, this.rectangle.y + this.rectangle.height / 2),
         new Point(this.rectangle.x, this.rectangle.y + this.rectangle.height),
         new Point(this.rectangle.x + this.rectangle.width, this.rectangle.y),
         new Point(this.rectangle.x + this.rectangle.width, this.rectangle.y + this.rectangle.height / 2),
         new Point(this.rectangle.x + this.rectangle.width / 2, this.rectangle.y + this.rectangle.height),
         new Point(this.rectangle.x + this.rectangle.width, this.rectangle.y + this.rectangle.height)
      };
      IManipulationMaker[] markers = new IManipulationMaker[points.length];

      for (int i = 0; i < points.length; i++) {
         markers[i] = new ManipulationMarker(points[i]);
      }

      return markers;
   }
}
