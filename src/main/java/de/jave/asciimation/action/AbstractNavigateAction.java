package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import java.awt.Component;
import javax.swing.Icon;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public abstract class AbstractNavigateAction extends SmartAction {
   private final AnimationEditorModel model;

   public AbstractNavigateAction(Icon icon, AnimationEditorModel model) {
      super(icon);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      model.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractNavigateAction.this.updateEnabled();
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractNavigateAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   protected final AnimationEditorModel getModel() {
      return this.model;
   }

   private final void updateEnabled() {
      this.setEnabled(this.isEnabled(this.getModel()));
   }

   @Override
   protected final void execute(Component parentComponent) {
      this.execute(parentComponent, this.model);
   }

   protected abstract void execute(Component var1, AnimationEditorModel var2);

   protected abstract boolean isEnabled(AnimationEditorModel var1);
}
