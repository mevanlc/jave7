package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizard;
import de.jave.asciimation.export.AnimationFileExporter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.javeplayer.JaveAnimationFile;
import java.awt.Component;
import java.awt.Font;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class ExportAnimationAction extends SmartAction {
   private final AnimationEditorModel model;
   private final FileModel currentDirectoryModel;
   private final FontModel displayFontModel;
   private final AnimationExportPreferences preferences;

   public ExportAnimationAction(AnimationEditorModel model, FileModel currentDirectoryModel, FontModel displayFontModel, AnimationExportPreferences preferences) {
      super(JaveIcons.EXPORT_ANIMATION_ICON);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.displayFontModel = displayFontModel;
      this.model = model;
      this.currentDirectoryModel = currentDirectoryModel;
      this.setToolTipText("Export Animation");
   }

   @Override
   protected void execute(Component parentComponent) {
      performExport(parentComponent, this.model, this.currentDirectoryModel, this.displayFontModel.getFont(), this.preferences);
   }

   public static void performExport(
      Component parentComponent, AnimationEditorModel model, FileModel currentDirectoryModel, Font displayFont, AnimationExportPreferences preferences
   ) {
      AnimationExportOptions options = AnimationExportWizard.showOptionsDialogs(
         parentComponent, currentDirectoryModel, "Animation Export", null, displayFont, preferences
      );
      if (options != null) {
         JaveAnimationFile animationFile = model.getAnimationFile();
         AnimationFileExporter.performExport(parentComponent, animationFile, options);
      }
   }
}
