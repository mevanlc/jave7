package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;

public class CompressExpand extends JaveOptionsAlgorithm {
   private static CompressExpand instance;
   protected CompressExpandOptions options;

   private CompressExpand() {
   }

   public static synchronized CompressExpand getInstance() {
      if (instance == null) {
         instance = new CompressExpand();
      }

      return instance;
   }

   @Override
   public void setOptions(JaveAlgorithmOptions options) {
      this.options = (CompressExpandOptions)options;
   }

   @Override
   public JaveAlgorithmOptions getOptions() {
      if (this.options == null) {
         this.options = new CompressExpandOptions();
      }

      return this.options;
   }

   @Override
   public String getUndoRedoName() {
      return "compress/expand";
   }

   @Override
   public String getMenuItemLabel() {
      return "Compress/Expand...";
   }

   @Override
   public String getOptionsDialogTitle() {
      return "Compress / Expand Tool";
   }

   @Override
   public JaveSelection apply(JaveSelection sel) {
      int sourceWidth = sel.getWidth();
      int sourceHeight = sel.getHeight();
      int newWidth = this.options.getNewWidth();
      int newHeight = this.options.getNewHeight();
      double xScale = (double)newWidth / (double)sourceWidth;
      double yScale = (double)newHeight / (double)sourceHeight;
      CharacterPlate sourcePlate = sel.getContent();
      CharacterPlate resultContent = new CharacterPlate(newWidth, newHeight);
      BooleanArea sourceMask = sel.getMask();
      BooleanArea resultMask = new BooleanArea(newWidth, newHeight);
      resultContent.setMix(true);
      if (xScale <= 1.0 && yScale <= 1.0) {
         for (int y = 0; y < sourceHeight; y++) {
            for (int x = 0; x < sourceWidth; x++) {
               int xx = (int)Math.round((double)x * xScale);
               int yy = (int)Math.round((double)y * yScale);
               char ch = sourcePlate.get(x, y);
               if (ch != ' ') {
                  resultContent.set(xx, yy, ch);
               }

               if (sourceMask == null || sourceMask.isSet(x, y)) {
                  resultMask.set(xx, yy, true);
               }
            }
         }
      } else if (xScale <= 1.0 && yScale > 1.0) {
         for (int yyx = 0; yyx < newHeight; yyx++) {
            for (int x = 0; x < sourceWidth; x++) {
               int y = (int)((double)yyx / yScale);
               int xxx = (int)Math.round((double)x * xScale);
               char chx = sourcePlate.get(x, y);
               if (chx != ' ') {
                  resultContent.set(xxx, yyx, chx);
               }

               if (sourceMask == null || sourceMask.isSet(x, y)) {
                  resultMask.set(xxx, yyx, true);
               }
            }
         }
      } else if (xScale > 1.0 && yScale <= 1.0) {
         for (int yx = 0; yx < sourceHeight; yx++) {
            for (int xxxx = 0; xxxx < newWidth; xxxx++) {
               int yyx = (int)Math.round((double)yx * yScale);
               int x = (int)((double)xxxx / xScale);
               char chxx = sourcePlate.get(x, yx);
               if (chxx != ' ') {
                  resultContent.set(xxxx, yyx, chxx);
               }

               if (sourceMask == null || sourceMask.isSet(x, yx)) {
                  resultMask.set(xxxx, yyx, true);
               }
            }
         }
      } else {
         for (int yyxx = 0; yyxx < newHeight; yyxx++) {
            for (int xxxx = 0; xxxx < newWidth; xxxx++) {
               int yx = (int)((double)yyxx / yScale);
               int xxxxx = (int)((double)xxxx / xScale);
               char chxxx = sourcePlate.get(xxxxx, yx);
               if (chxxx != ' ') {
                  resultContent.set(xxxx, yyxx, chxxx);
               }

               if (sourceMask == null || sourceMask.isSet(xxxxx, yx)) {
                  resultMask.set(xxxx, yyxx, true);
               }
            }
         }
      }

      sel.setContent(resultContent);
      sel.setMask(resultMask);
      return sel;
   }
}
