package de.jave.asciimation.export.multipletexts;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import de.jave.gui.io.ExtensionFileFilters;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class MultipleTextsAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.TEXTS_ICON;
   }

   @Override
   public String getName() {
      return "Multiple text files with one frame each";
   }

   @Override
   public String getDescription() {
      return "Generates one text file for each frame.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new MultipleTextsAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(model, new AnimationOutputOptionsConfiguration(ExtensionFileFilters.TXT));
   }
}
