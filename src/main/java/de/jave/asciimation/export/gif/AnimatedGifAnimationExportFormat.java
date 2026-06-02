package de.jave.asciimation.export.gif;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class AnimatedGifAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.ANIMATED_GIF_ICON;
   }

   @Override
   public String getName() {
      return "Animated GIF";
   }

   @Override
   public String getDescription() {
      return "Generates an animated GIF image playing the animation.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new AnimatedGifAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new GifAnimationsOutputOptionsPage(model, true);
   }
}
