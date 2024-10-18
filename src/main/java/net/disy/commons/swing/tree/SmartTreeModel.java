package net.disy.commons.swing.tree;

import java.util.LinkedList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.core.util.ITransformer;

public class SmartTreeModel<T> implements TreeModel {
   private final transient ListenerList<TreeModelListener> listeners = new ListenerList<>();
   private final ISmartTree<T> smartTree;
   private ISmartTreeValueEditor<T> valueEditor;

   public SmartTreeModel(ISmartTree<T> smartTree) {
      Ensure.ensureArgumentNotNull(smartTree);
      this.smartTree = smartTree;
   }

   public SmartTreeModelNode<T> getRoot() {
      return new SmartTreeModelNode<>(null, this.smartTree.getRoot());
   }

   public SmartTreeModelNode<T> getChild(Object parent, int index) {
      SmartTreeModelNode<T> smartParent = (SmartTreeModelNode<T>)parent;
      return new SmartTreeModelNode<>(smartParent, this.smartTree.getChild(smartParent.getNodeInSmartTree(), index));
   }

   @Override
   public int getChildCount(Object parent) {
      return this.smartTree.getChildCount(this.getNodeInSmartTree(parent));
   }

   private T getNodeInSmartTree(Object node) {
      SmartTreeModelNode<T> smartNode = (SmartTreeModelNode<T>)node;
      return smartNode.getNodeInSmartTree();
   }

   public T[] getPathInSmartTree(SmartTreeModelNode<T>[] pathInModel) {
      return ArrayUtilities.transform(pathInModel, this.smartTree.getNodeClass(), new ITransformer<SmartTreeModelNode<T>, T>() {
         public T transform(SmartTreeModelNode<T> input) {
            return input.getNodeInSmartTree();
         }
      });
   }

   @Override
   public int getIndexOfChild(Object parent, Object child) {
      return this.smartTree.getIndexOfChild(this.getNodeInSmartTree(parent), this.getNodeInSmartTree(child));
   }

   @Override
   public boolean isLeaf(Object node) {
      return this.smartTree.isLeaf(this.getNodeInSmartTree(node));
   }

   public void setValueEditor(ISmartTreeValueEditor<T> valueEditor) {
      this.valueEditor = valueEditor;
   }

   @Override
   public void valueForPathChanged(TreePath path, Object newValue) {
      Object node = path.getLastPathComponent();
      T smartNode = this.getNodeInSmartTree(node);
      if (this.valueEditor != null) {
         this.valueEditor.changeValue(smartNode, newValue);
      }

      this.nodeChanged((SmartTreeModelNode<T>)node);
   }

   @Override
   public void addTreeModelListener(TreeModelListener listener) {
      this.listeners.add(listener);
   }

   @Override
   public void removeTreeModelListener(TreeModelListener listener) {
      this.listeners.remove(listener);
   }

   private void nodeChanged(SmartTreeModelNode<T> node) {
      SmartTreeModelNode<T> parent = node.getParent();
      if (parent != null) {
          this.fireTreeNodesChanged(
            this.getPathToRoot(parent),
            new int[]{this.smartTree.getIndexOfChild(parent.getNodeInSmartTree(), node.getNodeInSmartTree())},
                  new SmartTreeModelNode[]{node}
         );
      } else {
         this.fireTreeNodesChanged(this.getPathToRoot(node), null, null);
      }
   }

   public void nodeWasRemoved(SmartTreeModelNode<T> parent, int index, SmartTreeModelNode<T> child) {
      this.fireTreeNodesRemoved(this.getPathToRoot(parent), new int[]{index}, new SmartTreeModelNode[]{child});
   }

   public void nodeWasInserted(SmartTreeModelNode<T> parent, int index) {
      this.fireTreeNodesInserted(this.getPathToRoot(parent), new int[]{index}, new SmartTreeModelNode[]{this.getChild(parent, index)});
   }

   public SmartTreeModelNode<T>[] getPathToRoot(SmartTreeModelNode<T> node) {
      LinkedList<SmartTreeModelNode<T>> path = new LinkedList<>();

      while (node != null) {
         path.addFirst(node);
         node = node.getParent();
      }

      return path.toArray(new SmartTreeModelNode[0]);
   }

   public boolean isAncestorOrSame(SmartTreeModelNode<T> possibleAncestor, SmartTreeModelNode<T> possibleDescendant) {
      for (SmartTreeModelNode<T> actualNode = possibleDescendant; actualNode != null; actualNode = actualNode.getParent()) {
         if (actualNode == possibleAncestor) {
            return true;
         }
      }

      return false;
   }

   public SmartTreeModelNode<T>[] getPathInModel(T[] path) {
      SmartTreeModelNode<T>[] modelPath = new SmartTreeModelNode[path.length];
      SmartTreeModelNode<T> parent = null;

      for (int i = 0; i < path.length; i++) {
         parent = new SmartTreeModelNode<>(parent, path[i]);
         modelPath[i] = parent;
      }

      return modelPath;
   }

   private void fireTreeNodesChanged(final SmartTreeModelNode<T>[] path, final int[] childIndices, final SmartTreeModelNode<T>[] children) {
      this.listeners.forAllDo(new IClosure<TreeModelListener>() {
         public void execute(TreeModelListener listener) {
            listener.treeNodesChanged(new TreeModelEvent(this, path, childIndices, children));
         }
      });
   }

   private void fireTreeNodesRemoved(final SmartTreeModelNode<T>[] path, final int[] childIndices, final SmartTreeModelNode<T>[] children) {
      this.listeners.forAllDo(new IClosure<TreeModelListener>() {
         public void execute(TreeModelListener listener) {
            listener.treeNodesRemoved(new TreeModelEvent(this, path, childIndices, children));
         }
      });
   }

   private void fireTreeNodesInserted(final SmartTreeModelNode<T>[] path, final int[] childIndices, final SmartTreeModelNode<T>[] children) {
      this.listeners.forAllDo(new IClosure<TreeModelListener>() {
         public void execute(TreeModelListener listener) {
            listener.treeNodesInserted(new TreeModelEvent(this, path, childIndices, children));
         }
      });
   }
}
