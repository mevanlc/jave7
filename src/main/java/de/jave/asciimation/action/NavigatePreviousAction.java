package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.JavePlayerResources;
import java.awt.Component;

public class NavigatePreviousAction extends AbstractNavigateAction {
   public NavigatePreviousAction(AnimationEditorModel model) {
      super(JavePlayerResources.REVERSE_ICON, model);
   }

   @Override
   protected boolean isEnabled(AnimationEditorModel model) {
      return model.getCurrentFrameIndexModel().getCurrentFrameIndex() > 0;
   }

   @Override
   protected void execute(Component parentComponent, AnimationEditorModel model) {
      model.navigatePrevious();
   }
}
