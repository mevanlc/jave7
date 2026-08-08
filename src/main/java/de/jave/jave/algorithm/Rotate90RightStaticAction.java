package de.jave.jave.algorithm;

import de.jave.image.Rotation;
import de.jave.image2ascii.RotationUi;
import de.jave.jave.JaveSelection;
import de.jave.jave.actions.AbstractJaveUndoableAction;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;

public class Rotate90RightStaticAction extends AbstractJaveUndoableAction {
   public Rotate90RightStaticAction(JaveMainPanel mainPanel) {
      super(mainPanel, "90 degrees right", new RotationUi().getIcon(Rotation.RIGHT));
   }

   @Override
   protected String getActionName() {
      return "rotate 90 degrees";
   }

   @Override
   protected JaveSelection apply(Component parentComponent, JaveSelection selection) {
      return applyTo(selection);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   public static JaveSelection applyTo(JaveSelection selection) {
      CharacterPlate plate = selection.getContent();
      int[][] ch1 = plate.glyphPlane();
      int h = plate.getSize().height;
      int w = plate.getSize().width;
      plate.setSize(h, w);
      int[][] ch2 = plate.glyphPlane();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            ch2[x][h - y - 1] = ch1[y][x];
         }
      }

      return selection;
   }
}
