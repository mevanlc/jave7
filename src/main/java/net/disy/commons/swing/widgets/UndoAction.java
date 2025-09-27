package net.disy.commons.swing.undo;

import java.awt.Component;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class UndoAction extends SmartAction {
   private final UndoManager<?> undoManager;

   public UndoAction(UndoManager<?> undoManager) {
      super(DisyCommonsSwingMessages.getString("UndoAction.name"), DisyCommonsSwingIconResources.UNDO);
      this.undoManager = undoManager;
      undoManager.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            UndoAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(this.undoManager.isUndoPossible());
   }

   @Override
   protected void execute(Component parentComponent) {
      this.undoManager.undo();
   }
}
