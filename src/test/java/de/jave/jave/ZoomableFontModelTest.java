package de.jave.jave;

import static org.junit.Assert.*;

import java.awt.Font;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import org.junit.Test;

public class ZoomableFontModelTest {
   @Test public void resetRestoresConfiguredFontAndDisablesAutoZoom() {
      BooleanModel auto = new BooleanModel(true);
      FontModel font = new FontModel(new Font(Font.MONOSPACED, Font.BOLD, 18));
      ZoomableFontModel model = new ZoomableFontModel(font, 25, auto);
      model.addChangeListener(() -> assertFalse(auto.getValue()));
      model.resetZoom();
      assertEquals(0, model.getSizeDelta());
      assertEquals(font.getFont(), model.getFont());
      model.setAutoZoomDelta(30);
      assertEquals(0, model.getSizeDelta());
      model.zoomOut();
      model.resetZoom();
      assertEquals(font.getFont(), model.getFont());
   }

   @Test public void manualZoomDisablesAutoBeforeExecutingItsStep() {
      BooleanModel auto = new BooleanModel(true);
      ZoomableFontModel model = model(auto);
      model.setAutoZoomDelta(20);
      auto.addChangeListener(() -> assertEquals("disable precedes manual step", 20, model.getSizeDelta()));
      model.addChangeListener(() -> assertFalse("manual font change sees Auto Zoom unchecked", auto.getValue()));
      model.zoomIn();
      assertEquals(21, model.getSizeDelta());
   }

   @Test public void manualZoomOutFromAutoMinimumMovesExactlyOneStep() {
      BooleanModel auto = new BooleanModel(true);
      ZoomableFontModel model = model(auto);
      model.setAutoZoomDelta(-10);
      assertEquals(3, model.getFont().getSize());
      model.zoomOut();
      assertFalse(auto.getValue());
      assertEquals(-11, model.getSizeDelta());
      model.zoomIn();
      assertEquals(-10, model.getSizeDelta());
   }

   @Test public void pendingAutoUpdateCannotUndoManualZoom() {
      BooleanModel auto = new BooleanModel(true);
      ZoomableFontModel model = model(auto);
      model.setAutoZoomDelta(10);
      model.zoomOut();
      model.setAutoZoomDelta(30);
      assertEquals(9, model.getSizeDelta());
      assertFalse(auto.getValue());
   }

   @Test public void repeatedFitDoesNotFireFontChanges() {
      BooleanModel auto = new BooleanModel(true);
      ZoomableFontModel model = model(auto);
      model.setAutoZoomDelta(10);
      model.addChangeListener(() -> fail("unchanged fit must not trigger another layout"));
      model.setAutoZoomDelta(10);
      assertTrue(auto.getValue());
   }

   private ZoomableFontModel model(BooleanModel auto) {
      return new ZoomableFontModel(new FontModel(new Font(Font.MONOSPACED, Font.PLAIN, 13)), 0, auto);
   }
}
