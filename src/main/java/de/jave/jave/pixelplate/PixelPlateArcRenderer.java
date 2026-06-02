package de.jave.jave.pixelplate;

import de.jave.jave.Point2d;
import net.dizzy.commons.core.util.Ensure;

public class PixelPlateArcRenderer {
   private final PixelPlate plate;

   public PixelPlateArcRenderer(PixelPlate plate) {
      Ensure.ensureArgumentNotNull(plate);
      this.plate = plate;
   }

   public void drawArc(Point2d center, double radiusX, double radiusY, double angle, double arcAngle) {
      int physicalCenterX = this.plate.getPhysicalX(center.getX());
      int physicalCenterY = this.plate.getPhysicalY(center.getY());
      int physicalRadiusX = this.plate.getPhysicalX(radiusX);
      int physicalRadiusY = this.plate.getPhysicalY(radiusY);
      this.drawArc(physicalCenterX, physicalCenterY, physicalRadiusX, physicalRadiusY, angle, arcAngle);
   }

   private void drawArc(int centerX, int centerY, int radiusX, int radiusY, double angle, double arcAngle) {
      if (radiusX != 0 && radiusY != 0) {
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
               this.drawArcSE(centerX, centerY, radiusX, radiusY, 0.0, endAngle);
               this.drawArcSE(centerX, centerY, radiusX, radiusY, startAngle, 90.0);
            } else {
               this.drawArcSE(centerX, centerY, radiusX, radiusY, startAngle, endAngle);
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
               this.drawArcSW(centerX, centerY, radiusX, radiusY, 0.0, endAnglex);
               this.drawArcSW(centerX, centerY, radiusX, radiusY, startAnglex, 90.0);
            } else {
               this.drawArcSW(centerX, centerY, radiusX, radiusY, startAnglex, endAnglex);
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
               this.drawArcNW(centerX, centerY, radiusX, radiusY, 0.0, endAnglexx);
               this.drawArcNW(centerX, centerY, radiusX, radiusY, startAnglexx, 90.0);
            } else {
               this.drawArcNW(centerX, centerY, radiusX, radiusY, startAnglexx, endAnglexx);
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
               this.drawArcNE(centerX, centerY, radiusX, radiusY, 0.0, endAnglexxx);
               this.drawArcNE(centerX, centerY, radiusX, radiusY, startAnglexxx, 90.0);
            } else {
               this.drawArcNE(centerX, centerY, radiusX, radiusY, startAnglexxx, endAnglexxx);
            }
         }
      } else {
         this.set(centerX, centerY, centerX, centerX, centerY, centerY);
      }
   }

   private void drawArcSE(int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
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
                     this.set(oX + x, oY + y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + x, oY + y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX + x, oY, minX, maxX, minY, maxY);
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
                     this.set(oX + y, oY + x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + y, oY + x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX, oY + x, minX, maxX, minY, maxY);
         }
      }
   }

   private void drawArcSW(int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
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
                     this.set(oX - x, oY + y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX - x, oY + y, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX - x, oY, minX, maxX, minY, maxY);
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
                     this.set(oX - y, oY + x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX - y, oY + x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX, oY + x, minX, maxX, minY, maxY);
         }
      }
   }

   private void drawArcNW(int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
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
                     this.set(oX - x, oY - y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX - x, oY - y, minX, maxX, minY, maxY);
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
                     this.set(oX - y, oY - x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX - y, oY - x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX, oY - x, minX, maxX, minY, maxY);
         }
      }
   }

   private void drawArcNE(int oX, int oY, int radiusX, int radiusY, double startAngle, double endAngle) {
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
                     this.set(oX + x, oY - y, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + x, oY - y, minX, maxX, minY, maxY);
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
                     this.set(oX + y, oY - x, minX, maxX, minY, maxY);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  this.set(oX + y, oY - x, minX, maxX, minY, maxY);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            this.set(oX, oY - x, minX, maxX, minY, maxY);
         }
      }
   }

   private final void set(int x, int y, int minX, int maxX, int minY, int maxY) {
      if ((x >= minX || y >= minY) && (y <= maxY || x <= maxX) && x >= minX - 1 && x <= maxX + 1 && y >= minY - 1 && y <= maxY + 1) {
         this.plate.set(x, y);
      }
   }
}
