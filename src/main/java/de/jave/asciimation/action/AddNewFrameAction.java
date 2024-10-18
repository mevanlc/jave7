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

public class AddNewFrameAction extends AbstractJaveAction {
   public AddNewFrameAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Add new frame", JaveIcons.ANIMATION_ADD_FRAME_ICON);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnimationEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      AnimationDocumentEditor animationDocumentEditor = (AnimationDocumentEditor)editor;
      AnimationEditorModel model = animationDocumentEditor.getModel();
      model.addNewFrame(model.getCurrentFrameIndexModel().getCurrentFrameIndex());
      model.navigateNext();
   }
}
