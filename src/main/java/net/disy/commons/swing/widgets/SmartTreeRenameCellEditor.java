package net.disy.commons.swing.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.icon.EmptyIcon;

public class SmartTreeRenameCellEditor<T> extends AbstractCellEditor implements TreeCellEditor, FocusListener {
   private final JTree tree;
   private final ISmartTreeRenameStrategy<T> renameStrategy;
   private final JTextField textField;
   private SmartTreeModelNode<T> editingNode;
   private TreePath lastPath;

   private SmartTreeRenameCellEditor(JTree tree, ISmartTreeRenameStrategy<T> renameStrategy) {
      Ensure.ensureArgumentNotNull(tree);
      Ensure.ensureArgumentNotNull(renameStrategy);
      this.tree = tree;
      this.renameStrategy = renameStrategy;
      Border aBorder = UIManager.getBorder("Tree.editorBorder");
      this.textField = new JTextField(12);
      this.textField.setBorder(aBorder);
      this.textField.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent arg0) {
            SmartTreeRenameCellEditor.this.stopCellEditing();
         }
      });
      this.textField.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent evt) {
         }

         @Override
         public void focusLost(FocusEvent evt) {
            SmartTreeRenameCellEditor.this.stopCellEditing();
         }
      });
   }

   public static <T> void attachTo(JTree tree, ISmartTreeRenameStrategy<T> renameStrategy) {
      SmartTreeRenameCellEditor editor = new SmartTreeRenameCellEditor<>(tree, renameStrategy);
      tree.setCellEditor(editor);
      tree.addFocusListener(editor);
   }

   @Override
   public Object getCellEditorValue() {
      return this.textField.getText();
   }

   @Override
   public Component getTreeCellEditorComponent(JTree theTree, Object selectedNode, boolean isSelected, boolean isExpanded, boolean leaf, int row) {
      SmartTreeModelNode<T> treeNode = (SmartTreeModelNode<T>)selectedNode;
      this.editingNode = treeNode;
      Component rendererComponent = this.tree.getCellRenderer().getTreeCellRendererComponent(this.tree, selectedNode, isSelected, isExpanded, leaf, row, true);
      Icon icon;
      if (rendererComponent instanceof JLabel) {
         JLabel label = (JLabel)rendererComponent;
         icon = label.getIcon();
      } else {
         icon = new EmptyIcon();
      }

      this.textField.setText(this.renameStrategy.getOriginalName(treeNode.getNodeInSmartTree()));
      this.textField.selectAll();
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.add(new JLabel(icon), "West");
      panel.add(this.textField, "Center");
      panel.setBackground(this.tree.getBackground());
      return panel;
   }

   @Override
   public boolean isCellEditable(EventObject event) {
      if (event == null) {
         TreePath selectionPath = this.tree.getSelectionPath();
         if (selectionPath == null) {
            return false;
         } else {
            SmartTreeModelNode<T> node = (SmartTreeModelNode<T>)selectionPath.getLastPathComponent();
            return this.renameStrategy.isRenameable(node.getNodeInSmartTree());
         }
      } else if (!(event.getSource() instanceof JTree)) {
         return false;
      } else if (!(event instanceof MouseEvent)) {
         return true;
      } else {
         MouseEvent mouseEvent = (MouseEvent)event;
         if (mouseEvent.isMetaDown()) {
            this.lastPath = null;
            return false;
         } else if (mouseEvent.getClickCount() == 2) {
            return false;
         } else {
            TreePath path = this.tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path == null) {
               return false;
            } else {
               Rectangle nodeBounds = this.tree.getUI().getPathBounds(this.tree, path);
               SmartTreeModelNode<T> node = (SmartTreeModelNode<T>)path.getLastPathComponent();
               if (!this.renameStrategy.isRenameable(node.getNodeInSmartTree())) {
                  return false;
               } else {
                  int iconWidth = 16;
                  Rectangle boundsWithoutIcon = new Rectangle(nodeBounds.x + 16, nodeBounds.y, nodeBounds.width - 16, nodeBounds.height);
                  if (!boundsWithoutIcon.contains(mouseEvent.getPoint())) {
                     return false;
                  } else if (this.lastPath != null && this.lastPath.equals(path)) {
                     this.lastPath = null;
                     return true;
                  } else {
                     this.lastPath = path;
                     return false;
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean shouldSelectCell(EventObject anEvent) {
      return true;
   }

   @Override
   public boolean stopCellEditing() {
      String newName = this.textField.getText();
      boolean success = !StringUtilities.isNullOrEmpty(newName) && this.renameStrategy.isValidNewName(this.editingNode.getNodeInSmartTree(), newName);
      if (success) {
         this.fireEditingStopped();
      } else {
         Toolkit.getDefaultToolkit().beep();
      }

      return success;
   }

   @Override
   public void cancelCellEditing() {
      this.fireEditingCanceled();
   }

   @Override
   public void focusGained(FocusEvent arg0) {
   }

   @Override
   public void focusLost(FocusEvent arg0) {
      this.lastPath = null;
   }
}
