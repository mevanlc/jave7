package de.jave.jave.actions.export;

import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.jave.JaveSelection;
import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.IOException;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public final class ExportTextAction extends AbstractJaveAction {
   private final FileModel currentDirectoryModel;
   private final FontModel displayFontModel;
   private final TextExportPreferences preferences;

   public ExportTextAction(JaveMainPanel mainPanel, TextExportPreferences preferences, FileModel currentDirectoryModel, FontModel displayFontModel) {
      super(mainPanel, "Export...", JaveIcons.EXPORT_ICON);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.currentDirectoryModel = currentDirectoryModel;
      this.displayFontModel = displayFontModel;
      this.setToolTipText("Export the Document Content to File or Clipboard");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveSelection contentOfInterest = editor.getPlate().getContentOfInterest();
      CharacterPlate content = contentOfInterest.getContent();
      TextExportDialogModel model = this.preferences.createModel();
      ColorScheme colorScheme = editor.getPlate().getDocument().getColorScheme();
      model.getOptionsModel().setForegroundColor(colorScheme.getColorText());
      model.getOptionsModel().setBackgroundColor(colorScheme.getColorPlateBackground());
      model.getOptionsModel().setFont(this.displayFontModel.getFont());
      IDialogPage dialogPage = new TextExportDialogPage(content, model);
      UserDialog dialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(dialogPage) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createVisibleWithIcon(JaveIcons.LARGE_EXPORT_WIZARD_ICON);
         }
      });
      IDialogResult result = dialog.show();
      if (!result.isCanceled()) {
         ITextExportFormat format = model.getFormatSelectionModel().getFirstSelectedValue();
         ExportDestination destination = model.getDestinationModel().getValue();
         this.preferences.saveSettings(model);
         if (destination == ExportDestination.CLIPBOARD) {
            this.performExportToClipboard(parentComponent, content, model, format);
         } else {
            this.performExportToFile(parentComponent, content, model, format);
         }
      }
   }

   private void performExportToFile(Component parentComponent, CharacterPlate content, TextExportDialogModel model, final ITextExportFormat format) {
      final String documentName = DocumentEditorTitleFactory.createShortEditorTitle(this.getMainPanel().getEditor());
      final SmartFileFilter[] fileFilters = format.getFileFilters();
      File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return ExportTextAction.this.currentDirectoryModel;
         }

         @Override
         public String getSaveDialogTitle() {
            return "Save as " + format.getName();
         }

         @Override
         public String getOpenDialogTitle() {
            return null;
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return fileFilters;
         }

         @Override
         public String getFileNameSuggestion() {
            SmartFileFilter filter = fileFilters[0];
            if (filter instanceof ExtensionFileFilter) {
               ExtensionFileFilter extensionFilter = (ExtensionFileFilter)filter;
               return documentName + "." + extensionFilter.getExtensions().get(0).getString();
            } else {
               return documentName;
            }
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }
      });
      if (file != null) {
         try {
            format.convertTo(content, model.getOptionsModel(), file);
         } catch (IOException var9) {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("Error writing to file '" + file.getAbsolutePath() + "'.", var9));
         }
      }
   }

   private void performExportToClipboard(Component parentComponent, CharacterPlate content, TextExportDialogModel model, ITextExportFormat format) {
      Clipboard clipboard = parentComponent.getToolkit().getSystemClipboard();
      format.convertTo(content, model.getOptionsModel(), clipboard);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }
}
