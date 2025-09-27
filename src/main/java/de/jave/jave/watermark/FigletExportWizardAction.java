package de.jave.jave.actions;

import de.jave.figlet.engine.IFigDriver;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.figlet.export.FigletExportWizard;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.wizard.WizardDialog;

public class FigletExportWizardAction extends AbstractJaveAction {
   private final JaveApplicationPreferences preferences;
   private final IFigDriver figDriver;

   public FigletExportWizardAction(IFigDriver figDriver, JaveMainPanel mainPanel, JaveApplicationPreferences preferences) {
      super(mainPanel, "Export as FIGlet Font", JaveIcons.EXPORT_FIGLET_ICON);
      Ensure.ensureArgumentNotNull(figDriver);
      Ensure.ensureArgumentNotNull(preferences);
      this.figDriver = figDriver;
      this.preferences = preferences;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      FigletExportWizard wizard = new FigletExportWizard(editor, this.preferences, this.figDriver);
      WizardDialog dialog = new WizardDialog(parentComponent, wizard);
      dialog.show();
      wizard.resetPlateView();
   }
}
