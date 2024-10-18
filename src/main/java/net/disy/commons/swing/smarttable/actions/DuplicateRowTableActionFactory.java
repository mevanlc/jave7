package net.disy.commons.swing.smarttable.actions;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public abstract class DuplicateRowTableActionFactory implements ITableActionFactory {
   private final IActionConfiguration actionConfiguration;

   public DuplicateRowTableActionFactory() {
      this(new ActionConfiguration("Duplizieren", null, "AusgewÃ¤hlten Eintrag duplizieren"));
   }

   public DuplicateRowTableActionFactory(IActionConfiguration actionConfiguration) {
      Ensure.ensureArgumentNotNull(actionConfiguration);
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public Action createAction(final SmartTable table) {
      final SmartAction smartAction = new SmartAction(this.actionConfiguration) {
         @Override
         protected void execute(Component parentComponent) {
            int index = table.getSelectedRowIndex();
            boolean success = DuplicateRowTableActionFactory.this.performDuplicate(parentComponent, index);
            if (success) {
               table.getTable().getSelectionModel().setSelectionInterval(index + 1, index + 1);
            }
         }
      };
      table.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            DuplicateRowTableActionFactory.this.updateActionEnabled(table, smartAction);
         }
      });
      this.updateActionEnabled(table, smartAction);
      return smartAction;
   }

   private void updateActionEnabled(SmartTable table, SmartAction smartAction) {
      smartAction.setEnabled(table.getSelectedRowIndex() != -1);
   }

   protected abstract boolean performDuplicate(Component var1, int var2);
}
