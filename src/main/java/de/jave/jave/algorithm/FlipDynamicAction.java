package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.area.BooleanArea;
import net.dizzy.commons.core.util.Ensure;

public class FlipDynamicAction extends JaveAlgorithm {
   private final GeneralAlgorithmConfiguration configuration;

   public FlipDynamicAction(GeneralAlgorithmConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public String getUndoRedoName() {
      return "flip";
   }

   @Override
   public String getMenuItemLabel() {
      return "Flip dynamic";
   }

   @Override
   public JaveSelection apply(JaveSelection plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();
      char[][] ch = plate.getContent().getContent();
      BooleanArea mask = plate.getMask();
      if (mask != null) {
         boolean[][] m = mask.getContent();

         for (int y = 0; y < h / 2; y++) {
            boolean[] t = m[y];
            m[y] = m[h - y - 1];
            m[h - y - 1] = t;
         }
      }

      for (int y = 0; y < h / 2; y++) {
         char[] t = ch[y];
         ch[y] = ch[h - y - 1];
         ch[h - y - 1] = t;
      }

      String replacements = this.configuration.getFlip();

      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            if (ch[y][x] >= ' ' && ch[y][x] <= '~') {
               ch[y][x] = replacements.charAt(ch[y][x] - ' ');
            }
         }
      }

      return plate;
   }
}
