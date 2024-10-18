package de.jave.jave.actions.fileimport;

import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class ImportWizardModel {
   private final FileModel currentDirectoryModel;
   private final FileModel sourceFileModel = new FileModel();
   private final AsciimationOptionsModel asciimationOptionsModel = new AsciimationOptionsModel();
   private final ImportAnimationModel importModel = new ImportAnimationModel();
   private String[] fileContent;

   public ImportWizardModel(FileModel currentDirectoryModel) {
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
      this.sourceFileModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ImportWizardModel.this.fileContent = null;
         }
      });
   }

   public FileModel getCurrentDirectoryModel() {
      return this.currentDirectoryModel;
   }

   public FileModel getSourceFileModel() {
      return this.sourceFileModel;
   }

   public AsciimationOptionsModel getAsciimationOptionsModel() {
      return this.asciimationOptionsModel;
   }

   public ImportAnimationModel getImportedAnimationModel() {
      return this.importModel;
   }

   public String[] getSourceFileContent() {
      return this.fileContent;
   }

   public void setSourceFileContent(String[] fileContent) {
      this.fileContent = fileContent;
   }
}
