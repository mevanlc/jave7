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

public class Rotate90RightAction extends AbstractJaveUndoableAction {
   private final GeneralAlgorithmConfiguration configuration;

   public Rotate90RightAction(JaveMainPanel mainPanel, GeneralAlgorithmConfiguration configuration) {
      super(mainPanel, "90 degrees right", new RotationUi().getIcon(Rotation.RIGHT));
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   protected String getActionName() {
      return "rotate 90 degrees";
   }

   @Override
   protected JaveSelection apply(Component parentComponent, JaveSelection selection) {
      selection = Rotate90RightStaticAction.applyTo(selection);
      char[][] ch = selection.getContent().getContent();
      char[] table = this.configuration.getRotate90Right().toCharArray();
      Dimension size = selection.getSize();

      for (int y = 0; y < size.height; y++) {
         for (int x = 0; x < size.width; x++) {
            if (ch[y][x] >= ' ' && ch[y][x] <= '~') {
               ch[y][x] = table[ch[y][x] - ' '];
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
