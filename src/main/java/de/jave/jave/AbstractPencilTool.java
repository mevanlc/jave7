package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public abstract class AbstractPencilTool extends Tool {
   protected Point cursorLocation;

   public AbstractPencilTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.cursorLocation != null) {
         int[][] brush = this.getBrush();
         if (brush != null) {
            g.setColor(colorScheme.getColorTool());
            this.paintBrushBorder(g, brush, this.cursorLocation.x, this.cursorLocation.y);
         }
      }
   }

   protected abstract int[][] getBrush();

   protected final void paintBrushBorder(Graphics g, int[][] brush, int x0, int y0) {
      int h = brush.length;
      int w = brush[0].length;
      int cx = (w - 1) / 2;
      int cy = (h - 1) / 2;
      int charHeight = this.getPlate().getCharacterMetrics().getHeight();
      int charWidth = this.getPlate().getCharacterMetrics().getWidth();

      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            if (brush[y][x] != ' ' && brush[y][x] != 0) {
               Point p0 = this.getScreenPointFor(x0 - cx + x, y0 - cy + y);
               if (x == 0 || brush[y][x - 1] == ' ' || brush[y][x - 1] == 0) {
                  g.drawLine(p0.x, p0.y, p0.x, p0.y + charHeight);
               }

               if (y == 0 || brush[y - 1][x] == ' ' || brush[y - 1][x] == 0) {
                  g.drawLine(p0.x, p0.y, p0.x + charWidth, p0.y);
               }

               if (x == w - 1 || brush[y][x + 1] == ' ' || brush[y][x + 1] == 0) {
                  g.drawLine(p0.x + charWidth, p0.y, p0.x + charWidth, p0.y + charHeight);
               }

               if (y == h - 1 || brush[y + 1][x] == ' ' || brush[y + 1][x] == 0) {
                  g.drawLine(p0.x, p0.y + charHeight, p0.x + charWidth, p0.y + charHeight);
               }
            }
         }
      }
   }

   protected void paint(Point location) {
      if (location != null) {
         this.paint(location.x, location.y);
      }
   }

   protected abstract void paint(int var1, int var2);

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.cursorLocation = location;
         this.paint(location);
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         if (this.cursorLocation == null || !this.cursorLocation.equals(location)) {
            if (this.cursorLocation == null) {
               this.paint(location);
            } else {
               this.paintLineBresenham(this.cursorLocation.x, this.cursorLocation.y, location.x, location.y);
            }

            this.cursorLocation = location;
            this.repaintCursor();
            this.showCoordinates(location);
         }
      }
   }

   protected void paintLineBresenham(int x1, int y1, int x2, int y2) {
      int x = x1;
      int y = y1;
      int d = 0;
      int hx = x2 - x1;
      int hy = y2 - y1;
      int xInc = 1;
      int yInc = 1;
      if (hx < 0) {
         xInc = -1;
         hx = -hx;
      }

      if (hy < 0) {
         yInc = -1;
         hy = -hy;
      }

      if (hy <= hx) {
         int c = 2 * hx;
         int m = 2 * hy;

         while (true) {
            this.paint(x, y);
            if (x == x2) {
               break;
            }

            x += xInc;
            d += m;
            if (d > hx) {
               y += yInc;
               d -= c;
            }
         }
      } else {
         int c = 2 * hy;
         int m = 2 * hx;

         while (true) {
            this.paint(x, y);
            if (y == y2) {
               break;
            }

            y += yInc;
            d += m;
            if (d > hy) {
               x += xInc;
               d -= c;
            }
         }
      }
   }

   @Override
   public void mouseMoved(Point point, Point location, MouseEvent evt) {
      super.mouseMoved(point, location, evt);
      if (this.cursorLocation == null || !this.cursorLocation.equals(location)) {
         this.cursorLocation = location;
         this.repaintCursor();
      }
   }

   protected abstract String getUndoRedoActionName();

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      this.getPlate().saveCurrentState(this.getUndoRedoActionName());
   }

   @Override
   public void mouseExited(Point point, Point location, MouseEvent evt) {
      super.mouseExited(point, location, evt);
      this.cursorLocation = null;
      this.repaintCursor();
   }
}
