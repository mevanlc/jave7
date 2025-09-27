package net.disy.commons.swing.dialog.wizard;

import java.awt.Component;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.core.internal.AbstractGenericDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public abstract class AbstractWizardConfiguration extends AbstractGenericDialogConfiguration implements IWizardConfiguration {
   private IWizardContainer container;

   public AbstractWizardConfiguration() {
      super(DialogButtonConfigurationFactory.createOkCancelWithOkText(DisyCommonsSwingDialogMessages.WIZARD_FINISH), null);
   }

   @Override
   public IWizardContainer getContainer() {
      return this.container;
   }

   @Override
   public void setContainer(IWizardContainer container) {
      this.container = container;
   }

   @Override
   public boolean canCancel() {
      IWizardPage currentPage = this.getContainer().getCurrentPage();
      return currentPage.canCancel();
   }

   @Override
   public boolean canFinish() {
      IWizardPage currentPage = this.getContainer().getCurrentPage();
      return currentPage.canFinish();
   }

   @Override
   public void updateSize() {
      this.container.updateSize();
   }

   @Override
   public boolean shallInitializePagesFromData() {
      return false;
   }

   @Override
   public IVetoDialogCloseHandler getVetoCloseHandler() {
      return new IVetoDialogCloseHandler() {
         @Override
         public boolean handleDialogAboutToClose(IDialogResult result, Component parentComponent) {
            return result.isCanceled() ? true : AbstractWizardConfiguration.this.performFinish(parentComponent);
         }
      };
   }

   @Deprecated
   @Override
   public boolean performFinish(Component parentComponent) {
      return true;
   }
}
