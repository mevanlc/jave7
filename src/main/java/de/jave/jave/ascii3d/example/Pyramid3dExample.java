package de.jave.jave.ascii3d.example;

public class Pyramid3dExample implements I3dExample {
   private static final String PYRAMID = "#Simple Pryramid demo - by Markus Gebhard 2001\n#This script has to contain two parts:\n# 1) List of vertices (3 float values, coordinates)\n# 2) List of polygons (3,4,... integer values, vertex indices)\n#Lines that do not fit this scheme are being ignored w/o warning!\n#\n#list of vertices:\n# x    y    z   \n 1.0 -1.0  1.0\n 1.0 -1.0 -1.0\n-1.0 -1.0  1.0\n-1.0 -1.0 -1.0\n 0.0  1.0  0.0\n\n#list of  polygons:\n 0 2 4 \n 0 1 4 \n 1 3 4 \n 2 3 4 \n 0 1 3 2 \n";

   @Override
   public String getCode() {
      return "#Simple Pryramid demo - by Markus Gebhard 2001\n#This script has to contain two parts:\n# 1) List of vertices (3 float values, coordinates)\n# 2) List of polygons (3,4,... integer values, vertex indices)\n#Lines that do not fit this scheme are being ignored w/o warning!\n#\n#list of vertices:\n# x    y    z   \n 1.0 -1.0  1.0\n 1.0 -1.0 -1.0\n-1.0 -1.0  1.0\n-1.0 -1.0 -1.0\n 0.0  1.0  0.0\n\n#list of  polygons:\n 0 2 4 \n 0 1 4 \n 1 3 4 \n 2 3 4 \n 0 1 3 2 \n";
   }

   @Override
   public String getTitle() {
      return "Pyramid";
   }
}
