package de.jave.jave.figlet.export;

import de.jave.jave.help.JaveOnlineHelpHandler;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.wizard.AbstractWizardPage;

public abstract class AbstractFigletExportWizardPage extends AbstractWizardPage {
   private final FigletExportModel model;

   protected AbstractFigletExportWizardPage(FigletExportModel model, String description, String defaultMessageText) {
      super(description, defaultMessageText);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   protected FigletExportModel getModel() {
      return this.model;
   }

   @Override
   public IDialogHelpHandler getHelpHandler() {
      return new JaveOnlineHelpHandler("figletexport/figletexport.html");
   }
}
