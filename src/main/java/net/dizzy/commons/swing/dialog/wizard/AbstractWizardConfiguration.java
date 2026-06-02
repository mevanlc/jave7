package net.dizzy.commons.swing.dialog.wizard;

import java.util.ArrayList;
import java.util.List;

import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IVetoDialogCloseHandler;

public abstract class AbstractWizardConfiguration implements IWizardConfiguration {
   private final List<IWizardPage> pages = new ArrayList<>();
   private final WizardContainer container = new WizardContainer();

   protected void addPage(IWizardPage page) {
      pages.add(page);
      if (page instanceof AbstractWizardPage) {
         ((AbstractWizardPage) page).setWizard(this);
      }
   }

   public WizardContainer getContainer() {
      return container;
   }

   @Override public IWizardPage getStartingPage() { return pages.isEmpty() ? null : pages.get(0); }
   @Override public IWizardPage getNextPage(IWizardPage page) { int index = pages.indexOf(page); return index >= 0 && index + 1 < pages.size() ? pages.get(index + 1) : null; }
   @Override public IWizardPage getPreviousPage(IWizardPage page) { int index = pages.indexOf(page); return index > 0 ? pages.get(index - 1) : null; }
   @Override public boolean isHelpAvailable() { return false; }
   @Override public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() { return DialogHeaderPanelConfiguration.createInvisible(); }
   @Override public IVetoDialogCloseHandler getVetoCloseHandler() { return null; }

   public static class WizardContainer {
      public void requestNext() {
      }
   }
}
