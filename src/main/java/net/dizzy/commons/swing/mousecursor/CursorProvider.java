package net.dizzy.commons.swing.mousecursor;

import java.awt.Cursor;

public final class CursorProvider {
   private static final CursorProvider INSTANCE = new CursorProvider();

   private CursorProvider() {
   }

   public static CursorProvider getInstance() {
      return INSTANCE;
   }

   public Cursor getCursor(CursorId cursorId) {
      if (cursorId == CursorId.TEXT) {
         return Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
      }
      return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
   }
}
