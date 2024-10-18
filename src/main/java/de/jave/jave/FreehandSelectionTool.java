package de.jave.jave;

import de.jave.gfx.GfxTools;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.area.BooleanArea;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.Icon;

public class FreehandSelectionTool extends SelectionTool {
   private Polygon polygon;

   public FreehandSelectionTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Freehand Selection";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_FREEHAND_SELECTION_ICON;
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.polygon != null) {
         g.setColor(colorScheme.getColorTool());
         int[] xs = this.polygon.xpoints;
         int[] ys = this.polygon.ypoints;
         int n = this.polygon.npoints;
         int dx = this.getPlate().getCharWidth() / 2;
         int dy = this.getPlate().getCharHeight() / 2;

         for (int i = 0; i < n - 1; i++) {
            Point p1 = this.getPlate().getScreenPointFor(xs[i], ys[i]);
            Point p2 = this.getPlate().getScreenPointFor(xs[i + 1], ys[i + 1]);
            g.drawLine(p1.x + dx, p1.y + dy, p2.x + dx, p2.y + dy);
         }

         Point p1 = this.getPlate().getScreenPointFor(xs[0], ys[0]);
         Point p2 = this.getPlate().getScreenPointFor(xs[n - 1], ys[n - 1]);
         GfxTools.drawBrokenLine(g, p1.x + dx, p1.y + dy, p2.x + dx, p2.y + dy);
      }
   }

   @Override
   protected void selectionMousePressedStarted(Point location) {
      if (location != null) {
         this.location1 = location;
         this.polygon = new Polygon();
         this.polygon.addPoint(location.x, location.y);
      }
   }

   @Override
   protected void selectionMouseDragged(Point location) {
      if (this.polygon != null && location != null && !location.equals(this.location2)) {
         this.polygon.addPoint(location.x, location.y);
         this.repaintCursor();
      }
   }

   @Override
   protected void selectionMouseReleasedFinished(Point location) {
      if (this.polygon != null && location != null) {
         this.polygon.addPoint(location.x, location.y);
         this.markPlate = this.createMarkPlate(location);
         this.markPlate.setMode(PixelPlateMode.CHAR);
         this.markPlate.set(location.x, location.y);
         this.markPlate.setCharacter('O');
         Rectangle bounds = this.polygon.getBounds();

         for (int y = 0; y < bounds.height; y++) {
            for (int x = 0; x < bounds.width; x++) {
               if (this.polygon.contains(bounds.x + x, bounds.y + y)) {
                  this.markPlate.set(bounds.x + x, bounds.y + y);
               }
            }
         }

         int[] xs = this.polygon.xpoints;
         int[] ys = this.polygon.ypoints;
         int n = this.polygon.npoints;

         for (int i = 0; i < n; i++) {
            this.markPlate.drawLine(xs[i], ys[i], xs[(i + 1) % n], ys[(i + 1) % n]);
         }

         bounds.width++;
         bounds.height++;
         BooleanArea mask = new BooleanArea(bounds.width, bounds.height);

         for (int y = 0; y < bounds.height; y++) {
            for (int xx = 0; xx < bounds.width; xx++) {
               if (this.getPlate().getContent().contains(bounds.x + xx, bounds.y + y) && this.markPlate.isSet(bounds.x + xx, bounds.y + y)) {
                  mask.set(xx, y, true);
               }
            }
         }

         this.markPlate = null;
         if (this.mode == 10) {
            this.getPlate().getSelection().set(bounds, mask);
            this.application.updateSelectionMenu();
            this.synchronizeToSelection();
            this.repaintAll();
            this.getPlate().saveCurrentState("select");
         } else if (this.mode == 11) {
            boolean success = this.getPlate().getSelection().add(bounds, mask);
            if (!success) {
               this.repaintCursor();
            } else {
               this.repaintAll();
               this.getPlate().saveCurrentState("modify selection");
            }
         } else if (this.mode == 12) {
            boolean success = this.getPlate().getSelection().remove(bounds, mask);
            if (!success) {
               this.repaintCursor();
            } else {
               this.repaintAll();
               if (this.getPlate().hasSelection()) {
                  this.getPlate().saveCurrentState("modify selection");
               } else {
                  this.getPlate().saveCurrentState("drop selection");
                  this.application.switchToTextTool();
               }
            }
         }

         this.location1 = null;
         this.polygon = null;
         this.mode = 1;
         this.repaintAll();
      }
   }

   @Override
   protected void selectionCanceled() {
      this.location1 = null;
      this.polygon = null;
      this.mode = 1;
      this.repaintCursor();
   }
}
