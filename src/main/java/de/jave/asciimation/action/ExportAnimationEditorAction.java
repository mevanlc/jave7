package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.AnimationEditorOnlyEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.AnimationExportPreferences;
import java.awt.Component;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class ExportAnimationEditorAction extends AbstractJaveAction {
   private final FileModel currentDirectoryModel;
   private final FontModel displayFontModel;
   private final AnimationExportPreferences animationExportPreferences;

   public ExportAnimationEditorAction(
      JaveMainPanel mainPanel, FileModel currentDirectoryModel, FontModel displayFontModel, AnimationExportPreferences animationExportPreferences
   ) {
      super(mainPanel, "Export...", JaveIcons.EXPORT_ANIMATION_ICON);
      Ensure.ensureArgumentNotNull(animationExportPreferences);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(displayFontModel);
      this.animationExportPreferences = animationExportPreferences;
      this.currentDirectoryModel = currentDirectoryModel;
      this.displayFontModel = displayFontModel;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      AnimationDocumentEditor animationDocumentEditor = (AnimationDocumentEditor)editor;
      AnimationEditorModel model = animationDocumentEditor.getModel();
      ExportAnimationAction.performExport(parentComponent, model, this.currentDirectoryModel, this.displayFontModel.getFont(), this.animationExportPreferences);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnimationEditorOnlyEnabledStrategy.getInstance();
   }
}
