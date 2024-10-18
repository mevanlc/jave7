package de.jave.figlet.swing.application;

import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.ui.FontCategoriesListCellRenderer;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class FontCategoriesListView {
   private final JList list = new JList(new DefaultListModel());
   private final JComponent content;

   public FontCategoriesListView() {
      this.list.setCellRenderer(new FontCategoriesListCellRenderer());
      JScrollPane scrollPane = new JScrollPane(this.list);
      scrollPane.setPreferredSize(new Dimension(150, 100));
      this.content = scrollPane;
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setCategories(IFigFontCategory[] categories) {
      DefaultListModel model = (DefaultListModel)this.list.getModel();
      model.removeAllElements();

      for (int i = 0; i < categories.length; i++) {
         model.addElement(categories[i]);
      }
   }
}
