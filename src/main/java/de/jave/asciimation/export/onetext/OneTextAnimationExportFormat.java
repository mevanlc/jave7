package de.jave.asciimation.export.onetext;

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

public class OneTextAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.TEXT_ICON;
   }

   @Override
   public String getName() {
      return "One text file with all frames";
   }

   @Override
   public String getDescription() {
      return "Generates a single text file containing all animation frames.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new OneTextAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(model, new AnimationOutputOptionsConfiguration(ExtensionFileFilters.TXT));
   }
}
