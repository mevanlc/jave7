package net.disy.commons.swing.undo;

import java.awt.Component;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class RedoAction extends SmartAction {
   private final UndoManager<?> undoManager;

   public RedoAction(UndoManager<?> undoManager) {
      super(DisyCommonsSwingMessages.getString("RedoAction.name"), DisyCommonsSwingIconResources.REDO);
      this.undoManager = undoManager;
      undoManager.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            RedoAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(this.undoManager.isRedoPossible());
   }

   @Override
   protected void execute(Component parentComponent) {
      this.undoManager.redo();
   }
}
