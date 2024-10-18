package net.disy.commons.swing.dialog.action;

import java.awt.Component;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class DialogAction extends SmartAction {
   private final IDialogInput input;

   public DialogAction(IActionConfiguration configuration, IDialogInput input) {
      super(configuration);
      this.input = input;
   }

   @Override
   protected void execute(Component parentComponent) {
      IDialogPage dialogPage = this.input.createPage();
      UserDialog userDialog = new UserDialog(parentComponent, dialogPage);
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         this.input.confirm();
      }
   }

   public boolean evaluateInput(IPredicate<IDialogInput> predicate) {
      return predicate.evaluate(this.input);
   }
}
