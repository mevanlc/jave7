package net.disy.commons.swing.tree;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.disy.commons.core.model.AbstractChangeableModel;

public class SmartTreeSelectionModel<T> extends AbstractChangeableModel {
   private final TreeSelectionModel selectionModel;
   private final SmartTreeModel<T> treeModel;

   public SmartTreeSelectionModel(TreeSelectionModel selectionModel, SmartTreeModel<T> treeModel) {
      this.selectionModel = selectionModel;
      this.treeModel = treeModel;
      selectionModel.addTreeSelectionListener(new TreeSelectionListener() {
         @Override
         public void valueChanged(TreeSelectionEvent e) {
            SmartTreeSelectionModel.this.fireChangeEvent();
         }
      });
   }

   public void setSelectionPath(T[] path) {
      this.selectionModel.setSelectionPath(new TreePath(this.treeModel.getPathInModel(path)));
   }

   public T[] getSelectionPath() {
      TreePath selectionPath = this.selectionModel.getSelectionPath();
       if (selectionPath == null) return null;
      SmartTreeModelNode<T>[] path = (SmartTreeModelNode<T>[]) selectionPath.getPath();
      var pathInSmartTree = this.treeModel.getPathInSmartTree(path);
      return pathInSmartTree;
   }

   public T getSelectedNode() {
      T[] selectionPath = this.getSelectionPath();
      return selectionPath == null ? null : selectionPath[selectionPath.length - 1];
   }

   public boolean isSelectionEmpty() {
      return this.selectionModel.isSelectionEmpty();
   }
}
