package net.disy.commons.swing.smarttable.celleditors.action;

public class CanceledCellEditResult implements ICellEditResult {
   @Override
   public void accept(ICellEditResultVisitor visitor) {
      visitor.visitCanceled(this);
   }
}
