package de.jave.jave;

import de.jave.gfx.GfxTools;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class BezierToolGeneric extends GenericTool {
   private Point point1;
   private Point point2;
   private Point point3;
   private Point movingPoint;

   public BezierToolGeneric(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Bezier Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_BEZIER_GENERIC_ICON;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         if (this.point1 == null) {
            this.point1 = point;
            this.movingPoint = this.point1;
            this.point2 = null;
            this.point3 = null;
         } else if (this.point2 == null) {
            this.point2 = point;
            this.movingPoint = this.point2;
            this.point3 = null;
         } else if (this.point3 == null) {
            this.point3 = point;
            this.movingPoint = this.point3;
            this.markPlate = this.createMarkPlate(location);
            pixelPlateOptionsPanel.configure(this.markPlate);
            this.markPlate.setCharacter(this.getMouseChar());
         }

         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.movingPoint != null) {
         this.movingPoint.x = point.x;
         this.movingPoint.y = point.y;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.point1 != null && this.point2 != null && this.point3 != null) {
         this.movingPoint = null;
         this.setMixMode(this.isMix());
         LocatedCharacterPlate result = this.markPlate.convert();
         result.pasteInto(this.getPlate().getContent());
         this.markPlate = null;
         this.point1 = null;
         this.point2 = null;
         this.point3 = null;
         this.getPlate().saveCurrentState("draw bezier");
         this.getPlate().repaint();
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27 && this.point1 != null) {
         this.point1 = null;
         this.point2 = null;
         this.point3 = null;
         this.movingPoint = null;
         this.markPlate = null;
         this.repaintCursor();
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.point1 != null) {
         g.setColor(colorScheme.getColorToolHelping());
         g.drawRect(this.point1.x - 2, this.point1.y - 2, 5, 5);
         if (this.point2 != null) {
            g.drawRect(this.point2.x - 2, this.point2.y - 2, 5, 5);
            GfxTools.drawBrokenLine(g, this.point1.x, this.point1.y, this.point2.x, this.point2.y);
            if (this.point3 != null) {
               int x4 = (4 * this.point3.x - this.point1.x - this.point2.x) / 2;
               int y4 = (4 * this.point3.y - this.point1.y - this.point2.y) / 2;
               Point point4 = new Point(x4, y4);
               GfxTools.drawBrokenLine(g, this.point1.x, this.point1.y, point4.x, point4.y);
               GfxTools.drawBrokenLine(g, this.point2.x, this.point2.y, point4.x, point4.y);
               Point[] bez = new Point[]{this.point1, point4, this.point2};
               Point[] points = BezierAlgorithm.getPolyLineForBezier(bez);

               for (int i = 0; i < points.length - 1; i++) {
                  g.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
               }

               this.markPlate.clear();
               Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
               Point2d p2 = this.getPlate().getRealLocationForScreenPoint(this.point2);
               Point2d p3 = this.getPlate().getRealLocationForScreenPoint(point4);
               BezierAlgorithm.drawBezier(this.markPlate, p1, p3, p2);
               LocatedCharacterPlate result = this.markPlate.convert();
               PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
            }
         }
      }
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.point1 = null;
      this.point2 = null;
      this.point3 = null;
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.point1 = null;
      this.point2 = null;
      this.point3 = null;
   }
}
