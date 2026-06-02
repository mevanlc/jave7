package net.dizzy.commons.swing.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ObjectUiListCellRenderer<T> extends DefaultListCellRenderer {
   private final IObjectUi<T> ui;

   public ObjectUiListCellRenderer(IObjectUi<T> ui) {
      this.ui = ui;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
      super.getListCellRendererComponent(list, value, index, selected, focus);
      T typedValue = (T) value;
      setText(ui.getLabel(typedValue));
      setIcon(ui.getIcon(typedValue));
      return this;
   }
}
