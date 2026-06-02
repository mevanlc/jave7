package de.jave.asciimation.export;

import de.jave.jave.icon.JaveIcons;
import de.jave.jave.preferences.AnimationExportPreferences;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.dizzy.commons.swing.dialog.wizard.IWizardPage;
import net.dizzy.commons.swing.dialog.wizard.WizardDialog;

public class AnimationExportWizard extends AbstractWizardConfiguration {
   private final String title;
   private AnimationExportFormatPage exportFormatPage;
   private final AnimationExportWizardModel model;
   private final Map<IAnimationExportFormat, IWizardPage> optionsPageCache = new HashMap<>();
   private final IWizardPage optionalStartPage;

   public static AnimationExportOptions showOptionsDialogs(
      Component parent, FileModel currentDirectoryModel, String title, IWizardPage optionalStartPage, Font displayFont, AnimationExportPreferences preferences
   ) {
      AnimationExportWizardModel model = new AnimationExportWizardModel(currentDirectoryModel, displayFont, preferences);
      AnimationExportWizard wizard = new AnimationExportWizard(title, model, optionalStartPage);
      WizardDialog wizardDialog = new WizardDialog(parent, wizard);
      IDialogResult result = wizardDialog.show();
      return result.isCanceled() ? null : model.getExportOptions();
   }

   public AnimationExportWizard(String title, AnimationExportWizardModel model, IWizardPage optionalStartPage) {
      Ensure.ensureArgumentNotNull(title);
      Ensure.ensureArgumentNotNull(model);
      this.title = title;
      this.model = model;
      this.optionalStartPage = optionalStartPage;
   }

   @Override
   public void addPages() {
      this.exportFormatPage = new AnimationExportFormatPage(this.title, this.model);
      this.exportFormatPage.setWizard(this);
      if (this.optionalStartPage != null) {
         this.optionalStartPage.setWizard(this);
      }
   }

   @Override
   public IWizardPage getStartingPage() {
      return this.optionalStartPage != null ? this.optionalStartPage : this.exportFormatPage;
   }

   @Override
   public IWizardPage getNextPage(IWizardPage page) {
      if (page == this.exportFormatPage) {
         IAnimationExportFormat format = this.model.getExportOptions().getFormat();
         if (format == null) {
            return null;
         } else {
            if (!this.optionsPageCache.containsKey(format)) {
               IWizardPage optionsPage = format.createOptionsPage(this.model);
               optionsPage.setWizard(this);
               this.optionsPageCache.put(format, optionsPage);
            }

            return this.optionsPageCache.get(format);
         }
      } else {
         return page == this.optionalStartPage ? this.exportFormatPage : null;
      }
   }

   @Override
   public IWizardPage getPreviousPage(IWizardPage page) {
      if (page == this.exportFormatPage) {
         return this.optionalStartPage;
      } else {
         return page == this.optionalStartPage ? null : this.exportFormatPage;
      }
   }

   @Override
   public boolean isHelpAvailable() {
      return false;
   }

   @Override
   public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
      return DialogHeaderPanelConfiguration.createVisibleWithIcon(JaveIcons.LARGE_EXPORT_WIZARD_ICON);
   }
}
