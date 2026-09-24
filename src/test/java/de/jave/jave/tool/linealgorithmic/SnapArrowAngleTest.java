package de.jave.jave.tool.linealgorithmic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.jave.jave.Point2d;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.preferences.SmartPreferences;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class SnapArrowAngleTest {
   private static final double EPSILON = 1e-6;

   @Test
   public void snapArrowAngleDefaultsToFalse() {
      LineAlgorithmicOptions options = new LineAlgorithmicOptions();
      assertFalse(options.isSnapArrowAngle());
   }

   @Test
   public void snapArrowAngleFiresChangeEventWhenModified() {
      LineAlgorithmicOptions options = new LineAlgorithmicOptions();
      AtomicInteger changeCount = new AtomicInteger();
      options.addChangeListener(changeCount::incrementAndGet);

      options.setSnapArrowAngle(true);
      assertEquals(1, changeCount.get());
      assertTrue(options.isSnapArrowAngle());

      // Setting same value must not fire change event
      options.setSnapArrowAngle(true);
      assertEquals(1, changeCount.get());

      options.setSnapArrowAngle(false);
      assertEquals(2, changeCount.get());
      assertFalse(options.isSnapArrowAngle());
   }

   @Test
   public void snapArrowAnglePersistsInPreferences() {
      SmartPreferences preferences = new SmartPreferences(new ProbePreferencesFactory().userRoot());

      LineAlgorithmicOptions options = new LineAlgorithmicOptions();
      options.setSnapArrowAngle(true);
      options.saveTo(preferences);

      LineAlgorithmicOptions restored = new LineAlgorithmicOptions();
      restored.loadFrom(preferences);
      assertTrue(restored.isSnapArrowAngle());
   }

   @Test
   public void snapsExactCardinalDirectionsOnCanvas() {
      assertDirection(1.0, 0.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, 0));   // East (0°)
      assertDirection(0.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(0, 10));   // South (90°)
      assertDirection(-1.0, 0.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, 0)); // West (180°)
      assertDirection(0.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(0, -10)); // North (-90° / 270°)
   }

   @Test
   public void snapsExactDiagonalsOnCanvas() {
      assertDirection(1.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, 10));   // SE (45°)
      assertDirection(-1.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, 10)); // SW (135°)
      assertDirection(-1.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, -10)); // NW (-135° / 225°)
      assertDirection(1.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, -10));  // NE (-45° / 315°)
   }

   @Test
   public void snapsArbitraryAnglesToNearest45DegreeStopsOnCanvas() {
      // Near horizontal (shallow angle, ~11.3°) snaps to East
      assertDirection(1.0, 0.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, 2));

      // Near diagonal (~31°) snaps to South-East
      assertDirection(1.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, 6));

      // Near diagonal from the other side (~59°) snaps to South-East
      assertDirection(1.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(6, 10));

      // Near vertical (~78.7°) snaps to South
      assertDirection(0.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(2, 10));

      // Quadrant 2: between South and West
      assertDirection(0.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(-2, 10)); // ~101.3° -> South
      assertDirection(-1.0, 1.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, 7)); // ~145° -> SW
      assertDirection(-1.0, 0.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, 2)); // ~168.7° -> West

      // Quadrant 3 & 4 (negative Y)
      assertDirection(-1.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(-10, -8)); // ~-141° -> NW
      assertDirection(0.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(1, -10));  // ~-84° -> North
      assertDirection(1.0, -1.0, LineAlgorithmicTool.getSnappedCardinalDirection(10, -9));  // ~-42° -> NE
   }

   private static void assertDirection(double expectedX, double expectedY, Point2d actual) {
      assertEquals("X component mismatch", expectedX, actual.getX(), EPSILON);
      assertEquals("Y component mismatch", expectedY, actual.getY(), EPSILON);
   }
}
