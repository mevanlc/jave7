package net.disy.commons.swing.util;

import java.util.Collection;
import javax.swing.DefaultComboBoxModel;

public class ComboBoxUtilities {
   private ComboBoxUtilities() {
   }

   public static void setComboBoxItems(DefaultComboBoxModel comboBoxModel, Collection<?> allItems) {
      comboBoxModel.removeAllElements();

      for (Object bearbeiter : allItems) {
         comboBoxModel.addElement(bearbeiter);
      }
   }
}
