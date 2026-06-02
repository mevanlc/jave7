package de.jave.asciimation.export.gif;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class MultipleGifsAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.MULTIPLE_GIF_ICON;
   }

   @Override
   public String getName() {
      return "Multiple GIF images";
   }

   @Override
   public String getDescription() {
      return "Generates one GIF image file for each frame.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new MultipleGifsAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new GifAnimationsOutputOptionsPage(model, false);
   }
}
