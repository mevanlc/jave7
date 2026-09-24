package de.jave.jave.tool.linealgorithmic;

import static org.junit.Assert.assertEquals;
import de.jave.jave.Point2d;
import org.junit.Test;

public class EndpointSnappingTest {
   @Test
   public void snapsWithinTheContainingCellIncludingBoundaries() {
      assertPoint(3.5, 7.5, LineAlgorithmicTool.snapEndpoint(new Point2d(3.01, 7.99), true));
      assertPoint(4.5, 8.5, LineAlgorithmicTool.snapEndpoint(new Point2d(4.0, 8.0), true));
      assertPoint(-0.5, -2.5, LineAlgorithmicTool.snapEndpoint(new Point2d(-0.01, -2.01), true));
   }

   @Test
   public void preservesRawPositionForReleaseAndKeepsEndpointsIndependent() {
      Point2d origin = new Point2d(3.125, 7.875);
      Point2d destination = new Point2d(19.875, 12.125);
      assertPoint(3.5, 7.5, LineAlgorithmicTool.snapEndpoint(origin, true));
      assertPoint(19.875, 12.125, LineAlgorithmicTool.snapEndpoint(destination, false));
      assertPoint(3.125, 7.875, LineAlgorithmicTool.snapEndpoint(origin, false));
      assertPoint(19.5, 12.5, LineAlgorithmicTool.snapEndpoint(destination, true));
      assertPoint(3.125, 7.875, origin);
      assertPoint(19.875, 12.125, destination);
   }

   @Test
   public void centerDoesNotRoundToAScreenPixelAtOddCellSizes() {
      Point2d center = LineAlgorithmicTool.snapEndpoint(new Point2d(23.0 / 9, 75.0 / 17), true);
      assertEquals(22.5, center.getX() * 9, 0.0);
      assertEquals(76.5, center.getY() * 17, 0.0);
   }

   private static void assertPoint(double x, double y, Point2d actual) {
      assertEquals(x, actual.getX(), 1e-9);
      assertEquals(y, actual.getY(), 1e-9);
   }
}
