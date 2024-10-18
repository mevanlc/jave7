package de.jave.jave;

import java.awt.Point;

public class EllipseAlgorithm {
   public static void drawEllipse(ICharacterDrawable plate, Point centerLocation, int radiusX, int radiusY, char ch) {
      drawEllipse(plate, centerLocation.x, centerLocation.y, radiusX, radiusY, ch);
   }

   public static void drawEllipse(ICharacterDrawable plate, int oX, int oY, int radiusX, int radiusY, char ch) {
      if (radiusX == 0) {
         LineAlgorithm.drawLineBresenham(plate, oX, oY - radiusY, oX, oY + radiusY, ch);
      } else if (radiusY == 0) {
         LineAlgorithm.drawLineBresenham(plate, oX - radiusX, oY, oX + radiusX, oY + radiusY, ch);
      } else {
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
                     plate.set(oX + x, oY + y, ch);
                     plate.set(oX - x, oY + y, ch);
                     plate.set(oX + x, oY - y, ch);
                     plate.set(oX - x, oY - y, ch);
                     x++;
                     xE += radiusY;
                     e += xE;
                  } while (e <= 0);
               } else {
                  plate.set(oX + x, oY + y, ch);
                  plate.set(oX - x, oY + y, ch);
                  plate.set(oX + x, oY - y, ch);
                  plate.set(oX - x, oY - y, ch);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            plate.set(oX + x, oY, ch);
            plate.set(oX - x, oY, ch);
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
                     plate.set(oX + y, oY + x, ch);
                     plate.set(oX - y, oY + x, ch);
                     plate.set(oX + y, oY - x, ch);
                     plate.set(oX - y, oY - x, ch);
                     x++;
                     xE += radiusX;
                     e += xE;
                  } while (e <= 0);
               } else {
                  plate.set(oX + y, oY + x, ch);
                  plate.set(oX - y, oY + x, ch);
                  plate.set(oX + y, oY - x, ch);
                  plate.set(oX - y, oY - x, ch);
               }

               y--;
               yE -= c;
               e -= yE;
            } while (y != 0);

            plate.set(oX, oY + x, ch);
            plate.set(oX, oY - x, ch);
         }
      }
   }
}
