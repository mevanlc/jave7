package net.disy.commons.swing.smarttable.actions;

import java.awt.Component;
import javax.swing.Action;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public abstract class RemoveRowTableActionFactory implements ITableActionFactory {
   private final IActionConfiguration actionConfiguration;

   public RemoveRowTableActionFactory() {
      this(new ActionConfiguration("Entfernen", null, "AusgewÃ¤hlten Eintrag entfernen"));
   }

   public RemoveRowTableActionFactory(IActionConfiguration actionConfiguration) {
      Ensure.ensureArgumentNotNull(actionConfiguration);
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public Action createAction(SmartTable table) {
      ITableRowRemovePerformer removePerformer = new ITableRowRemovePerformer() {
         @Override
         public boolean performRemove(Component parentComponent, int rowIndex) {
            return RemoveRowTableActionFactory.this.performRemove(parentComponent, rowIndex);
         }
      };
      SmartAction action = new RemoveRowTableAction(this.actionConfiguration, table, removePerformer);
      if (table.isToolBarStyleButtons()) {
         action.setIcon(TableActionResources.DELETE_ROW_ICON);
      }

      return action;
   }

   protected abstract boolean performRemove(Component var1, int var2);
}
