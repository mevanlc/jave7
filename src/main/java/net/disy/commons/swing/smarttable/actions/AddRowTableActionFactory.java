package net.disy.commons.swing.smarttable.actions;

import javax.swing.Action;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.smarttable.SmartTable;

public class AddRowTableActionFactory implements ITableActionFactory {
   private final IActionConfiguration actionConfiguration;
   private final IAdditionPerformer additionPerformer;

   public AddRowTableActionFactory(IAdditionPerformer additionPerformer) {
      this(additionPerformer, new ActionConfiguration("HinzufÃ¼gen", null, "Eintrag hinzufÃ¼gen"));
   }

   public AddRowTableActionFactory(IAdditionPerformer additionPerformer, IActionConfiguration actionConfiguration) {
      Ensure.ensureArgumentNotNull(actionConfiguration);
      this.additionPerformer = additionPerformer;
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public Action createAction(SmartTable table) {
      SmartAction action = new AddRowTableAction(this.actionConfiguration, table, this.additionPerformer);
      if (table.isToolBarStyleButtons() && action.getIcon() == null) {
         action.setIcon(TableActionResources.ADD_ROW_ICON);
      }

      return action;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof AddRowTableActionFactory)) {
         return false;
      } else {
         AddRowTableActionFactory other = (AddRowTableActionFactory)obj;
         return ObjectUtilities.equals(this.additionPerformer, other.additionPerformer)
            && ObjectUtilities.equals(this.actionConfiguration, other.actionConfiguration);
      }
   }

   @Override
   public int hashCode() {
      return this.additionPerformer.hashCode();
   }
}
