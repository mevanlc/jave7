package net.disy.commons.swing.dialog.foldout;

import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public abstract class AbstractFoldOutDialogConfiguration<P extends IDialogPage> extends DefaultDialogConfiguration<P> implements IFoldOutDialogConfiguration<P> {
   private final IFoldOutPage foldOutPage;

   public AbstractFoldOutDialogConfiguration(P dialogPage, IFoldOutPage foldOutPage) {
      this(dialogPage, foldOutPage, DialogButtonConfigurationFactory.createOkCancel());
   }

   public AbstractFoldOutDialogConfiguration(P dialogPage, IFoldOutPage foldOutPage, IDialogButtonConfiguration buttonConfiguration) {
      super(dialogPage, buttonConfiguration);
      this.foldOutPage = foldOutPage;
   }

   @Override
   public IActionConfiguration getFoldOutButtonConfiguration() {
      String label = DisyCommonsSwingDialogMessages.getString("FoldOutDialog.Button.showDetails.text");
      return new ActionConfiguration(label);
   }

   @Override
   public IActionConfiguration getFoldInButtonConfiguration() {
      String label = DisyCommonsSwingDialogMessages.getString("FoldOutDialog.Button.hideDetails.text");
      return new ActionConfiguration(label);
   }

   @Override
   public IFoldOutPage getFoldOutPage() {
      return this.foldOutPage;
   }

   @Override
   public boolean isInitiallyFoldedOut() {
      return false;
   }
}
