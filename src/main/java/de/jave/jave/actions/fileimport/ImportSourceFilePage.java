package de.jave.jave.actions.fileimport;

import de.jave.gui.io.AbstractSourceFilePanelConfiguration;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.ISourceFilePanelConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.gui.io.SourceFilePanel;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class ImportSourceFilePage extends AbstractImportWizardPage {
   private static final String DEFAULT_MESSAGE = "Open a source file and select the type for import.";

   public ImportSourceFilePage(AbstractWizardConfiguration wizard, ImportWizardModel model) {
      super("Import", "Open a source file and select the type for import.", wizard, model);
   }

   @Override
   protected JComponent createContent() {
      final IFileChooserConfiguration fileChooserConfiguration = new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return ImportSourceFilePage.this.getModel().getCurrentDirectoryModel();
         }

         @Override
         public String getSaveDialogTitle() {
            return null;
         }

         @Override
         public String getOpenDialogTitle() {
            return "Source File";
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[0];
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }
      };
      ISourceFilePanelConfiguration configuration = new AbstractSourceFilePanelConfiguration() {
         @Override
         public String getLabel() {
            return "Source file:";
         }

         @Override
         public IFileChooserConfiguration getFileChooserConfiguration() {
            return fileChooserConfiguration;
         }
      };
      SourceFilePanel sourceFilePanel = new SourceFilePanel(this.getModel().getSourceFileModel(), configuration);
      JRadioButton radioButton = new JRadioButton("Ascii animation (plain text)", true);
      ButtonGroup group = new ButtonGroup();
      group.add(radioButton);
      GridDialogLayoutData insettedData = new GridDialogLayoutData();
      insettedData.setHorizontalIndent(20);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(sourceFilePanel.createPanel());
      panel.add(new JLabel("Type:"));
      panel.add(radioButton, insettedData);
      this.getModel().getSourceFileModel().addChangeListener(this.getCheckInputValidListener());
      return panel;
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getModel().getSourceFileModel().getValue() == null
         ? new BasicMessage("No file selected. Please select a source file.", MessageType.ERROR)
         : this.getDefaultMessage();
   }

   @Override
   public boolean canFinish() {
      return false;
   }

   @Override
   public void requestFocus() {
   }
}
