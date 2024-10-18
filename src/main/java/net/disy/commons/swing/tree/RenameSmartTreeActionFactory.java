package net.disy.commons.swing.tree;

import java.awt.Component;
import net.disy.commons.swing.action.ActionGroupId;
import net.disy.commons.swing.action.GroupedMenuItem;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;

public class RenameSmartTreeActionFactory<T> extends AbstractSmartTreeActionFactory<T> {
   private final SmartTreeComponent<T> smartTree;
   private final IActionConfiguration actionConfiguration;
   private final ISmartTreeRenameStrategy<T> renameStrategy;

   public RenameSmartTreeActionFactory(SmartTreeComponent<T> smartTree, ISmartTreeRenameStrategy<T> renameStrategy, IActionConfiguration actionConfiguration) {
      this.smartTree = smartTree;
      this.renameStrategy = renameStrategy;
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public GroupedMenuItem createMenuItem(final T[] path) {
      SmartAction action = new SmartAction(this.actionConfiguration) {
         @Override
         protected void execute(Component parentComponent) {
            RenameSmartTreeActionFactory.this.smartTree.startEditing(path);
         }
      };
      action.setEnabled(this.renameStrategy.isRenameable(path[path.length - 1]));
      return new GroupedMenuItem(action, ActionGroupId.EDIT);
   }
}
