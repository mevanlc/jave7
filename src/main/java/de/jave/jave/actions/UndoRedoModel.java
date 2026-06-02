package de.jave.jave.actions;

import de.jave.jave.plate.JaveMainPanel;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class UndoRedoModel extends AbstractChangeableModel {
   private final JaveMainPanel mainPanel;

   public UndoRedoModel(JaveMainPanel mainPanel) {
      Ensure.ensureArgumentNotNull(mainPanel);
      this.mainPanel = mainPanel;
      mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            UndoRedoModel.this.fireChangeEvent();
         }
      });
   }

   public boolean canRedo() {
      return this.mainPanel.canRedo();
   }

   public boolean canUndo() {
      return this.mainPanel.canUndo();
   }

   @Override
   public void fireChangeEvent() {
      super.fireChangeEvent();
   }

   public String getRedoActionName() {
      return this.mainPanel.getRedoActionName();
   }

   public String getUndoActionName() {
      return this.mainPanel.getUndoActionName();
   }
}
