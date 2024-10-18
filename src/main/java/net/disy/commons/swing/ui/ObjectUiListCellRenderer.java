package net.disy.commons.swing.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.icon.DisabledIconDecorator;

public class ObjectUiListCellRenderer extends DefaultListCellRenderer {
   private final IObjectUi ui;

   public ObjectUiListCellRenderer(IObjectUi ui) {
      Ensure.ensureArgumentNotNull(ui);
      this.ui = ui;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      Icon icon = this.ui.getIcon(value);
      if (icon != null && !list.isEnabled()) {
         icon = new DisabledIconDecorator(icon);
      }

      this.setIcon(icon);
      this.setDisabledIcon(icon == null ? null : new DisabledIconDecorator(icon));
      String label = this.ui.getLabel(value);
      this.setText(label != null && !label.isEmpty() ? label : " ");
      this.setToolTipText(this.ui.getToolTipText(value));
      return this;
   }
}
