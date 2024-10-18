package net.disy.commons.swing.mousecursor;

import java.awt.Cursor;
import java.awt.Point;

public class CursorInitializer {
   public void initCursorDescriptions(CursorProvider provider) {
      initCustomCursors(provider);
      initSystemCursors(provider);
   }

   private static void initSystemCursors(CursorProvider provider) {
      addSystemCursor(provider, CursorId.CROSSHAIR, 1);
      addSystemCursor(provider, CursorId.DEFAULT, 0);
      addSystemCursor(provider, CursorId.E_RESIZE, 11);
      addSystemCursor(provider, CursorId.HAND, 12);
      addSystemCursor(provider, CursorId.MOVE, 13);
      addSystemCursor(provider, CursorId.N_RESIZE, 8);
      addSystemCursor(provider, CursorId.NE_RESIZE, 7);
      addSystemCursor(provider, CursorId.NW_RESIZE, 6);
      addSystemCursor(provider, CursorId.S_RESIZE, 9);
      addSystemCursor(provider, CursorId.SE_RESIZE, 5);
      addSystemCursor(provider, CursorId.SW_RESIZE, 4);
      addSystemCursor(provider, CursorId.W_RESIZE, 10);
      addSystemCursor(provider, CursorId.WAIT, 3);
   }

   private static void initCustomCursors(CursorProvider provider) {
      provider.addCursorDescriptionSet(createArrowCursorDescriptionSet());
      provider.addCursorDescriptionSet(createArrowPlusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createArrowMinusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createCrosshairSelectionCursorDescriptionSet());
      provider.addCursorDescriptionSet(createCrosshairSelectionPlusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createCrosshairSelectionMinusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createDropperCursorDescriptionSet());
      provider.addCursorDescriptionSet(createHandOpenCursorDescriptionSet());
      provider.addCursorDescriptionSet(createHandClosedCursorDescriptionSet());
      provider.addCursorDescriptionSet(createZoomInCursorDescriptionSet());
      provider.addCursorDescriptionSet(createZoomOutCursorDescriptionSet());
      provider.addCursorDescriptionSet(createZoomCursorDescriptionSet());
      provider.addCursorDescriptionSet(createInfoCursorDescriptionSet());
      provider.addCursorDescriptionSet(createHandPlusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createHandMinusCursorDescriptionSet());
      provider.addCursorDescriptionSet(createTextCursorDescriptionSet());
      provider.addCursorDescriptionSet(createUnsupportedCursorDescriptionSet());
   }

   private static void addSystemCursor(CursorProvider provider, CursorId id, int cursor) {
      ExtendedCursor set = new ExtendedCursor(id);
      set.setFallBackCursor(Cursor.getPredefinedCursor(cursor));
      provider.addCursorDescriptionSet(set);
   }

