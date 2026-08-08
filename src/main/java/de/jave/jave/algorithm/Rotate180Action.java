package de.jave.jave.algorithm;

import de.jave.image.Rotation;
import de.jave.image2ascii.RotationUi;
import de.jave.jave.JaveSelection;
import de.jave.jave.actions.AbstractJaveUndoableAction;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import java.awt.Dimension;
import net.dizzy.commons.core.util.Ensure;

public class Rotate180Action extends AbstractJaveUndoableAction {
   private final GeneralAlgorithmConfiguration configuration;

   public Rotate180Action(JaveMainPanel mainPanel, GeneralAlgorithmConfiguration configuration) {
      super(mainPanel, "180 degrees", new RotationUi().getIcon(Rotation.UPSIDE_DOWN));
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   public String getActionName() {
      return "rotate 180";
   }

   @Override
   protected JaveSelection apply(Component parentComponent, JaveSelection selection) {
      selection = Rotate180StaticAction.applyTo(selection);
      int[][] ch = selection.getContent().glyphPlane();
      Dimension size = selection.getSize();
      String replacements = this.configuration.getRotate180();

      for (int y = 0; y < size.height; y++) {
         for (int x = 0; x < size.width; x++) {
            if (ch[y][x] >= ' ' && ch[y][x] <= '~') {
               ch[y][x] = replacements.charAt(ch[y][x] - ' ');
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
