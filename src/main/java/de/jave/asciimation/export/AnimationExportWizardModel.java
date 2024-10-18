package de.jave.asciimation.export;

import de.jave.jave.preferences.AnimationExportPreferences;
import java.awt.Font;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.util.Ensure;

public class AnimationExportWizardModel {
   private final AnimationExportOptions options;
   private final FileModel currentDirectoryModel;
   private final Font displayFont;

   public AnimationExportWizardModel(FileModel currentDirectoryModel, Font displayFont, AnimationExportPreferences preferences) {
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(displayFont);
      Ensure.ensureArgumentNotNull(preferences);
      this.options = new AnimationExportOptions(preferences);
      this.currentDirectoryModel = currentDirectoryModel;
      this.displayFont = displayFont;
   }

   public AnimationExportOptions getExportOptions() {
      return this.options;
   }

   public FileModel getCurrentDirectoryModel() {
      return this.currentDirectoryModel;
   }

   public Font getDisplayFont() {
      return this.displayFont;
   }
}
