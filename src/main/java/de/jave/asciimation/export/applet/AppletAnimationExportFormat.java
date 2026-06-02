package de.jave.asciimation.export.applet;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.FileTypeIcons;
import de.jave.asciimation.export.IAnimationExportFormat;
import de.jave.asciimation.export.IAnimationExporter;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;

public class AppletAnimationExportFormat implements IAnimationExportFormat {
   @Override
   public Icon getIcon() {
      return FileTypeIcons.JAVA_ICON;
   }

   @Override
   public String getName() {
      return "Java Applet";
   }

   @Override
   public String getDescription() {
      return "Generates a simple Html page that uses a Java applet based player.";
   }

   @Override
   public IAnimationExporter createExporter(AnimationExportOptions options) {
      return new AppletAnimationExporter(options);
   }

   @Override
   public IWizardPage createOptionsPage(AnimationExportWizardModel model) {
      return new AppletAnimationOutputOptionsPage(model);
   }
}
