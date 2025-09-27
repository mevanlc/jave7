package net.disy.commons.swing.smarttable.celleditors.action;

import net.disy.commons.core.util.Ensure;

public class FinishedCellEditResult implements ICellEditResult {
   private final Object value;

   public FinishedCellEditResult(Object value) {
      Ensure.ensureArgumentNotNull(value);
      this.value = value;
   }

   @Override
   public void accept(ICellEditResultVisitor visitor) {
      visitor.visitFinished(this);
   }

   public Object getValue() {
      return this.value;
   }
}
