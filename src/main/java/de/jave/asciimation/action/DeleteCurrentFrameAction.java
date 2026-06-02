package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public class DeleteCurrentFrameAction extends SmartAction {
   private final AnimationEditorModel model;

   public DeleteCurrentFrameAction(AnimationEditorModel model) {
      super(JaveIcons.ANIMATION_DELETE_FRAME_ICON);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Delete frame");
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DeleteCurrentFrameAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(this.model.getAnimationFile().getFrameCount() > 1);
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.deleteFrame(this.model.getCurrentFrameIndexModel().getCurrentFrameIndex());
   }
}
