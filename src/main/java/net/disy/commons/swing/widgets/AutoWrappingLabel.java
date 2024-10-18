package net.disy.commons.swing.widgets;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import net.disy.commons.core.text.TextAlignment;
import net.disy.commons.swing.laf.LookAndFeelUtilities;
import net.disy.commons.swing.widgets.internal.AutoWrappingTextComponent;

public class AutoWrappingLabel {
   private static final int INITIAL_WIDTH = 330;
   private final AutoWrappingTextComponent component;

   public AutoWrappingLabel() {
      this("");
   }

   public AutoWrappingLabel(String text) {
      this(text, 330);
   }

   public AutoWrappingLabel(String text, int width) {
      this.component = new AutoWrappingTextComponent(text, width);
      LookAndFeelUtilities.installColorsAndFont(this.component, "Label");
   }

   public void setTextAlignment(TextAlignment textAlignment) {
      this.component.setTextAlignment(textAlignment);
   }

   public void setFont(Font font) {
      this.component.setFont(font);
   }

   public Font getFont() {
      return this.component.getFont();
   }

   public JComponent getContent() {
      return this.component;
   }

   public void setForeground(Color color) {
      this.component.setForeground(color);
   }

   public void setBackground(Color color) {
      this.component.setBackground(color);
   }

   public void setText(String text) {
      this.component.setText(text);
   }

   public void setOpaque(boolean opaque) {
      this.component.setOpaque(opaque);
   }

   public void setVisible(boolean visible) {
      this.component.setVisible(visible);
   }

   public void setEnabled(boolean enabled) {
      this.component.setEnabled(enabled);
   }
}
