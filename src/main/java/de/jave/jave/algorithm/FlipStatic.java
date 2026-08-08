package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.area.BooleanArea;

public class FlipStatic extends JaveAlgorithm {
   private static FlipStatic instance;

   private FlipStatic() {
   }

   public static synchronized FlipStatic getInstance() {
      if (instance == null) {
         instance = new FlipStatic();
      }

      return instance;
   }

   @Override
   public String getUndoRedoName() {
      return "flip static";
   }

   @Override
   public String getMenuItemLabel() {
      return "Flip static";
   }

   @Override
   public JaveSelection apply(JaveSelection plate) {
      int h = plate.getHeight();
      BooleanArea mask = plate.getMask();
      if (mask != null) {
         boolean[][] m = mask.getContent();

         for (int y = 0; y < h / 2; y++) {
            boolean[] t = m[y];
            m[y] = m[h - y - 1];
            m[h - y - 1] = t;
         }
      }

      int[][] ch = plate.getContent().glyphPlane();

      for (int y = 0; y < h / 2; y++) {
         int[] t = ch[y];
         ch[y] = ch[h - y - 1];
         ch[h - y - 1] = t;
      }

      return plate;
   }
}
