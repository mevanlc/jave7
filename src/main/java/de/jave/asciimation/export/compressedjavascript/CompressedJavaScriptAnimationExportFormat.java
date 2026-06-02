package de.jave.asciimation.export.compressedjavascript;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class CompressedJavaScriptAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.JAVASCRIPT_ICON;
   }

   @Override
   public String getName() {
      return "Javascript animation";
   }

   @Override
   public String getDescription() {
      return "Generates a simple Html page containing a JavaScript based player with the animation.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new CompressedJavaScriptAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(
         model, new AnimationOutputOptionsConfiguration(new ExtensionFileFilter[]{ExtensionFileFilters.HTML}, true, true)
      );
   }
}
