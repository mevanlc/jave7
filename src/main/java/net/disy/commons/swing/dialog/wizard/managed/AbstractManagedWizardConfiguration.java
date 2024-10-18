package net.disy.commons.swing.dialog.wizard.managed;

import java.awt.Component;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.disy.commons.swing.dialog.wizard.IWizardPage;
import net.disy.commons.swing.dialog.wizard.WizardDialog;

public abstract class AbstractManagedWizardConfiguration extends AbstractWizardConfiguration {
   private WizardDialog dialog;
   private IWizardPageManager pageManager;

   protected abstract IWizardPageManager createPageManager();

   @Override
   public final IWizardPage getStartingPage() {
      return this.pageManager.getStartPage();
   }

   @Override
   public final IWizardPage getNextPage(IWizardPage page) {
      return this.pageManager.getNextPage(page);
   }

   @Override
   public final IWizardPage getPreviousPage(IWizardPage page) {
      return this.pageManager.getPreviousPage(page);
   }

   @Override
   public IVetoDialogCloseHandler getVetoCloseHandler() {
      return new IVetoDialogCloseHandler() {
         @Override
         public boolean handleDialogAboutToClose(IDialogResult result, Component parentComponent) {
            if (result.isCanceled()) {
               return true;
            } else {
               return AbstractManagedWizardConfiguration.this.pageManager instanceof IFinishingWizardPageManager
                  ? ((IFinishingWizardPageManager)AbstractManagedWizardConfiguration.this.pageManager).performFinish(parentComponent)
                  : true;
            }
         }
      };
   }

   @Override
   public final void addPages() {
      this.pageManager = this.createPageManager();
   }

   public void show(Component parentComponent) {
      if (this.dialog == null) {
         this.dialog = new WizardDialog(parentComponent, this);
      }

      this.dialog.show();
   }
}
