package net.disy.commons.swing.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;

public final class TreeDoubleClickMouseActionListener<T> extends MouseAdapter {
   private final ListenerList<ITreeNodeActionListener<T>> nodeActionListeners;
   private int lastRowClickIndex = -1;

   public TreeDoubleClickMouseActionListener(ListenerList<ITreeNodeActionListener<T>> nodeActionListeners) {
      Ensure.ensureArgumentNotNull(nodeActionListeners);
      this.nodeActionListeners = nodeActionListeners;
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      JTree tree = (JTree)e.getSource();
      if (e.getClickCount() == 1) {
         this.lastRowClickIndex = tree.getRowForLocation(e.getX(), e.getY());
      }

      if (e.getClickCount() == 2 && !e.isMetaDown()) {
         int rowIndex = tree.getRowForLocation(e.getX(), e.getY());
         if (rowIndex != -1 && this.lastRowClickIndex == rowIndex) {
            this.lastRowClickIndex = -1;
            TreePath path = tree.getPathForRow(rowIndex);
            this.fireNodeActionPerformed(tree, (SmartTreeModelNode<T>)path.getLastPathComponent());
         } else {
            this.lastRowClickIndex = -1;
         }
      }
   }

   private final void fireNodeActionPerformed(final JTree tree, final SmartTreeModelNode<T> node) {
      this.nodeActionListeners.forAllDo(new IClosure<ITreeNodeActionListener<T>>() {
         public void execute(ITreeNodeActionListener<T> listener) {
            listener.nodeActionPerformed(tree, node.getNodeInSmartTree());
         }
      });
   }
}
