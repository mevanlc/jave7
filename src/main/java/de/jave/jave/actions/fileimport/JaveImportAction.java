package de.jave.jave.actions.fileimport;

import de.jave.jave.JavEApplication;
import de.jave.jave.icon.JaveIcons;
import de.jave.javeplayer.JaveAnimationFile;
import java.awt.Component;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.wizard.IWizardConfiguration;
import net.dizzy.commons.swing.dialog.wizard.WizardDialog;

public class JaveImportAction extends SmartAction {
   private final FileModel currentDirectoryModel;
   private final JavEApplication jave;

   public JaveImportAction(JavEApplication jave, FileModel currentDirectoryModel) {
      super("Import...", JaveIcons.IMPORT_ICON);
      Ensure.ensureArgumentNotNull(jave);
      this.jave = jave;
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.currentDirectoryModel = currentDirectoryModel;
   }

   @Override
   protected void execute(Component parentComponent) {
      ImportWizardModel model = new ImportWizardModel(this.currentDirectoryModel);
      IWizardConfiguration wizard = new ImportWizard(model);
      WizardDialog dialog = new WizardDialog(parentComponent, wizard);
      IDialogResult result = dialog.show();
      if (!result.isCanceled()) {
         JaveAnimationFile animationFile = model.getImportedAnimationModel().getAnimationFile();
         this.jave.openJaveAnimation(animationFile);
      }
   }
}
