package net.disy.commons.swing.tree;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.ActionGroupId;
import net.disy.commons.swing.action.GroupedMenuItem;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class SmartTreeDeleteAddition<T> implements ISmartTreeAddition<T> {
   private final IActionConfiguration actionConfiguration;
   private final ISmartTreeDeleteStrategy<T> deleteStrategy;

   public SmartTreeDeleteAddition(ISmartTreeDeleteStrategy<T> deleteStrategy) {
      this(deleteStrategy, new ActionConfiguration(DisyCommonsSwingMessages.getString("SmartTreeRenameAddition.Delete.label")));
   }

   public SmartTreeDeleteAddition(ISmartTreeDeleteStrategy<T> deleteStrategy, IActionConfiguration actionConfiguration) {
      this.deleteStrategy = deleteStrategy;
      this.actionConfiguration = actionConfiguration;
   }

   @Override
   public void applyTo(final SmartTreeComponent<T> smartTreeComponent, final EnhancedJTree<T> tree) {
      smartTreeComponent.addActionFactory(new AbstractSmartTreeActionFactory<T>() {
         @Override
         public GroupedMenuItem createMenuItem(final T[] path) {
            SmartAction action = new SmartAction(SmartTreeDeleteAddition.this.actionConfiguration) {
               @Override
               protected void execute(Component parentComponent) {
                  SmartTreeDeleteAddition.this.deleteNode(smartTreeComponent, tree, path);
               }
            };
            action.setEnabled(SmartTreeDeleteAddition.this.isDeletable(smartTreeComponent.getSmartTree(), path[path.length - 1]));
            return new GroupedMenuItem(action, ActionGroupId.EDIT);
         }
      });
      tree.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent e) {
         }

         @Override
         public void keyReleased(KeyEvent e) {
            if (!smartTreeComponent.getSelectionModel().isSelectionEmpty()) {
               if (e.getKeyCode() == 127) {
                  if (SmartTreeDeleteAddition.this.isDeletable(smartTreeComponent.getSmartTree(), smartTreeComponent.getSelectionModel().getSelectedNode())) {
                     SmartTreeDeleteAddition.this.deleteNode(smartTreeComponent, tree, smartTreeComponent.getSelectionModel().getSelectionPath());
                  }
               }
            }
         }

         @Override
         public void keyPressed(KeyEvent e) {
         }
      });
   }

   private boolean isDeletable(ISmartTree<T> smartTree, T node) {
      return !node.equals(smartTree.getRoot()) && this.deleteStrategy.isDeletable(node);
   }

   private void deleteNode(SmartTreeComponent<T> smartTreeComponent, EnhancedJTree<T> tree, T[] path) {
      T parent = path.length > 1 ? path[path.length - 2] : null;
      T node = path[path.length - 1];
      SmartTreeModel<T> smartTreeModel = tree.getSmartTreeModel();
      SmartTreeModelNode<T>[] modelPath = smartTreeModel.getPathInModel(path);
      int index = smartTreeComponent.getSmartTree().getIndexOfChild(parent, node);
      SmartTreeModelNode<T> modelParent = modelPath[modelPath.length - 2];
      SmartTreeModelNode<T> modelNode = modelPath[modelPath.length - 1];
      if (this.deleteStrategy.deleteNode(tree, parent, index, node)) {
         smartTreeModel.nodeWasRemoved(modelParent, index, modelNode);
      }
   }
}
