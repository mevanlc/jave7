package de.jave.jave.actions.fileimport;

import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.disy.commons.swing.dialog.wizard.IWizardPage;

final class ImportWizard extends AbstractWizardConfiguration {
   private final ImportWizardModel model;
   private ImportWizardPageManager pageManager;

   public ImportWizard(ImportWizardModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   public void addPages() {
      this.pageManager = new ImportWizardPageManager(this, this.model);
   }

   @Override
   public IWizardPage getStartingPage() {
      return this.pageManager.getStartingPage();
   }

   @Override
   public IWizardPage getNextPage(IWizardPage page) {
      return this.pageManager.getNextPage(page);
   }

   @Override
   public IWizardPage getPreviousPage(IWizardPage page) {
      return this.pageManager.getPreviousPage(page);
   }

   @Override
   public boolean isHelpAvailable() {
      return false;
   }
}
