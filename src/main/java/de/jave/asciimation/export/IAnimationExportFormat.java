package de.jave.asciimation.export;

import javax.swing.Icon;
import net.disy.commons.swing.dialog.wizard.IWizardPage;

public interface IAnimationExportFormat {
   Icon getIcon();

   String getName();

   String getDescription();

   IAnimationExporter createExporter(AnimationExportOptions var1);

   IWizardPage createOptionsPage(AnimationExportWizardModel var1);
}
