package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.AnimationEditorOnlyEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class AnimationEditorPropertiesAction extends AbstractJaveAction {
   public AnimationEditorPropertiesAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Properties", JaveIcons.PROPERTIES_ICON);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnimationEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      AnimationDocumentEditor animationDocumentEditor = (AnimationDocumentEditor)editor;
      AnimationEditorModel model = animationDocumentEditor.getModel();
      AnimationPropertiesAction.showPropertiesDialog(parentComponent, model);
   }
}
