package net.disy.commons.swing.smarttable;

import javax.swing.table.AbstractTableModel;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IExceptionThrowingBlock;

public abstract class AbstractChangeableModelTableModel<T extends IChangeableModel> extends AbstractTableModel {
   private boolean autoEventsSuppressed = false;
   private final T model;

   protected AbstractChangeableModelTableModel(T model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (!AbstractChangeableModelTableModel.this.autoEventsSuppressed) {
               AbstractChangeableModelTableModel.this.fireTableDataChanged();
            }
         }
      });
   }

   protected final T getModel() {
      return this.model;
   }

   protected final void commitRowModification(int rowIndex, IExceptionThrowingBlock<RuntimeException> commitBlock) {
      synchronized (this.model) {
         try {
            this.autoEventsSuppressed = true;
            commitBlock.execute();
            this.fireTableRowsUpdated(rowIndex, rowIndex);
         } finally {
            this.autoEventsSuppressed = false;
         }
      }
   }

   protected final void commitCellModification(int rowIndex, int columnIndex, IExceptionThrowingBlock<RuntimeException> commitBlock) {
      synchronized (this.model) {
         try {
            this.autoEventsSuppressed = true;
            commitBlock.execute();
            this.fireTableCellUpdated(rowIndex, columnIndex);
         } finally {
            this.autoEventsSuppressed = false;
         }
      }
   }
}
