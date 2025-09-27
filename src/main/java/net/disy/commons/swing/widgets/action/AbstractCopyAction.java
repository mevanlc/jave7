package net.disy.commons.swing.dialog.action;

import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public abstract class AbstractCopyAction extends SmartAction {
   public AbstractCopyAction() {
      super(DisyCommonsSwingDialogMessages.COPY, DisyCommonsSwingIconResources.COPY);
   }
}
