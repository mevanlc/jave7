package net.disy.commons.swing.tree;

import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class SmartTreeRenameAddition<T> implements ISmartTreeAddition<T> {
   private final IActionConfiguration actionConfiguration;
   private final ISmartTreeRenameStrategy<T> renameStrategy;

   public SmartTreeRenameAddition(ISmartTreeRenameStrategy<T> renameStrategy) {
      this(renameStrategy, new ActionConfiguration(DisyCommonsSwingMessages.getString("SmartTreeRenameAddition.Rename.label")));
   }

   public SmartTreeRenameAddition(ISmartTreeRenameStrategy<T> renameStrategy, IActionConfiguration actionConfiguration) {
      this.renameStrategy = renameStrategy;
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public void applyTo(SmartTreeComponent<T> smartTree, EnhancedJTree<T> tree) {
      tree.setEditable(true);
      SmartTreeRenameCellEditor.attachTo(tree, this.renameStrategy);
      tree.getSmartTreeModel().setValueEditor(new ISmartTreeValueEditor<T>() {
         @Override
         public void changeValue(T smartNode, Object newValue) {
            SmartTreeRenameAddition.this.renameStrategy.renameTo(smartNode, (String)newValue);
         }
      });
      smartTree.addActionFactory(new RenameSmartTreeActionFactory<>(smartTree, this.renameStrategy, this.actionConfiguration));
   }
}
