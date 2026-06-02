package de.jave.asciimation.export.swf;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class SwfAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.SWF_ICON;
   }

   @Override
   public String getName() {
      return "Macromedia Flash File Format (SWF)";
   }

   @Override
   public String getDescription() {
      return "Generates a Macromedia Flash animation (SWF) by using the open source Motion-Twin ActionScript 2 Compiler (MTASC).";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new SwfAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new SwfAnimationOutputOptionsPage(model);
   }
}
