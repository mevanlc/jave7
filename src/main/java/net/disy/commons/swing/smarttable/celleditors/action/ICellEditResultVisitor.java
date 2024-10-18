package net.disy.commons.swing.smarttable.celleditors.action;

public interface ICellEditResultVisitor {
   void visitFinished(FinishedCellEditResult var1);

   void visitCanceled(CanceledCellEditResult var1);
}
