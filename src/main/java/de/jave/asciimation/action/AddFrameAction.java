package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public class AddFrameAction extends SmartAction {
   private final AnimationEditorModel model;

   public AddFrameAction(AnimationEditorModel model) {
      super(JaveIcons.ANIMATION_ADD_FRAME_ICON);
      Ensure.ensureArgumentNotNull(model);
      this.setToolTipText("Add new frame");
      this.model = model;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.addNewFrame(this.model.getCurrentFrameIndexModel().getCurrentFrameIndex());
      this.model.navigateNext();
   }
}
