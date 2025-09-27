package de.jave.jave.ascii3d.example;

public class Cube3dExample implements I3dExample {
   private static final String CUBE = "#Simple Cube demo - by Markus Gebhard 2001\n#This script has to contain two parts:\n# 1) List of vertices (3 float values, coordinates)\n# 2) List of polygons (3,4,... integer values, vertex indices)\n#Lines that do not fit this scheme are being ignored w/o warning!\n#\n#list of vertices:\n# x    y    z   \n 1.0  1.0  1.0\n-1.0  1.0  1.0\n 1.0 -1.0  1.0\n 1.0  1.0 -1.0\n 1.0 -1.0 -1.0\n-1.0 -1.0  1.0\n-1.0  1.0 -1.0\n-1.0 -1.0 -1.0\n\n#list of polygons:\n 0 1 5 2 \n 0 1 6 3 \n 0 3 4 2 \n 2 5 7 4 \n 1 6 7 5 \n 3 6 7 4 \n";

   @Override
   public String getTitle() {
      return "Cube";
   }

   @Override
   public String getCode() {
      return "#Simple Cube demo - by Markus Gebhard 2001\n#This script has to contain two parts:\n# 1) List of vertices (3 float values, coordinates)\n# 2) List of polygons (3,4,... integer values, vertex indices)\n#Lines that do not fit this scheme are being ignored w/o warning!\n#\n#list of vertices:\n# x    y    z   \n 1.0  1.0  1.0\n-1.0  1.0  1.0\n 1.0 -1.0  1.0\n 1.0  1.0 -1.0\n 1.0 -1.0 -1.0\n-1.0 -1.0  1.0\n-1.0  1.0 -1.0\n-1.0 -1.0 -1.0\n\n#list of polygons:\n 0 1 5 2 \n 0 1 6 3 \n 0 3 4 2 \n 2 5 7 4 \n 1 6 7 5 \n 3 6 7 4 \n";
   }
}
