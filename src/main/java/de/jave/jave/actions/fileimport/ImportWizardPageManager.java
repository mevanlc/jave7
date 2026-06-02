package de.jave.jave.actions.fileimport;

import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class ImportWizardPageManager {
   private final ImportSourceFilePage page1;
   private final AsciimationImportPage page2;

   public ImportWizardPageManager(ImportWizard wizard, ImportWizardModel model) {
      this.page1 = new ImportSourceFilePage(wizard, model);
      this.page2 = new AsciimationImportPage(wizard, model);
   }

   public IWizardPage getStartingPage() {
      return this.page1;
   }

   public IWizardPage getNextPage(IWizardPage currentPage) {
      return currentPage == this.page1 ? this.page2 : null;
   }

   public IWizardPage getPreviousPage(IWizardPage currentPage) {
      return currentPage == this.page2 ? this.page1 : null;
   }
}
