package net.disy.commons.swing.tree;

import net.disy.commons.core.util.Ensure;

public class SmartTreeModelNode<T> {
   private final SmartTreeModelNode<T> parent;
   private final T nodeInSmartTree;

   public SmartTreeModelNode(SmartTreeModelNode<T> parent, T node) {
      Ensure.ensureArgumentNotNull(node);
      this.parent = parent;
      this.nodeInSmartTree = node;
   }

   public SmartTreeModelNode<T> getParent() {
      return this.parent;
   }

   public T getNodeInSmartTree() {
      return this.nodeInSmartTree;
   }

   @Override
   public boolean equals(Object object) {
      if (!(object instanceof SmartTreeModelNode)) {
         return false;
      } else {
         SmartTreeModelNode other = (SmartTreeModelNode)object;
         return this.nodeInSmartTree.equals(other.nodeInSmartTree);
      }
   }

   @Override
   public int hashCode() {
      return this.nodeInSmartTree.hashCode();
   }
}
