package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.JavePlayerResources;
import java.awt.Component;

public class NavigateLastAction extends AbstractNavigateAction {
   public NavigateLastAction(AnimationEditorModel model) {
      super(JavePlayerResources.FORWARD_LAST_ICON, model);
   }

   @Override
   protected void execute(Component parentComponent, AnimationEditorModel model) {
      model.navigateLast();
   }

   @Override
   protected boolean isEnabled(AnimationEditorModel model) {
      return model.getCurrentFrameIndexModel().getCurrentFrameIndex() < model.getAnimationFile().getFrameCount() - 1;
   }
}
