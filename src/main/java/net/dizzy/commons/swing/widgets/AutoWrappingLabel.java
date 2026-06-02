package net.dizzy.commons.swing.widgets;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.dizzy.commons.swing.component.IComponentContainer;

public class AutoWrappingLabel implements IComponentContainer {
   private final JLabel label;

   public AutoWrappingLabel(String text) {
      label = new JLabel(toHtml(text));
   }

   public AutoWrappingLabel(String text, int width) {
      this(text);
      label.setSize(width, Short.MAX_VALUE);
   }

   @Override
   public JComponent getContent() {
      return label;
   }

   public void setEnabled(boolean enabled) {
      label.setEnabled(enabled);
   }

   private static String toHtml(String text) {
      return "<html><body>" + (text == null ? "" : text) + "</body></html>";
   }
}
