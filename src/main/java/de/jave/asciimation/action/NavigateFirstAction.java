package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.JavePlayerResources;
import java.awt.Component;

public class NavigateFirstAction extends AbstractNavigateAction {
   public NavigateFirstAction(AnimationEditorModel model) {
      super(JavePlayerResources.REVERSE_FIRST_ICON, model);
   }

   @Override
   protected boolean isEnabled(AnimationEditorModel model) {
      return model.getCurrentFrameIndexModel().getCurrentFrameIndex() > 0;
   }

   @Override
   protected void execute(Component parentComponent, AnimationEditorModel model) {
      model.navigateFirst();
   }
}
