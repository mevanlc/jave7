package net.disy.commons.swing.dialog.action;

import java.awt.Component;
import javax.swing.text.JTextComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;

public class TextComponentSelectAllAction extends SmartAction {
   private final JTextComponent textComponent;

   public TextComponentSelectAllAction(JTextComponent textComponent) {
      super(DisyCommonsSwingDialogMessages.SELECT_ALL);
      Ensure.ensureArgumentNotNull(textComponent);
      this.textComponent = textComponent;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.textComponent.selectAll();
   }
}