   private static ExtendedCursor createZoomInCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ZOOM_IN);
      set.setFallBackCursor(Cursor.getPredefinedCursor(1));
      set.addCursorDescription(new CursorDescription("lupe_plus_2_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_plus_2_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_plus_2_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_plus_16_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_plus_16_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_plus_16_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createZoomCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ZOOM);
      set.setFallBackCursor(Cursor.getPredefinedCursor(1));
      set.addCursorDescription(new CursorDescription("lupe_2_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_2_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_2_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_16_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_16_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_16_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createZoomOutCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ZOOM_OUT);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("lupe_minus_2_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_minus_2_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_minus_2_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("lupe_minus_16_16.gif", new Point(5, 5), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_minus_16_32.gif", new Point(5, 5), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("lupe_minus_16_64.gif", new Point(5, 5), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createHandOpenCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.HAND_OPENED);
      set.setFallBackCursor(Cursor.getPredefinedCursor(12));
      set.addCursorDescription(new CursorDescription("hand_opened_2_16.gif", new Point(8, 7), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_opened_2_32.gif", new Point(8, 7), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_opened_2_64.gif", new Point(8, 7), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_opened_16_16.gif", new Point(8, 7), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_opened_16_32.gif", new Point(8, 7), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_opened_16_64.gif", new Point(8, 7), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createHandClosedCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.HAND_CLOSED);
      set.setFallBackCursor(Cursor.getPredefinedCursor(12));
      set.addCursorDescription(new CursorDescription("hand_closed_2_16.gif", new Point(8, 7), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_closed_2_32.gif", new Point(8, 7), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_closed_2_64.gif", new Point(8, 7), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_closed_16_16.gif", new Point(8, 7), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_closed_16_32.gif", new Point(8, 7), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_closed_16_64.gif", new Point(8, 7), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createDropperCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.DROPPER);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("dropper_2_16.gif", new Point(0, 15), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("dropper_2_32.gif", new Point(0, 16), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("dropper_2_64.gif", new Point(0, 16), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("dropper_16_16.gif", new Point(0, 15), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("dropper_16_32.gif", new Point(0, 16), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("dropper_16_64.gif", new Point(0, 16), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createArrowCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ARROW);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("arrow_2_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_2_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_2_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_16_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_16_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_16_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createArrowMinusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ARROW_MINUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("arrow_minus_2_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_minus_2_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_minus_2_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_minus_16_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_minus_16_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_minus_16_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createArrowPlusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.ARROW_PLUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("arrow_plus_2_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_plus_2_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_plus_2_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("arrow_plus_16_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_plus_16_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("arrow_plus_16_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createInfoCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.INFO);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("mouse_info_2_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("mouse_info_2_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("mouse_info_2_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("mouse_info_16_16.gif", new Point(0, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("mouse_info_16_32.gif", new Point(0, 0), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("mouse_info_16_64.gif", new Point(0, 0), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createCrosshairSelectionMinusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.CROSSHAIR_SELECTION_MINUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(1));
      set.addCursorDescription(new CursorDescription("selection_minus_2_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_minus_2_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_minus_2_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_minus_16_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_minus_16_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_minus_16_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createCrosshairSelectionPlusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.CROSSHAIR_SELECTION_PLUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(1));
      set.addCursorDescription(new CursorDescription("selection_plus_2_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_plus_2_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_plus_2_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_plus_16_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_plus_16_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_plus_16_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createCrosshairSelectionCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.CROSSHAIR_SELECTION);
      set.setFallBackCursor(Cursor.getPredefinedCursor(1));
      set.addCursorDescription(new CursorDescription("selection_2_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_2_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_2_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("selection_16_16.gif", new Point(7, 8), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_16_32.gif", new Point(9, 16), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("selection_16_64.gif", new Point(9, 16), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createHandPlusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.HAND_PLUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(12));
      set.addCursorDescription(new CursorDescription("hand_plus_2_16.gif", new Point(4, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_plus_2_32.gif", new Point(5, 2), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_plus_2_64.gif", new Point(5, 2), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_plus_16_16.gif", new Point(4, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_plus_16_32.gif", new Point(5, 2), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_plus_16_64.gif", new Point(5, 2), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createHandMinusCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.HAND_MINUS);
      set.setFallBackCursor(Cursor.getPredefinedCursor(12));
      set.addCursorDescription(new CursorDescription("hand_minus_2_16.gif", new Point(4, 0), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_minus_2_32.gif", new Point(5, 2), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_minus_2_64.gif", new Point(5, 2), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("hand_minus_16_16.gif", new Point(4, 0), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_minus_16_32.gif", new Point(5, 2), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("hand_minus_16_64.gif", new Point(5, 2), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createTextCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.TEXT);
      set.setFallBackCursor(Cursor.getPredefinedCursor(2));
      set.addCursorDescription(new CursorDescription("text_2_16.gif", new Point(5, 7), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("text_2_32.gif", new Point(5, 7), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("text_2_64.gif", new Point(5, 7), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("text_16_16.gif", new Point(5, 8), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("text_16_32.gif", new Point(5, 8), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("text_16_64.gif", new Point(5, 8), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }

   private static ExtendedCursor createUnsupportedCursorDescriptionSet() {
      ExtendedCursor set = new ExtendedCursor(CursorId.UNSUPPORTED_OPERATION);
      set.setFallBackCursor(Cursor.getPredefinedCursor(0));
      set.addCursorDescription(new CursorDescription("unsupported_operation_2_16.gif", new Point(8, 8), CursorCapabilities.DIMENSION_16_DEPTH_2));
      set.addCursorDescription(new CursorDescription("unsupported_operation_2_32.gif", new Point(10, 10), CursorCapabilities.DIMENSION_32_DEPTH_2));
      set.addCursorDescription(new CursorDescription("unsupported_operation_2_64.gif", new Point(10, 10), CursorCapabilities.DIMENSION_64_DEPTH_2));
      set.addCursorDescription(new CursorDescription("unsupported_operation_16_16.gif", new Point(8, 8), CursorCapabilities.DIMENSION_16_DEPTH_16));
      set.addCursorDescription(new CursorDescription("unsupported_operation_16_32.gif", new Point(10, 10), CursorCapabilities.DIMENSION_32_DEPTH_16));
      set.addCursorDescription(new CursorDescription("unsupported_operation_16_64.gif", new Point(10, 10), CursorCapabilities.DIMENSION_64_DEPTH_16));
      return set;
   }
}
