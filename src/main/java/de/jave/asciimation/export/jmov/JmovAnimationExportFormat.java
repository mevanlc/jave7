package de.jave.asciimation.export.jmov;

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

public class JmovAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.ANIMATION_ICON;
   }

   @Override
   public String getName() {
      return "JMOV JavE Animation File";
   }

   @Override
   public String getDescription() {
      return "Generates a compact file in JavE's animation format.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new JmovAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new DefaultAnimationOutputOptionsPage(model, new AnimationOutputOptionsConfiguration(ExtensionFileFilters.JMOV));
   }
}
