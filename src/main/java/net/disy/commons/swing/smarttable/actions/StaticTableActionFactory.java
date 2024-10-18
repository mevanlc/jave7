package net.disy.commons.swing.smarttable.actions;

import javax.swing.Action;
import net.disy.commons.swing.smarttable.SmartTable;

public final class StaticTableActionFactory implements ITableActionFactory {
   private final Action constructedAction;

   public StaticTableActionFactory(Action constructedAction) {
      this.constructedAction = constructedAction;
   }

   @Override
   public Action createAction(SmartTable table) {
      return this.constructedAction;
   }
}
