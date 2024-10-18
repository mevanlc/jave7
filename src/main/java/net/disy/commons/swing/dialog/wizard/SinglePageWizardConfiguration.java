package net.disy.commons.swing.dialog.wizard;

import net.disy.commons.core.util.Ensure;

public class SinglePageWizardConfiguration extends AbstractWizardConfiguration {
   private IWizardPage wizardPage;
   private final IWizardPageFactory pageFactory;

   public SinglePageWizardConfiguration(IWizardPageFactory pageFactory) {
      Ensure.ensureArgumentNotNull(pageFactory);
      this.pageFactory = pageFactory;
   }

   @Override
   public void addPages() {
      this.wizardPage = this.pageFactory.createPage(this);
   }

   @Override
   public IWizardPage getStartingPage() {
      return this.wizardPage;
   }

   @Override
   public IWizardPage getNextPage(IWizardPage page) {
      return null;
   }

   @Override
   public IWizardPage getPreviousPage(IWizardPage page) {
      return null;
   }

   @Override
   public boolean isHelpAvailable() {
      return false;
   }
}
