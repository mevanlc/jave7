package de.jave.jave;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class MoveResizeRectangle extends Rectangle {
   protected static final int L1 = 5;
   protected static final int L3 = 8;
   protected static final int L2 = 16;
   public static final int OUTSIDE = 0;
   public static final int INSIDE = 1;
   public static final int NORTH = 2;
   public static final int WEST = 3;
   public static final int SOUTH = 4;
   public static final int EAST = 5;
   public static final int NORTHEAST = 6;
   public static final int NORTHWEST = 7;
   public static final int SOUTHEAST = 8;
   public static final int SOUTHWEST = 9;

   public MoveResizeRectangle(int x, int y, int width, int height) {
      super(x, y, width, height);
   }

   public void paint(Graphics g) {
      g.drawRect(this.x, this.y, this.width, this.height);
      int x0 = this.x + 1;
      int y0 = this.y + 1;
      int x1 = this.x + this.width;
      int y1 = this.y + this.height;
      g.fillRect(x0, y0, 5, 5);
      g.fillRect(x0, y1 - 5, 5, 5);
      g.fillRect(x1 - 5, y0, 5, 5);
      g.fillRect(x1 - 5, y1 - 5, 5, 5);
      g.fillRect((x0 + x1 - 5) / 2, y0, 5, 5);
      g.fillRect((x0 + x1 - 5) / 2, y1 - 5, 5, 5);
      g.fillRect(x0, (y0 + y1 - 5) / 2, 5, 5);
      g.fillRect(x1 - 5, (y0 + y1 - 5) / 2, 5, 5);
   }

   public int getPlace(Point point) {
      if (!this.contains(point)) {
         return 0;
      } else {
         int x0 = this.x;
         int y0 = this.y;
         int x1 = this.x + this.width;
         int y1 = this.y + this.height;
         if (new Rectangle((x0 + x1 - 8) / 2, y0, 8, 8).contains(point)) {
            return 2;
         } else if (new Rectangle((x0 + x1 - 8) / 2, y1 - 8, 8, 8).contains(point)) {
            return 4;
         } else if (new Rectangle(x0, (y0 + y1 - 8) / 2, 8, 8).contains(point)) {
            return 3;
         } else if (new Rectangle(x1 - 8, (y0 + y1 - 8) / 2, 8, 8).contains(point)) {
            return 5;
         } else if (new Rectangle(x0, y1 - 8, 8, 8).contains(point)) {
            return 9;
         } else if (new Rectangle(x1 - 8, y1 - 8, 8, 8).contains(point)) {
            return 8;
         } else if (new Rectangle(x0, y0, 8, 8).contains(point)) {
            return 7;
         } else {
            return new Rectangle(x1 - 8, y0, 8, 8).contains(point) ? 6 : 1;
         }
      }
   }

   public Cursor getCursorFor(Point point) {
      int place = this.getPlace(point);
      if (place == 1) {
         return Cursor.getPredefinedCursor(13);
      } else if (place == 0) {
         return CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION);
      } else if (place == 2) {
         return Cursor.getPredefinedCursor(8);
      } else if (place == 4) {
         return Cursor.getPredefinedCursor(9);
      } else if (place == 3) {
         return Cursor.getPredefinedCursor(10);
      } else if (place == 5) {
         return Cursor.getPredefinedCursor(11);
      } else if (place == 7) {
         return Cursor.getPredefinedCursor(6);
      } else if (place == 6) {
         return Cursor.getPredefinedCursor(7);
      } else if (place == 9) {
         return Cursor.getPredefinedCursor(4);
      } else {
         return place == 8 ? Cursor.getPredefinedCursor(5) : CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION);
      }
   }
}
