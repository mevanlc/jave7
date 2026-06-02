package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import java.awt.Component;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.resources.DisyCommonsSwingIconResources;

public class RedoAction extends SmartAction {
   private final JavEApplication application;
   private final UndoRedoModel model;
   private final boolean nameIsToolTip;

   public RedoAction(JavEApplication application, UndoRedoModel model, boolean nameIsToolTip) {
      super(DisyCommonsSwingIconResources.REDO_MODERN);
      Ensure.ensureArgumentNotNull(application);
      Ensure.ensureArgumentNotNull(model);
      this.application = application;
      this.model = model;
      this.nameIsToolTip = nameIsToolTip;
      this.setAcceleratorKey(JaveKeyBindings.REDO);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            RedoAction.this.updateAction();
         }
      });
      this.updateAction();
   }

   private void updateAction() {
      this.setEnabled(this.model.canRedo());
      String label;
      if (this.isEnabled()) {
         label = "Redo " + this.model.getRedoActionName();
      } else {
         label = "Redo";
      }

      if (this.nameIsToolTip) {
         this.setToolTipText(label);
      } else {
         this.setName(label);
      }
   }

   @Override
   protected void execute(Component parentComponent) {
      this.application.doRedo();
   }
}
