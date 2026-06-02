package net.dizzy.commons.swing.dialog.wizard;

import java.awt.Component;

import net.dizzy.commons.swing.dialog.core.DialogResult;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;

public class WizardDialog {
   private final Component parent;
   private final IWizardConfiguration configuration;

   public WizardDialog(Component parent, IWizardConfiguration configuration) {
      this.parent = parent;
      this.configuration = configuration;
      configuration.addPages();
   }

   public IDialogResult show() {
      IWizardPage page = configuration.getStartingPage();
      if (page == null) {
         return DialogResult.CANCELED;
      }
      return new UserDialog(parent, new DefaultDialogConfiguration<>(new WizardPageAdapter(page))).show();
   }
}
