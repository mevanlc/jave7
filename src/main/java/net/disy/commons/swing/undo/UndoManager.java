package net.disy.commons.swing.undo;

import java.util.ArrayList;
import java.util.List;
import net.disy.commons.core.model.AbstractChangeableModel;

public class UndoManager<T> extends AbstractChangeableModel {
   private final IUndoStateManager<T> stateManager;
   private T currentState;
   private final List<T> undoStates = new ArrayList<>();
   private final List<T> redoStates = new ArrayList<>();

   public UndoManager(IUndoStateManager<T> stateManager) {
      this.stateManager = stateManager;
      this.reset();
   }

   public void reset() {
      this.currentState = this.stateManager.getState();
      this.undoStates.clear();
      this.redoStates.clear();
      this.fireChangeEvent();
   }

   public boolean isUndoPossible() {
      return !this.undoStates.isEmpty();
   }

   public boolean isRedoPossible() {
      return !this.redoStates.isEmpty();
   }

   public void addUndoPoint() {
      this.undoStates.add(this.currentState);
      this.currentState = this.stateManager.getState();
      this.redoStates.clear();
      this.fireChangeEvent();
   }

   public void undo() {
      this.moveState(this.undoStates, this.redoStates);
   }

   public void redo() {
      this.moveState(this.redoStates, this.undoStates);
   }

   private void moveState(List<T> from, List<T> to) {
      to.add(this.currentState);
      int lastFromIndex = from.size() - 1;
      this.currentState = from.get(lastFromIndex);
      this.stateManager.setState(this.currentState);
      from.remove(lastFromIndex);
      this.fireChangeEvent();
   }
}
