package net.disy.commons.swing.smarttable.celleditors.action;

import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.columnsettings.IEditStoppedHandler;

public abstract class CellEditorAction extends SmartAction {
   private final IEditStoppedHandler handler;

   public CellEditorAction(IEditStoppedHandler handler) {
      Ensure.ensureArgumentNotNull(handler);
      this.handler = handler;
   }

   @Override
   protected void execute(Component parentComponent) {
      ICellEditResult result = this.performCellEdit(parentComponent);
      result.accept(new ICellEditResultVisitor() {
         @Override
         public void visitFinished(FinishedCellEditResult visitedResult) {
            CellEditorAction.this.handler.editFinished(visitedResult.getValue());
         }

         @Override
         public void visitCanceled(CanceledCellEditResult visitedResult) {
            CellEditorAction.this.handler.editCanceled();
         }
      });
   }

   protected abstract ICellEditResult performCellEdit(Component var1);
}
