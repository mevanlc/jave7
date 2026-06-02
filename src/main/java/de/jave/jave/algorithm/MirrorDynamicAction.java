package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.area.BooleanArea;
import net.dizzy.commons.core.util.Ensure;

public class MirrorDynamicAction extends JaveAlgorithm {
   private final GeneralAlgorithmConfiguration configuration;

   public MirrorDynamicAction(GeneralAlgorithmConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public String getUndoRedoName() {
      return "mirror";
   }

   @Override
   public String getMenuItemLabel() {
      return "Mirror dynamic";
   }

   @Override
   public JaveSelection apply(JaveSelection plate) {
      int h = plate.getHeight();
      int w = plate.getWidth();
      char[][] ch = plate.getContent().getContent();
      String replacements = this.configuration.getMirror();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w / 2; x++) {
            char t = ch[y][x];
            ch[y][x] = ch[y][w - x - 1];
            ch[y][w - x - 1] = t;
         }

         for (int x = 0; x < w; x++) {
            if (ch[y][x] >= ' ' && ch[y][x] <= '~') {
               ch[y][x] = replacements.charAt(ch[y][x] - ' ');
            }
         }
      }

      BooleanArea mask = plate.getMask();
      if (mask != null) {
         boolean[][] m = mask.getContent();

         for (int y = 0; y < h; y++) {
            for (int xx = 0; xx < w / 2; xx++) {
               boolean t = m[y][xx];
               m[y][xx] = m[y][w - xx - 1];
               m[y][w - xx - 1] = t;
            }
         }
      }

      return plate;
   }
}
