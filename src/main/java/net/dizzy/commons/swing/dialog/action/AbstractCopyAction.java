package net.dizzy.commons.swing.dialog.action;

import java.awt.Component;

import net.dizzy.commons.swing.action.SmartAction;

public abstract class AbstractCopyAction extends SmartAction {
   public AbstractCopyAction() {
      super(net.dizzy.commons.swing.dialog.DizzyCommonsSwingDialogMessages.CUT);
   }

   public AbstractCopyAction(String name) {
      super(name);
   }
}
