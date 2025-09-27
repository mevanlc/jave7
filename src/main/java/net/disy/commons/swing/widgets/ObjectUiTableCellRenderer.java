package net.disy.commons.swing.ui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.disy.commons.core.util.Ensure;

public class ObjectUiTableCellRenderer<T> extends DefaultTableCellRenderer {
   private final IObjectUi<T> objectUi;
   private final int alignment;
   private final boolean showText;

   public ObjectUiTableCellRenderer(IObjectUi<T> objectUi) {
      this(objectUi, 2, true);
   }

   public ObjectUiTableCellRenderer(IObjectUi<T> objectUi, int alignment, boolean showText) {
      Ensure.ensureArgumentNotNull(objectUi);
      this.objectUi = objectUi;
      this.alignment = alignment;
      this.showText = showText;
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      T object = this.getObject(value);
      this.setIcon(this.objectUi.getIcon(object));
      this.setText(this.showText ? this.objectUi.getLabel(object) : null);
      this.setToolTipText(this.objectUi.getToolTipText(object));
      this.setHorizontalAlignment(this.alignment);
      return this;
   }

   private T getObject(Object value) {
      return (T)value;
   }

   public IObjectUi<T> getObjectUi() {
      return this.objectUi;
   }
}
