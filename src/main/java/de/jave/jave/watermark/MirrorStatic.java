package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.area.BooleanArea;

public class MirrorStatic extends JaveAlgorithm {
   private static MirrorStatic instance;

   private MirrorStatic() {
   }

   public static synchronized MirrorStatic getInstance() {
      if (instance == null) {
         instance = new MirrorStatic();
      }

      return instance;
   }

   @Override
   public String getUndoRedoName() {
      return "mirror static";
   }

   @Override
   public String getMenuItemLabel() {
      return "Mirror static";
   }

   @Override
   public JaveSelection apply(JaveSelection plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();
      char[][] ch = plate.getContent().getContent();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w / 2; x++) {
            char t = ch[y][x];
            ch[y][x] = ch[y][w - x - 1];
            ch[y][w - x - 1] = t;
         }
      }

      BooleanArea mask = plate.getMask();
      if (mask != null) {
         boolean[][] m = mask.getContent();

         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w / 2; x++) {
               boolean t = m[y][x];
               m[y][x] = m[y][w - x - 1];
               m[y][w - x - 1] = t;
            }
         }
      }

      return plate;
   }
}
