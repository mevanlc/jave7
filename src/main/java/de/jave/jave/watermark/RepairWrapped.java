package de.jave.jave.algorithm;

import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;

public class RepairWrapped extends AbstractJaveAction {
   public RepairWrapped(JaveMainPanel mainPanel) {
      super(mainPanel, "(Wrapped lines)", null);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      CharacterPlate cp = this.getMainPanel().getContentOfInterest().getContent();
      String[] lines = cp.toStringArray();
      int maxWidth = 0;

      for (int i = 0; i < lines.length - 1; i += 2) {
         int w = lines[i].length() + 1 + lines[i + 1].length();
         if (w > maxWidth) {
            maxWidth = w;
         }
      }

      CharacterPlate result = new CharacterPlate(maxWidth, (lines.length + 1) / 2);

      for (int ix = 0; ix < lines.length; ix += 2) {
         result.paste(lines[ix], 0, ix / 2);
      }

      for (int ix = 1; ix < lines.length; ix += 2) {
         result.paste(lines[ix], maxWidth - lines[ix].length(), ix / 2);
      }

      this.getMainPanel().setContentOfInterest(result);
      this.getMainPanel().repaint();
      this.getMainPanel().saveCurrentState("repair wrapped");
   }
}
