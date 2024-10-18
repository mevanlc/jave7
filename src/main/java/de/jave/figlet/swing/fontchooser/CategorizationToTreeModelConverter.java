package de.jave.figlet.swing.fontchooser;

import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.file.IFigFontCategoryContainer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class CategorizationToTreeModelConverter {
   public TreeModel createTreeModel(IFigFontCategorization categorization) {
      DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Font categories");
      DefaultTreeModel model = new DefaultTreeModel(rootNode);
      this.addChildCategories(categorization, rootNode);
      return model;
   }

   private void addChildCategories(IFigFontCategoryContainer parentCategory, DefaultMutableTreeNode parentNode) {
      IFigFontCategory[] categories = parentCategory.getChildCategories();

      for (int i = 0; i < categories.length; i++) {
         DefaultMutableTreeNode node = new DefaultMutableTreeNode(categories[i]);
         parentNode.add(node);
         this.addChildCategories(categories[i], node);
      }
   }
}
