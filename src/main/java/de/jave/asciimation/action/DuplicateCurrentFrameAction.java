package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public class DuplicateCurrentFrameAction extends SmartAction {
   private final AnimationEditorModel model;

   public DuplicateCurrentFrameAction(AnimationEditorModel model) {
      super(JaveIcons.ANIMATION_DUPLICATE_FRAME_ICON);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Duplicate frame");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.duplicateFrame(this.model.getCurrentFrameIndexModel().getCurrentFrameIndex());
      this.model.navigateNext();
   }
}
