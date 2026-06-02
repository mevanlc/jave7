package de.jave.jave;

import de.jave.gfx.GfxTools;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateArcRenderer;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class ArcToolGeneric extends GenericTool {
   private Point point1;
   private Point point2;
   private Point point3;

   public ArcToolGeneric(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Arc Generic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_ARC_GENERIC_ICON;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         if (this.point1 == null) {
            this.point1 = point;
            this.point2 = null;
            this.point3 = null;
         } else if (this.point2 == null) {
            this.point2 = point;
            this.point3 = null;
         } else if (this.point3 == null) {
            this.point3 = point;
            this.markPlate = this.createMarkPlate(location);
            this.pixelPlateModel.configure(this.markPlate);
            this.markPlate.setCharacter(this.getMouseChar());
         }

         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.point3 != null || this.point2 != null || this.point1 != null) {
         if (this.point2 == null) {
            this.point1 = point;
            this.repaintCursor();
         } else if (this.point3 == null) {
            this.point2 = point;
            this.repaintCursor();
         } else {
            this.point3 = point;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.point1 != null && this.point2 != null && this.point3 != null) {
         this.point1 = null;
         this.point2 = null;
         this.point3 = null;
         if (this.markPlate != null) {
            this.setMixMode(this.isMix());
            LocatedCharacterPlate result = this.markPlate.convert();
            result.pasteInto(this.getPlate().getContent());
            this.saveCurrentState("draw arc");
            this.repaintAll();
            this.markPlate = null;
         }
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27 && this.point1 != null) {
         this.point1 = null;
         this.point2 = null;
         this.point3 = null;
         this.markPlate = null;
         this.repaintCursor();
      }
   }

   private static final double getAngle(double dx, double dy) {
      if (dx == 0.0) {
         return dy > 0.0 ? 270.0 : 90.0;
      } else {
         double alpha = Math.atan(dy / dx);
         return dx > 0.0 ? (360.0 - alpha * 180.0 / Math.PI) % 360.0 : 180.0 - alpha * 180.0 / Math.PI;
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.point1 != null) {
         g.setColor(colorScheme.getColorToolHelping());
         this.drawPoint(g, this.point1);
         if (this.point2 != null) {
            this.drawPoint(g, this.point2);
            if (this.point3 != null) {
               Point2d center = ArcAgorithm.getCenter(this.point1, this.point2, this.point3);
               if (center == null) {
                  int minX = this.point1.x < this.point2.x ? this.point1.x : this.point2.x;
                  int minY = this.point1.y < this.point2.y ? this.point1.y : this.point2.y;
                  int maxX = this.point1.x > this.point2.x ? this.point1.x : this.point2.x;
                  int maxY = this.point1.y > this.point2.y ? this.point1.y : this.point2.y;
                  if (this.point3.x >= minX && this.point3.x <= maxX && this.point3.y >= minY && this.point3.y <= maxY) {
                     g.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y);
                     this.markPlate.clear();
                     Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
                     Point2d p2 = this.getPlate().getRealLocationForScreenPoint(this.point2);
                     this.markPlate.drawLine(p1, p2);
                     LocatedCharacterPlate result = this.markPlate.convert();
                     PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
                  }
               } else {
                  double r = Math.sqrt(Math.pow((double)this.point1.x - center.getX(), 2.0) + Math.pow((double)this.point1.y - center.getY(), 2.0));
                  int radius = (int)Math.round(r);
                  double angle1 = getAngle((double)this.point1.x - center.getX(), (double)this.point1.y - center.getY());
                  double angle2 = getAngle((double)this.point2.x - center.getX(), (double)this.point2.y - center.getY());
                  double angle3 = getAngle((double)this.point3.x - center.getX(), (double)this.point3.y - center.getY());
                  double alpha = (angle2 - angle1) / 180.0 * Math.PI;
                  double h = Math.abs(r * Math.cos(alpha / 2.0));
                  if (r - h < 1.0) {
                     int minX = this.point1.x < this.point2.x ? this.point1.x : this.point2.x;
                     int minY = this.point1.y < this.point2.y ? this.point1.y : this.point2.y;
                     int maxX = this.point1.x > this.point2.x ? this.point1.x : this.point2.x;
                     int maxY = this.point1.y > this.point2.y ? this.point1.y : this.point2.y;
                     if (this.point3.x >= minX && this.point3.x <= maxX && this.point3.y >= minY && this.point3.y <= maxY) {
                        g.drawLine(this.point1.x, this.point1.y, this.point2.x, this.point2.y);
                        this.markPlate.clear();
                        Point2d p1 = this.getPlate().getRealLocationForScreenPoint(this.point1);
                        Point2d p2 = this.getPlate().getRealLocationForScreenPoint(this.point2);
                        this.markPlate.drawLine(p1, p2);
                        LocatedCharacterPlate result = this.markPlate.convert();
                        PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
                        return;
                     }

                     alpha = (angle3 - angle2) / 180.0 * Math.PI;
                     h = Math.abs(r * Math.cos(alpha / 2.0));
                     if (r - h < 0.5) {
                        return;
                     }
                  }

                  Rectangle clip = g.getClipBounds();
                  Point pp1 = new Point((int)center.getX(), (int)center.getY());
                  Point pp2 = new Point(this.point1.x, this.point1.y);
                  if (GfxTools.clipLine(pp1, pp2, clip)) {
                     GfxTools.drawBrokenLine(g, pp1.x, pp1.y, pp2.x, pp2.y);
                  }

                  pp1 = new Point((int)center.getX(), (int)center.getY());
                  pp2 = new Point(this.point2.x, this.point2.y);
                  if (GfxTools.clipLine(pp1, pp2, clip)) {
                     GfxTools.drawBrokenLine(g, pp1.x, pp1.y, pp2.x, pp2.y);
                  }

                  if (angle2 < angle1) {
                     angle2 += 360.0;
                  }

                  if (angle3 < angle1) {
                     angle3 += 360.0;
                  }

                  this.markPlate.clear();
                  Point2d pCenter = this.getPlate().getRealLocationForScreenPoint(new Point((int)center.getX(), (int)center.getY()));
                  Point2d rp = this.getPlate().getRealLocationForScreenPoint(new Point((int)center.getX() + radius, (int)center.getY() + radius));
                  double rX = rp.getX() - pCenter.getX();
                  double rY = rp.getY() - pCenter.getY();
                  boolean b = angle3 <= angle2 && angle3 >= angle1;
                  PixelPlateArcRenderer renderer = new PixelPlateArcRenderer(this.markPlate);
                  if (b) {
                     GfxTools.drawArc(g, (int)center.getX(), (int)center.getY(), radius, angle1, angle2 - angle1);
                     renderer.drawArc(pCenter, rX, rY, angle1, angle2 - angle1);
                  } else {
                     GfxTools.drawArc(g, (int)center.getX(), (int)center.getY(), radius, angle2, 360.0 - (angle2 - angle1));
                     renderer.drawArc(pCenter, rX, rY, angle2, 360.0 - (angle2 - angle1));
                  }

                  LocatedCharacterPlate result = this.markPlate.convert();
                  PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
               }
            }
         }
      }
   }

   private void drawPoint(Graphics2D g, Point p) {
      g.drawRect(p.x - 2, p.y - 2, 5, 5);
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
