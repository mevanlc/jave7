package de.jave.asciimation.export.scrollbar;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import de.jave.gui.io.ExtensionFileFilters;
import javax.swing.Icon;
import net.disy.commons.swing.dialog.wizard.IWizardPage;

public class ScrollbarAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.SCROLLBAR_ANIMATION_ICON;
   }

   @Override
   public String getName() {
      return "Scrollbar animation";
   }

   @Override
   public String getDescription() {
      return "Generates a text file that can be viewed by any text editor. Holding the 'Page Down' key will play the animation.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new ScrollbarAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(model, new AnimationOutputOptionsConfiguration(ExtensionFileFilters.TXT));
   }
}
