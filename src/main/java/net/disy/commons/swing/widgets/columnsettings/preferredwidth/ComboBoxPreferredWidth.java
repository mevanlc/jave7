package net.disy.commons.swing.smarttable.columnsettings.preferredwidth;

import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import net.disy.commons.swing.ui.IObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class ComboBoxPreferredWidth<T> implements IPreferredWidth {
   private final int preferredWidth;

   public ComboBoxPreferredWidth(List<T> values) {
      this.preferredWidth = this.getPreferredSize(new JComboBox<>(new Vector<>(values)));
   }

   private int getPreferredSize(JComboBox comboBox) {
      return comboBox.getPreferredSize().width;
   }

   public ComboBoxPreferredWidth(List<T> values, IObjectUi<T> objectUi) {
      JComboBox comboBox = new JComboBox<>(new Vector<>(values));
      comboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      this.preferredWidth = this.getPreferredSize(comboBox) + 2;
   }

   public ComboBoxPreferredWidth(T[] values, IObjectUi<T> objectUi) {
      JComboBox comboBox = new JComboBox<>(values);
      comboBox.setRenderer(new ObjectUiListCellRenderer(objectUi));
      this.preferredWidth = this.getPreferredSize(comboBox) + 2;
   }

   @Override
   public int getPreferredWidth() {
      return this.preferredWidth;
   }
}
