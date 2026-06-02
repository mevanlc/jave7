package de.jave.asciimation.export.actionscript;

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

public class ActionScriptAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.ACTION_SCRIPT_ICON;
   }

   @Override
   public String getName() {
      return "ActionScript";
   }

   @Override
   public String getDescription() {
      return "Generates an ActionScript file that can be used for creating a Macromedia Flash animation (SWF).";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new ActionScriptAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(
         model, new AnimationOutputOptionsConfiguration(new ExtensionFileFilter[]{ExtensionFileFilters.ACTIONSCRIPT}, true, false)
      );
   }
}
