package de.jave.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Vector;

public class GfxUtilities {
   protected static final int TOP = 8;
   protected static final int BOTTOM = 4;
   protected static final int LEFT = 2;
   protected static final int RIGHT = 1;

   private GfxUtilities() {
   }

   public static double distance(Point p1, Point p2) {
      return Math.sqrt(Math.pow(p1.x - p2.x, 2.0) + Math.pow(p1.y - p2.y, 2.0));
   }

   public static double distance2(Point p1, Point p2) {
      return Math.pow(p1.x - p2.x, 2.0) + Math.pow(p1.y - p2.y, 2.0);
   }

   public static void drawDottedRectangle(Graphics g, Color color1, Color color2, Rectangle r) {
      drawDottedRectangle(g, color1, color2, r.x, r.y, r.width, r.height);
   }

   public static void drawDottedRectangle(Graphics g, Color color1, Color color2, int x, int y, int width, int height) {
      g.setColor(color1);
      g.drawRect(x, y, width, height);
      g.setColor(color2);
      drawDottedRectangle(g, x, y, width, height);
   }

   public static void drawDottedRectangle(Graphics g, Rectangle r) {
      drawDottedRectangle(g, r.x, r.y, r.width, r.height);
   }

   public static void drawDottedRectangle(Graphics g, int x, int y, int width, int height) {
      drawBrokenHorizontalLine(g, x, x + width, y, 1, 0);
      drawBrokenHorizontalLine(g, x, x + width, y + height, 1, 0);
      drawBrokenVerticalLine(g, x, y, y + height, 1, 0);
      drawBrokenVerticalLine(g, x + width, y, y + height, 1, 0);
   }

   public static void drawDottedLine(Graphics g, int x1, int y1, int x2, int y2) {
      drawBrokenLine(g, x1, y1, x2, y2, 1);
   }

   public static void drawDottedLine(Graphics g, Point p1, Point p2) {
      drawBrokenLine(g, p1, p2, 1);
   }

   public static void drawBrokenLine(Graphics g, Point p1, Point p2, int strokeLength) {
      drawBrokenLine(g, p1.x, p1.y, p2.x, p2.y, strokeLength);
   }

   public static void drawBrokenLine(Graphics g, int x1, int y1, int x2, int y2, int strokeLength) {
      drawBrokenLine(g, x1, y1, x2, y2, strokeLength, 0);
   }

   public static void drawBrokenLine(Graphics g, int x1, int y1, int x2, int y2, int strokeLength, int offset) {
      if (y2 == y1) {
         drawBrokenHorizontalLine(g, x1, x2, y1, strokeLength, offset);
      } else if (x2 == x1) {
         drawBrokenVerticalLine(g, x1, y1, y2, strokeLength, offset);
      } else {
         double length = Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0));
         double dx = (double)(x2 - x1) / length * (double)strokeLength;
         double dy = (double)(y2 - y1) / length * (double)strokeLength;
         double cx = dx;
         double cy = dy;
         if (dx > 0.0) {
            cx = dx - 1.0;
         }

         if (dy > 0.0) {
            cy = dy - 1.0;
         }

         if (cx < 0.0) {
            cx++;
         }

         if (cy < 0.0) {
            cy++;
         }

         double l2 = length * length;

