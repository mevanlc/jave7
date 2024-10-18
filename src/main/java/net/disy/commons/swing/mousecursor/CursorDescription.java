package net.disy.commons.swing.mousecursor;

import java.awt.Point;

public class CursorDescription {
   private final CursorCapabilities capabilities;
   private final String fileName;
   private final Point hotSpot;

   public CursorDescription(String fileName, Point hotSpot, CursorCapabilities capabilities) {
      this.fileName = fileName;
      this.hotSpot = hotSpot;
      this.capabilities = capabilities;
   }

   public String getFileName() {
      return this.fileName;
   }

   public Point getHotSpot() {
      return this.hotSpot;
   }

   public CursorCapabilities getCapabilities() {
      return this.capabilities;
   }
}
