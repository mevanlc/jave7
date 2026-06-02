package de.jave.jave.figlet.export;

import de.jave.figlet.engine.IFigDriver;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class FigletExportWizardPageManager {
   private final IWizardPage page1;
   private final IWizardPage page2;
   private final IWizardPage page3;
   private final IWizardPage page4;
   private final IWizardPage page5;

   public FigletExportWizardPageManager(FigletExportWizard wizard, FigletExportModel model, IFigDriver figDriver) {
      Ensure.ensureArgumentNotNull(wizard);
      Ensure.ensureArgumentNotNull(figDriver);
      this.page1 = new FigletExportWizardPage1(model);
      this.page1.setWizard(wizard);
      this.page2 = new FigletExportWizardPage2(model);
      this.page2.setWizard(wizard);
      this.page3 = new FigletExportWizardPage3(model);
      this.page3.setWizard(wizard);
      this.page4 = new FigletExportWizardPage4(model, figDriver);
      this.page4.setWizard(wizard);
      this.page5 = new FigletExportWizardPage5(model);
      this.page5.setWizard(wizard);
   }

   public IWizardPage getStartingPage() {
      return this.page1;
   }

   public IWizardPage getNextPage(IWizardPage page) {
      if (page == this.page1) {
         return this.page2;
      } else if (page == this.page2) {
         return this.page3;
      } else if (page == this.page3) {
         return this.page4;
      } else {
         return page == this.page4 ? this.page5 : null;
      }
   }

   public IWizardPage getPreviousPage(IWizardPage page) {
      if (page == this.page5) {
         return this.page4;
      } else if (page == this.page4) {
         return this.page3;
      } else if (page == this.page3) {
         return this.page2;
      } else {
         return page == this.page2 ? this.page1 : null;
      }
   }
}
