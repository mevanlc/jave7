package de.jave.figlet.swing.fontchooser;

import de.jave.figlet.IFontCategorySelectionListener;
import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class CategoriesView {
   private final JTree tree;
   private final JComponent content;
   private final List listeners = new ArrayList();

   public CategoriesView(IFigFontCategorization categorization) {
      TreeModel treeModel = new CategorizationToTreeModelConverter().createTreeModel(categorization);
      this.tree = new JTree(treeModel);
      this.tree.setToolTipText("");
      this.tree.setRootVisible(true);
      this.tree.setShowsRootHandles(false);
      this.tree.setCellRenderer(new CategoriesTreeCellRenderer());
      this.tree.getSelectionModel().setSelectionMode(1);
      this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
         @Override
         public void valueChanged(TreeSelectionEvent e) {
            CategoriesView.this.fireCategorySelectionChangedEvent();
         }
      });
      JScrollPane scrollPane = new JScrollPane(this.tree);
      scrollPane.setPreferredSize(new Dimension(175, 145));
      this.content = scrollPane;
   }

   public JComponent getContent() {
      return this.content;
   }

   public synchronized void addFontCategorySelectionListener(IFontCategorySelectionListener listener) {
      this.listeners.add(listener);
   }

   public synchronized void removeFontCategorySelectionListener(IFontCategorySelectionListener listener) {
      this.listeners.remove(listener);
   }

   private synchronized void fireCategorySelectionChangedEvent() {
      for (int i = 0; i < this.listeners.size(); i++) {
         IFontCategorySelectionListener listener = (IFontCategorySelectionListener)this.listeners.get(i);
         listener.categorySelectionChanged();
      }
   }

   public IFigFontCategory getSelectedCategory() {
      DefaultMutableTreeNode selectedNode = this.getSelectedTreeNode();
      return selectedNode != null && selectedNode.getUserObject() instanceof IFigFontCategory ? (IFigFontCategory)selectedNode.getUserObject() : null;
   }

   private DefaultMutableTreeNode getSelectedTreeNode() {
      TreePath selectionPath = this.tree.getSelectionModel().getSelectionPath();
      return selectionPath == null ? null : (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
   }

   public void setSelectedCategory(IFigFontCategory category) {
      if (category != null) {
         TreeModel model = this.tree.getModel();
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)model.getRoot();
         this.selectCategory(category, node);
      }
   }

   private boolean selectCategory(IFigFontCategory category, DefaultMutableTreeNode node) {
      if (category.equals(node.getUserObject())) {
         TreePath path = new TreePath(node.getPath());
         this.tree.setSelectionPath(path);
         this.tree.scrollPathToVisible(path);
         return true;
      } else {
         for (int i = 0; i < node.getChildCount(); i++) {
            if (this.selectCategory(category, (DefaultMutableTreeNode)node.getChildAt(i))) {
               return true;
            }
         }

         return false;
      }
   }
}
