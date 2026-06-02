package de.jave.jave.actions;

import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.jave.JavEApplication;
import de.jave.jave.PlateDocument;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.clipart.ClipartDialogPage;
import de.jave.jave.clipart.ClipartDialogResult;
import de.jave.jave.clipart.ClipartGroup;
import de.jave.jave.clipart.ClipartManager;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import java.awt.Component;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class ClipartLibraryAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public ClipartLibraryAction(JavEApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Clipart Library", JaveIcons.CLIPART_ICON);
      this.jave = jave;
      this.setToolTipText("Show Clipart Library Dialog");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return new IJaveDocumentEditorActionEnabledStrategy() {
         @Override
         public boolean isEnabledFor(IDocumentEditor activeEditor) {
            return activeEditor == null || TextAndAnimationEditorEnabledStrategy.getInstance().isEnabledFor(activeEditor);
         }
      };
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      ClipartManager clipartManager = ClipartManager.performLoad(parentComponent);
      if (clipartManager != null) {
         String groupName = this.jave.getApplicationPreferences().getClipartGroupName();
         ClipartGroup group = clipartManager.getGroup(groupName);
         if (group == null && clipartManager.getGroupCount() > 0) {
            group = clipartManager.getGroup(0);
         }

         ObjectModel<ClipartGroup> groupSelectionModel = new ObjectModel<>(group);
         FontModel displayFontModel = this.jave.getApplicationPreferences().getDisplayFontModel();
         PlateDocument document = this.getMainPanel().getDocument();
         ColorScheme colorScheme = document != null ? document.getColorScheme() : this.jave.getApplicationPreferences().getDefaultColorSchemeModel().getValue();
         AsciiTextAreaProperties properties = new AsciiTextAreaProperties(displayFontModel);
         properties.setEditable(false);
         properties.setForeground(colorScheme.getColorText());
         properties.setBackground(colorScheme.getColorPlateBackground());
         ClipartDialogPage dialogPage = new ClipartDialogPage(properties, clipartManager, groupSelectionModel);
         UserDialog dialog = new UserDialog(
            parentComponent, new DefaultDialogConfiguration<ClipartDialogPage>(dialogPage, DialogButtonConfigurationFactory.createCloseOnly()) {
               @Override
               public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
                  return DialogHeaderPanelConfiguration.createInvisible();
               }
            }
         );
         dialog.show();
         ClipartDialogResult selection = dialogPage.getSelectionResult();
         if (selection != null && selection.getClipart() != null) {
            String newGroupName = selection.getGroup().getName();
            this.jave.getApplicationPreferences().setClipartGroupName(newGroupName);
            this.jave.pasteAsNewSelection(selection.getClipart().getContent());
         }
      }
   }
}
