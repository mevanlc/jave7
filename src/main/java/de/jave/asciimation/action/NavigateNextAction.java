package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.JavePlayerResources;
import java.awt.Component;

public class NavigateNextAction extends AbstractNavigateAction {
   public NavigateNextAction(AnimationEditorModel model) {
      super(JavePlayerResources.FORWARD_ICON, model);
   }

   @Override
   protected void execute(Component parentComponent, AnimationEditorModel model) {
      model.navigateNext();
   }

   @Override
   protected boolean isEnabled(AnimationEditorModel model) {
      return model.getCurrentFrameIndexModel().getCurrentFrameIndex() < model.getAnimationFile().getFrameCount() - 1;
   }
}
