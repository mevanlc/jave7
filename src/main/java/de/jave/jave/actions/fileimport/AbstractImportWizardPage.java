package de.jave.jave.actions.fileimport;

import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.disy.commons.swing.dialog.wizard.AbstractWizardPage;

public abstract class AbstractImportWizardPage extends AbstractWizardPage {
   private final ImportWizardModel model;

   public AbstractImportWizardPage(String string, String defaultMessageText, AbstractWizardConfiguration wizard, ImportWizardModel model) {
      super(string, defaultMessageText);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setWizard(wizard);
   }

   protected final ImportWizardModel getModel() {
      return this.model;
   }

   @Override
   public boolean canFlipToNextPage() {
      if (this.getNextPage() == null) {
         return false;
      } else {
         return this.getMessage().getType() == MessageType.ERROR ? false : this.createCurrentMessage().getType() != MessageType.ERROR;
      }
   }
}
