package net.disy.commons.swing.dialog.wizard.managed;

import java.util.ArrayList;
import java.util.List;
import net.disy.commons.swing.dialog.wizard.IWizardPage;

public class DefaultWizardPageManager implements IWizardPageManager {
   private final List<IManagedWizardPage> pages = new ArrayList<>();

   public final void addPage(IManagedWizardPage page) {
      this.pages.add(page);
   }

   private int getIndex(IWizardPage page) {
      return this.pages.indexOf(page);
   }

   private IWizardPage getNextApplicablePage(int startingIndex, int increment) {
      int index = startingIndex;

      while (index < this.pages.size() && index > -1) {
         IManagedWizardPage currentPage = this.pages.get(index);
         if (currentPage.isApplicable()) {
            return currentPage;
         }

         index += increment;
      }

      return null;
   }

   @Override
   public final IWizardPage getNextPage(IWizardPage page) {
      return this.getNextApplicablePage(this.getIndex(page) + 1, 1);
   }

   @Override
   public final IWizardPage getPreviousPage(IWizardPage page) {
      return this.getNextApplicablePage(this.getIndex(page) - 1, -1);
   }

   @Override
   public final IWizardPage getStartPage() {
      return this.getNextApplicablePage(0, 1);
   }
}
