package de.jave.lib.tree.checkbox;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class CheckboxTreeEditor extends AbstractCheckboxTreeRenderer implements TreeCellEditor {
   private final EventListenerList listenerList = new EventListenerList();
   private final CheckBoxCheckListener checkListener;
   private final TreeCellRenderer defaultRenderer;

   public CheckboxTreeEditor(ICheckBoxNodeAccess nodeAccess, CheckBoxSelectionMode mode, TreeCellRenderer defaultRenderer) {
      super(nodeAccess);
      if (defaultRenderer == null) {
         defaultRenderer = new DefaultTreeCellRenderer();
      }

      this.defaultRenderer = defaultRenderer;
      this.checkListener = new CheckBoxCheckListener(nodeAccess, mode, this);
      this.checkBox.addActionListener(this.checkListener);
   }

   @Override
   public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
      this.checkListener.setTree(tree);
      if (value instanceof TreeNode && this.getNodeAccess().isCheckableNode((TreeNode)value)) {
         TreeNode node = (TreeNode)value;
         boolean treeIsEnabled = tree.isEnabled();
         boolean nodeIsEnabled = this.getNodeAccess().isCheckEditable(node);
         boolean isEnabled = treeIsEnabled && nodeIsEnabled;
         this.setEnabled(isEnabled);
         if (isEnabled) {
            this.checkBox.setEnabled(true);
            this.checkBox.setState(this.getRenderState(node));
            this.checkListener.setNodeToCheck(node);
         } else {
            this.checkBox.setEnabled(false);
         }

         return this.updateLabel(tree, this.defaultRenderer, node, true, expanded, leaf, row, true);
      } else {
         return this.defaultRenderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, leaf);
      }
   }

   @Override
   public void cancelCellEditing() {
      this.fireEditingCanceled();
   }

   @Override
   public Object getCellEditorValue() {
      return this.checkBox.isSelected();
   }

   @Override
   public boolean isCellEditable(EventObject evt) {
      boolean editable = false;
      if (evt instanceof MouseEvent && evt.getSource() instanceof JTree) {
         MouseEvent me = (MouseEvent)evt;
         JTree tree = (JTree)evt.getSource();
         TreePath path = tree.getPathForLocation(me.getX(), me.getY());
         if (path != null) {
            TreeNode node = (TreeNode)path.getLastPathComponent();
            if (this.getNodeAccess().isCheckableNode(node)) {
               Rectangle rect = tree.getPathBounds(path);
               int x = me.getX() - rect.x;
               int y = me.getY() - rect.y;
               this.getTreeCellEditorComponent(tree, node, tree.isPathSelected(path), tree.isExpanded(path), node.isLeaf(), tree.getRowForPath(path));
               this.doLayout();
               editable = this.checkBox.getBounds().contains(x, y);
            }
         }
      }

      return editable;
   }

   @Override
   public boolean shouldSelectCell(EventObject evt) {
      return true;
   }

   @Override
   public boolean stopCellEditing() {
      this.fireEditingStopped();
      return true;
   }

   @Override
   public void addCellEditorListener(CellEditorListener l) {
      this.listenerList.add(CellEditorListener.class, l);
   }

   @Override
   public void removeCellEditorListener(CellEditorListener l) {
      this.listenerList.remove(CellEditorListener.class, l);
   }

   private void fireEditingStopped() {
      Object[] listeners = this.listenerList.getListenerList();
      ChangeEvent changeEvent = new ChangeEvent(this);

      for (int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == CellEditorListener.class) {
            ((CellEditorListener)listeners[i + 1]).editingStopped(changeEvent);
         }
      }
   }

   private void fireEditingCanceled() {
      Object[] listeners = this.listenerList.getListenerList();
      ChangeEvent changeEvent = new ChangeEvent(this);

      for (int i = listeners.length - 2; i >= 0; i -= 2) {
         if (listeners[i] == CellEditorListener.class) {
            ((CellEditorListener)listeners[i + 1]).editingCanceled(changeEvent);
         }
      }
   }
}
