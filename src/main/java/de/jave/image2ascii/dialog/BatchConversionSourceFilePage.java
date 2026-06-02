package de.jave.image2ascii.dialog;

import java.io.File;
import javax.swing.JComponent;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.wizard.AbstractWizardPage;

public class BatchConversionSourceFilePage extends AbstractWizardPage {
   private final FileModel currentDirectoryModel;
   private final BatchSourceImageModel model;
   private SourceFileListPanel sourceFileListPanel;

   public BatchConversionSourceFilePage(FileModel currentDirectoryModel) {
      super(
         "Source Images",
         "Batch Conversion",
         "Add source files for batch conversion. The files will be converted in the listed order using the current converter settings."
      );
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
      this.model = new BatchSourceImageModel(BatchSourceImagesFinder.findSourceFiles(currentDirectoryModel.getValue()));
      this.model.addChangeListener(this.getCheckInputValidListener());
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.model.getFileCount() == 0
         ? new BasicMessage("There are no source image files selected. Please select source files.", MessageType.ERROR)
         : this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      this.sourceFileListPanel = new SourceFileListPanel(this.model, this.currentDirectoryModel);
      return this.sourceFileListPanel.getContent();
   }

   @Override
   public boolean canFlipToNextPage() {
      if (this.getNextPage() == null) {
         return false;
      } else {
         return this.getMessage().getType() == MessageType.ERROR ? false : this.createCurrentMessage().getType() != MessageType.ERROR;
      }
   }

   @Override
   public boolean canFinish() {
      return false;
   }

   @Override
   public void performHelp() {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isHelpAvailable() {
      return false;
   }

   @Override
   public void requestFocus() {
      this.sourceFileListPanel.requestFocus();
   }

   public File[] getSourceFiles() {
      return this.model.getFiles();
   }
}