         for (double i = (double)offset / (double)strokeLength; Math.pow(i * dx + cx, 2.0) + Math.pow(i * dy + cy, 2.0) <= l2; i += 2.0) {
            g.drawLine(x1 + (int)(i * dx), y1 + (int)(i * dy), x1 + (int)(i * dx + cx), y1 + (int)(i * dy + cy));
         }
      }
   }

   public static void drawBrokenHorizontalLine(Graphics g, int x1, int x2, int y1, int strokeLength, int offset) {
      int count = 0;
      int x = 0;

      for (x = x1; x <= x2; x++) {
         int pos = (x + strokeLength * 2 - offset) % (strokeLength * 2);
         if (pos < strokeLength) {
            count++;
         } else if (count > 0) {
            g.drawLine(x - count, y1, x - 1, y1);
            count = 0;
         }
      }

      if (count > 0) {
         g.drawLine(x - count, y1, x - 1, y1);
      }
   }

   public static void drawBrokenVerticalLine(Graphics g, int x1, int y1, int y2, int strokeLength, int offset) {
      int count = 0;
      int y = 0;

      for (y = y1; y <= y2; y++) {
         int pos = (y + strokeLength * 2 - offset) % (strokeLength * 2);
         if (pos < strokeLength) {
            count++;
         } else if (count > 0) {
            g.drawLine(x1, y - count, x1, y - 1);
            count = 0;
         }
      }

      if (count > 0) {
         g.drawLine(x1, y - count, x1, y - 1);
      }
   }

   public static void drawBrokenRectangle(Graphics g, Color color1, Color color2, Rectangle r) {
      drawBrokenRectangle(g, color1, color2, r.x, r.y, r.width, r.height);
   }

   public static void drawBrokenRectangle(Graphics g, Color color1, Color color2, int x, int y, int width, int height) {
      drawBrokenRectangle(g, color1, color2, x, y, width, height, 0);
   }

   public static void drawBrokenRectangle(Graphics g, Color color1, Color color2, int x, int y, int width, int height, int offset) {
      g.setColor(color1);
      g.drawRect(x, y, width, height);
      g.setColor(color2);
      drawBrokenRectangle(g, x, y, width, height, offset);
   }

   public static void drawBrokenRectangle(Graphics g, Rectangle r) {
      drawBrokenRectangle(g, r.x, r.y, r.width, r.height);
   }

   public static void drawBrokenRectangle(Graphics g, int x, int y, int width, int height) {
      drawBrokenRectangle(g, x, y, width, height, 0);
   }

   public static void drawBrokenRectangle(Graphics g, int x, int y, int width, int height, int offset) {
      drawBrokenHorizontalLine(g, x, x + width, y, 3, offset);
      drawBrokenHorizontalLine(g, x, x + width, y + height, 3, offset);
      drawBrokenVerticalLine(g, x, y, y + height, 3, offset);
      drawBrokenVerticalLine(g, x + width, y, y + height, 3, offset);
   }

   public static void drawBrokenLine(Graphics g, Point p1, Point p2) {
      drawBrokenLine(g, p1, p2, 3);
   }

   public static void drawBrokenLine(Graphics g, int x1, int y1, int x2, int y2) {
      drawBrokenLine(g, x1, y1, x2, y2, 3);
   }

   public static void paintArrow(Graphics g, Point p1, Point p2) {
      paintArrow(g, p1.x, p1.y, p2.x, p2.y);
   }

   public static void paintArrow(Graphics g, int x1, int y1, int x2, int y2) {
      double vx = x1 - x2;
      double vy = y1 - y2;
      double scale = Math.sqrt(vx * vx + vy * vy);
      vx /= scale;
      vy /= scale;
      double nx = -vy * 3.0;
      double ny = vx * 3.0;
      vx *= 8.0;
      vy *= 8.0;
      g.drawLine(x2, y2, x2 + (int)(vx + nx), y2 + (int)(vy + ny));
      g.drawLine(x2, y2, x2 + (int)(vx - nx), y2 + (int)(vy - ny));
   }

   public static void paintArrowFilled(Graphics g, Point p1, Point p2) {
      paintArrowFilled(g, p1.x, p1.y, p2.x, p2.y);
   }

   public static void paintArrowFilled(Graphics g, int x1, int y1, int x2, int y2) {
      double vx = x1 - x2;
      double vy = y1 - y2;
      double scale = Math.sqrt(vx * vx + vy * vy);
      vx /= scale;
      vy /= scale;
      double nx = -vy * 4.0;
      double ny = vx * 4.0;
      vx *= 10.0;
      vy *= 10.0;
      Polygon pol = new Polygon();
      pol.addPoint(x2, y2);
      pol.addPoint(x2 + (int)(vx + nx), y2 + (int)(vy + ny));
      pol.addPoint(x2 + (int)(vx - nx), y2 + (int)(vy - ny));
      g.fillPolygon(pol);
   }

   public static void paintArrowInheritance(Graphics g, Point p1, Point p2) {
      double vx = p1.x - p2.x;
      double vy = p1.y - p2.y;
      double scale = Math.sqrt(vx * vx + vy * vy);
      vx /= scale;
      vy /= scale;
      double nx = -vy * 7.0;
      double ny = vx * 7.0;
      vx *= 14.0;
      vy *= 14.0;
      int d1x = (int)(vx + nx) + p2.x;
      int d1y = (int)(vy + ny) + p2.y;
      int d2x = (int)(vx - nx) + p2.x;
      int d2y = (int)(vy - ny) + p2.y;
      g.drawLine(p2.x, p2.y, d1x, d1y);
      g.drawLine(p2.x, p2.y, d2x, d2y);
      g.drawLine(d1x, d1y, d2x, d2y);
      g.drawLine(p2.x + (int)vx, p2.y + (int)vy, p1.x, p1.y);
   }

   public static void paintArrowImplementation(Graphics g, Point p1, Point p2) {
      double vx = p1.x - p2.x;
      double vy = p1.y - p2.y;
      double scale = Math.sqrt(vx * vx + vy * vy);
      vx /= scale;
      vy /= scale;
      double nx = -vy * 7.0;
      double ny = vx * 7.0;
      vx *= 14.0;
      vy *= 14.0;
      g.drawLine(p2.x, p2.y, (int)(vx + nx) + p2.x, (int)(vy + ny) + p2.y);
      g.drawLine(p2.x, p2.y, (int)(vx - nx) + p2.x, (int)(vy - ny) + p2.y);
      drawBrokenLine(g, new Point(p2.x + (int)(vx / 7.0), p2.y + (int)(vy / 7.0)), p1, 5);
   }

   public static void quickSortDepth(Vector r) {
      qSort(r, 0, r.size() - 1);
   }

   private static void qSort(Vector a, int lo0, int hi0) {
      int lo = lo0;
      int hi = hi0;
      if (hi0 > lo0) {
         DepthComparable mid = (DepthComparable)a.elementAt((lo0 + hi0) / 2);

         while (lo <= hi) {
            while (lo < hi0 && ((DepthComparable)a.elementAt(lo)).compareDepthTo(mid) > 0) {
               lo++;
            }

            while (hi > lo0 && ((DepthComparable)a.elementAt(hi)).compareDepthTo(mid) < 0) {
               hi--;
            }

            if (lo <= hi) {
               DepthComparable cache = (DepthComparable)a.elementAt(lo);
               a.setElementAt(a.elementAt(hi), lo);
               a.setElementAt(cache, hi);
               cache = null;
               lo++;
               hi--;
            }
         }

         if (lo0 < hi) {
            qSort(a, lo0, hi);
         }

         if (lo < hi0) {
            qSort(a, lo, hi0);
         }
      }
   }

   public static final boolean clipLine(Point p1, Point p2, Rectangle clip) {
      int yMin = clip.y;
      int yMax = clip.y + clip.height;
      int xMin = clip.x;
      int xMax = clip.x + clip.width;
      int i1 = computeOutCode(p1, xMin, xMax, yMin, yMax);
      int i2 = computeOutCode(p2, xMin, xMax, yMin, yMax);
      int x = 0;
      int y = 0;

      while (i1 != 0 || i2 != 0) {
         if ((i1 & i2) != 0) {
            return false;
         }

         int out = i1 != 0 ? i1 : i2;
         if ((out & 8) > 0) {
            x = p1.x + (p2.x - p1.x) * (yMin - p1.y) / (p2.y - p1.y);
            y = yMin;
         } else if ((out & 4) > 0) {
            x = p1.x + (p2.x - p1.x) * (yMax - p1.y) / (p2.y - p1.y);
            y = yMax;
         } else if ((out & 1) > 0) {
            y = p1.y + (p2.y - p1.y) * (xMax - p1.x) / (p2.x - p1.x);
            x = xMax;
         } else if ((out & 2) > 0) {
            y = p1.y + (p2.y - p1.y) * (xMin - p1.x) / (p2.x - p1.x);
            x = xMin;
         }

         if (out == i1) {
            p1.x = x;
            p1.y = y;
            i1 = computeOutCode(p1, xMin, xMax, yMin, yMax);
         } else {
            p2.x = x;
            p2.y = y;
            i2 = computeOutCode(p2, xMin, xMax, yMin, yMax);
         }
      }

      return true;
   }

   protected static final int computeOutCode(Point p, int xMin, int xMax, int yMin, int yMax) {
      int out = 0;
      if (p.y < yMin) {
         out = 8;
      } else if (p.y > yMax) {
         out = 4;
      }

      if (p.x < xMin) {
         out += 2;
      } else if (p.x > xMax) {
         out++;
      }

      return out;
   }

   public static final void drawArc(Graphics g, int oX, int oY, int radius, double angle, double arcAngle) {
      drawArc(g, oX, oY, radius, radius, angle, arcAngle);
   }

   public static final void drawArc(Graphics g, int oX, int oY, int radiusX, int radiusY, double angle, double arcAngle) {
      if ((double)radiusX != 0.0 && (double)radiusY != 0.0) {
         if (arcAngle < 0.0) {
            angle += arcAngle;
            arcAngle = -arcAngle;
         }

         angle %= 360.0;
         if (angle + arcAngle >= 270.0 && angle <= 360.0 || angle + arcAngle >= 630.0 && angle <= 720.0) {
            double startAngle = angle - 270.0;
            double endAngle = (angle + arcAngle) % 360.0;
            if (endAngle < 270.0) {
               endAngle = 90.0;
            } else {
               endAngle %= 270.0;
            }

            if (startAngle < 0.0) {
               startAngle = 0.0;
            }

            if (endAngle < 0.0) {
               endAngle = 0.0;
            }

            if (startAngle > 90.0) {
               startAngle = 90.0;
            }

            if (endAngle > 90.0) {
               endAngle = 90.0;
            }

            if (endAngle < startAngle) {
               drawArcSE(g, oX, oY, radiusX, radiusY, 0.0, endAngle);
               drawArcSE(g, oX, oY, radiusX, radiusY, startAngle, 90.0);
            } else {
               drawArcSE(g, oX, oY, radiusX, radiusY, startAngle, endAngle);
            }
         }

         if (angle + arcAngle >= 180.0 && angle <= 270.0 || angle + arcAngle >= 540.0 && angle <= 630.0) {
            double startAnglex = angle - 180.0;
            double endAnglex = (angle + arcAngle) % 360.0;
            if (endAnglex < 180.0) {
               endAnglex = 90.0;
            } else {
               endAnglex %= 180.0;
            }

            if (startAnglex < 0.0 || startAnglex > 90.0) {
               startAnglex = 0.0;
            }

            if (endAnglex < 0.0) {
               endAnglex = 0.0;
            }

            if (endAnglex > 90.0) {
               endAnglex = 90.0;
            }

            if (endAnglex < startAnglex) {
               drawArcSW(g, oX, oY, radiusX, radiusY, 0.0, endAnglex);
               drawArcSW(g, oX, oY, radiusX, radiusY, startAnglex, 90.0);
            } else {
               drawArcSW(g, oX, oY, radiusX, radiusY, startAnglex, endAnglex);
            }
         }

         if (angle + arcAngle >= 90.0 && angle <= 180.0 || angle + arcAngle >= 450.0 && angle <= 540.0) {
            double startAnglexx = angle - 90.0;
            if (angle < 90.0) {
               startAnglexx = 0.0;
            } else if (angle > 180.0) {
               startAnglexx = 0.0;
            }

            double endAnglexx = (angle + arcAngle) % 360.0;
            if (endAnglexx > 180.0) {
               endAnglexx = 90.0;
            } else if (endAnglexx < 90.0) {
               endAnglexx = 90.0;
            } else {
               endAnglexx %= 90.0;
            }

            if (endAnglexx < startAnglexx) {
               drawArcNW(g, oX, oY, radiusX, radiusY, 0.0, endAnglexx);
               drawArcNW(g, oX, oY, radiusX, radiusY, startAnglexx, 90.0);
            } else {
               drawArcNW(g, oX, oY, radiusX, radiusY, startAnglexx, endAnglexx);
            }
         }

         if (angle + arcAngle >= 0.0 && angle <= 90.0 || angle + arcAngle >= 360.0 && angle <= 450.0) {
            double startAnglexxx = angle;
            double endAnglexxx = (angle + arcAngle) % 360.0;
            if (angle > 90.0) {
               startAnglexxx = 0.0;
            }

            if (endAnglexxx > 90.0) {
               endAnglexxx = 90.0;
            }

            if (endAnglexxx < startAnglexxx) {
               drawArcNE(g, oX, oY, radiusX, radiusY, 0.0, endAnglexxx);
               drawArcNE(g, oX, oY, radiusX, radiusY, startAnglexxx, 90.0);
            } else {
               drawArcNE(g, oX, oY, radiusX, radiusY, startAnglexxx, endAnglexxx);
            }
         }
      } else {
         set(g, oX, oY, oX, oX, oY, oY);
      }
   }

   protected static final void drawArcSE(Graphics g, int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
      int minX = oX;
      int maxX = oX + radiusX;
      int minY = oY;
      int maxY = oY + radiusY;
      if (startAngle > 0.0) {
         minX = oX + (int)((double)radiusX * Math.sin(startAngle / 180.0 * Math.PI));
         maxY = oY + (int)((double)radiusY * Math.cos(startAngle / 180.0 * Math.PI));
      }

      if (endAngle < 90.0) {
         maxX = oX + (int)((double)radiusX * Math.sin(endAngle / 180.0 * Math.PI));
         minY = oY + (int)((double)radiusY * Math.cos(endAngle / 180.0 * Math.PI));
      }

      Rectangle clip = g.getClipBounds();
      if (clip.x > minX) {
         minX = clip.x;
      }

      if (clip.x + clip.width < maxX) {
         maxX = clip.x + clip.width;
      }

      if (clip.y > minY) {
         minY = clip.y;
      }

      if (clip.y + clip.height < maxY) {
         maxY = clip.y + clip.height;
      }

      if (maxX >= minX && maxY >= minY) {
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX + x, oY + y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX + x, oY + y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX + x, oY, minX, maxX, minY, maxY);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX + y, oY + x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX + y, oY + x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX, oY + x, minX, maxX, minY, maxY);
         }
      }
   }

   protected static final void drawArcSW(Graphics g, int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
      int minX = oX - radiusX;
      int maxX = oX;
      int minY = oY;
      int maxY = oY + radiusY;
      if (startAngle > 0.0) {
         minX = oX - (int)((double)radiusX * Math.cos(startAngle / 180.0 * Math.PI));
         minY = oY + (int)((double)radiusY * Math.sin(startAngle / 180.0 * Math.PI));
      }

      if (endAngle < 90.0) {
         maxX = oX - (int)((double)radiusX * Math.cos(endAngle / 180.0 * Math.PI));
         maxY = oY + (int)((double)radiusY * Math.sin(endAngle / 180.0 * Math.PI));
      }

      Rectangle clip = g.getClipBounds();
      if (clip.x > minX) {
         minX = clip.x;
      }

      if (clip.x + clip.width < maxX) {
         maxX = clip.x + clip.width;
      }

      if (clip.y > minY) {
         minY = clip.y;
      }

      if (clip.y + clip.height < maxY) {
         maxY = clip.y + clip.height;
      }

      if (maxX >= minX && maxY >= minY) {
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX - x, oY + y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX - x, oY + y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX - x, oY, minX, maxX, minY, maxY);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX - y, oY + x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX - y, oY + x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX, oY + x, minX, maxX, minY, maxY);
         }
      }
   }

   protected static final void drawArcNW(Graphics g, int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
      int minX = oX - radiusX;
      int maxX = oX;
      int minY = oY - radiusY;
      int maxY = oY;
      if (startAngle > 0.0) {
         maxX = oX - (int)((double)radiusX * Math.sin(startAngle / 180.0 * Math.PI));
         minY = oY - (int)((double)radiusY * Math.cos(startAngle / 180.0 * Math.PI));
      }

      if (endAngle < 90.0) {
         minX = oX - (int)((double)radiusX * Math.sin(endAngle / 180.0 * Math.PI));
         maxY = oY - (int)((double)radiusY * Math.cos(endAngle / 180.0 * Math.PI));
      }

      Rectangle clip = g.getClipBounds();
      if (clip.x > minX) {
         minX = clip.x;
      }

      if (clip.x + clip.width < maxX) {
         maxX = clip.x + clip.width;
      }

      if (clip.y > minY) {
         minY = clip.y;
      }

      if (clip.y + clip.height < maxY) {
         maxY = clip.y + clip.height;
      }

      if (maxX >= minX && maxY >= minY) {
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX - x, oY - y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX - x, oY - y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX - y, oY - x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX - y, oY - x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX, oY - x, minX, maxX, minY, maxY);
         }
      }
   }

   protected static final void drawArcNE(Graphics g, int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
      int minX = oX;
      int maxX = oX + radiusX;
      int minY = oY - radiusY;
      int maxY = oY;
      if (startAngle > 0.0) {
         maxX = oX + (int)((double)radiusX * Math.cos(startAngle / 180.0 * Math.PI));
         maxY = oY - (int)((double)radiusY * Math.sin(startAngle / 180.0 * Math.PI));
      }

      if (endAngle < 90.0) {
         minX = oX + (int)((double)radiusX * Math.cos(endAngle / 180.0 * Math.PI));
         minY = oY - (int)((double)radiusY * Math.sin(endAngle / 180.0 * Math.PI));
      }

      Rectangle clip = g.getClipBounds();
      if (clip.x > minX) {
         minX = clip.x;
      }

      if (clip.x + clip.width < maxX) {
         maxX = clip.x + clip.width;
      }

      if (clip.y > minY) {
         minY = clip.y;
      }

      if (clip.y + clip.height < maxY) {
         maxY = clip.y + clip.height;
      }

      if (maxX >= minX && maxY >= minY) {
         if (radiusY <= radiusX) {
            int x = 0;
            int y = radiusY;
            int xE = 0;
            int yE = radiusX * radiusX;
            int e = -yE / 2;
            int c = yE / radiusY;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX + x, oY - y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX + x, oY - y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);
         } else {
            int x = 0;
            int y = radiusX;
            int xE = 0;
            int yE = radiusY * radiusY;
            int e = -yE / 2;
            int c = yE / radiusX;

            do {
               if (e <= 0) {
                  do {
                     set(g, oX + y, oY - x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  set(g, oX + y, oY - x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            set(g, oX, oY - x, minX, maxX, minY, maxY);
         }
      }
   }

   protected static final void set(Graphics g, int x, int y, int minX, int maxX, int minY, int maxY) {
      if ((x >= minX || y >= minY) && (y <= maxY || x <= maxX) && x >= minX - 1 && x <= maxX + 1 && y >= minY - 1 && y <= maxY + 1) {
         g.drawLine(x, y, x, y);
      }
   }
}
