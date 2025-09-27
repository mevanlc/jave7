package de.jave.jave.algorithm;

import de.jave.image.Rotation;
import de.jave.image2ascii.RotationUi;
import de.jave.jave.JaveSelection;
import de.jave.jave.actions.AbstractJaveUndoableAction;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.awt.Component;

public class Rotate180StaticAction extends AbstractJaveUndoableAction {
   public Rotate180StaticAction(JaveMainPanel mainPanel) {
      super(mainPanel, "180 degrees", new RotationUi().getIcon(Rotation.UPSIDE_DOWN));
   }

   @Override
   protected String getActionName() {
      return "rotate 180 degrees";
   }

   @Override
   protected JaveSelection apply(Component parentComponent, JaveSelection selection) {
      return applyTo(selection);
   }

   public static JaveSelection applyTo(JaveSelection selection) {
      CharacterPlate plate = selection.getContent();
      char[][] ch = plate.getContent();
      int h = plate.getHeight();
      int w = plate.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w / 2; x++) {
            char t = ch[y][x];
            ch[y][x] = ch[h - y - 1][w - x - 1];
            ch[h - y - 1][w - x - 1] = t;
         }
      }

      if (w % 2 != 0) {
         int x = w / 2;

         for (int y = 0; y < h / 2; y++) {
            char t = ch[y][x];
            ch[y][x] = ch[h - y - 1][x];
            ch[h - y - 1][x] = t;
         }
      }

      BooleanArea mask = selection.getMask();
      if (mask != null) {
         boolean[][] m = mask.getContent();

         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w / 2; x++) {
               boolean t = m[y][x];
               m[y][x] = m[h - y - 1][w - x - 1];
               m[h - y - 1][w - x - 1] = t;
            }
         }

         if (w % 2 != 0) {
            int x = w / 2;

            for (int y = 0; y < h / 2; y++) {
               boolean t = m[y][x];
               m[y][x] = m[h - y - 1][x];
               m[h - y - 1][x] = t;
            }
         }
      }

      return selection;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }
}
