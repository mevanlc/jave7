package net.disy.commons.swing.list;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;

public class JListUtilities {
   private JListUtilities() {
   }

   public static void setListItems(JList list, Object[] items) {
      list.setModel(createListModel(items));
   }

   public static void setComboItems(JComboBox comboBox, Object[] items) {
      comboBox.setModel(createListModel(items));
   }

   public static DefaultComboBoxModel createListModel(Object[] items) {
      DefaultComboBoxModel model = new DefaultComboBoxModel();

      for (int i = 0; i < items.length; i++) {
         model.addElement(items[i]);
      }

      return model;
   }
}
