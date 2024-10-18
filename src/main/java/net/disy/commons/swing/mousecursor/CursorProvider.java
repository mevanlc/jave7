package net.disy.commons.swing.mousecursor;

import java.awt.Cursor;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class CursorProvider {
   private CursorCapabilities cursorCapabilities;
   private static CursorProvider instance = new CursorProvider();
   private final Map<CursorId, ExtendedCursor> cursorDescriptions = new HashMap<>();

   private CursorProvider() {
   }

   public static CursorProvider getInstance() {
      return instance;
   }

   public void addCursorDescriptionSet(ExtendedCursor set) {
      if (this.cursorDescriptions.containsKey(set.getCursorId())) {
         throw new IllegalArgumentException("Cursor for id " + set.getCursorId() + " already registered");
      } else {
         this.cursorDescriptions.put(set.getCursorId(), set);
      }
   }

   public Cursor getCursor(CursorId cursorId) {
      ExtendedCursor set = this.getExtendedCursor(cursorId);
      return set == null ? Cursor.getDefaultCursor() : set.getCursor();
   }

   public Image getCursorImage(CursorId cursorId) {
      ExtendedCursor set = this.getExtendedCursor(cursorId);
      return set == null ? null : set.getCursorImage();
   }

   public ExtendedCursor getExtendedCursor(CursorId cursorId) {
      ExtendedCursor extendedCursor = this.cursorDescriptions.get(cursorId);
      if (extendedCursor == null) {
         return null;
      } else {
         if (!extendedCursor.isInitialized()) {
            extendedCursor.initialize(this.getCursorCapabilities());
         }

         return extendedCursor;
      }
   }

   private CursorCapabilities getCursorCapabilities() {
      if (this.cursorCapabilities == null) {
         this.cursorCapabilities = CursorCapabilities.getSystemCapabilities();
      }

      return this.cursorCapabilities;
   }

   public void setCursorCapabilities(CursorCapabilities cursorCapabilities) {
      this.cursorCapabilities = cursorCapabilities;
   }

   static {
      new CursorInitializer().initCursorDescriptions(getInstance());
   }
}
