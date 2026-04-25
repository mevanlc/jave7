package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import java.awt.Component;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class UndoAction extends SmartAction {
   private final JavEApplication application;
   private final UndoRedoModel model;
   private final boolean nameIsToolTip;

   public UndoAction(JavEApplication application, UndoRedoModel model, boolean nameIsToolTip) {
      super(DisyCommonsSwingIconResources.UNDO_MODERN);
      Ensure.ensureArgumentNotNull(application);
      Ensure.ensureArgumentNotNull(model);
      this.application = application;
      this.model = model;
      this.nameIsToolTip = nameIsToolTip;
      this.setAcceleratorKey(JaveKeyBindings.UNDO);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            UndoAction.this.updateAction();
         }
      });
      this.updateAction();
   }

   private void updateAction() {
      this.setEnabled(this.model.canUndo());
      String label;
      if (this.isEnabled()) {
         label = "Undo " + this.model.getUndoActionName();
      } else {
         label = "Undo";
      }

      if (this.nameIsToolTip) {
         this.setToolTipText(label);
      } else {
         this.setName(label);
      }
   }

   @Override
   protected void execute(Component parentComponent) {
      this.application.doUndo();
   }
}
