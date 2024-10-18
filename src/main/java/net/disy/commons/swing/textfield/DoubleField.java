package net.disy.commons.swing.textfield;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.string.StringFilter;
import net.disy.commons.swing.events.ICheckInputValidListener;

public class DoubleField {
   private final NumberFormat format;
   protected final DoubleField.InternalComponent textField;

   public static DoubleField getDoubleField(Component component) {
      return !(component instanceof DoubleField.InternalComponent) ? null : ((DoubleField.InternalComponent)component).doubleField;
   }

   public DoubleField() {
      this(0);
   }

   public DoubleField(int cols) {
      this(cols, 0.0);
   }

   public DoubleField(int columnCount, double value) {
      this(columnCount, value, createDefaultFormat());
   }

   private static NumberFormat createDefaultFormat() {
      NumberFormat format = NumberFormat.getNumberInstance();
      format = (NumberFormat)format.clone();
      format.setMinimumFractionDigits(Math.max(1, format.getMinimumFractionDigits()));
      return format;
   }

   public DoubleField(int columnCount, double value, final NumberFormat format) {
      this.textField = new DoubleField.InternalComponent(new DoubleField.DoubleFilter(format), getValueText(value, format), columnCount);
      this.textField.setHorizontalAlignment(4);
      this.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent e) {
            DoubleField.this.setText(DoubleField.getValueText(DoubleField.this.getValue(), format));
         }
      });
      this.format = format;
      this.textField.doubleField = this;
   }

   public double getValue() {
      if (this.isInvalidContent()) {
         return 0.0;
      } else {
         try {
            return this.format != null ? this.format.parse(this.getText()).doubleValue() : Double.parseDouble(this.getText());
         } catch (Exception var2) {
            throw new UnreachableCodeReachedException(var2);
         }
      }
   }

   public Double getDoubleValue() {
      return this.isInvalidContent() ? null : this.getValue();
   }

   public boolean isInvalidContent() {
      String text = this.getText();
      return text.length() == 0 || text.equals("-");
   }

   private String getText() {
      return this.textField.getText();
   }

   public void setValue(double value) {
      if (this.getValue() != value) {
         this.setText(getValueText(value, this.format));
      }
   }

   public void setValue(Double value) {
      if (value == null) {
         if (!this.isInvalidContent()) {
            this.setText("");
         }
      } else if (this.getValue() != value) {
         this.setText(getValueText(value, this.format));
      }
   }

   private void setText(String text) {
      this.textField.setText(text);
   }

   private static String getValueText(double value, NumberFormat numberFormat) {
      return numberFormat != null ? numberFormat.format(value) : String.valueOf(value);
   }

   public void addCheckInputValidListener(ICheckInputValidListener listener) {
      this.addDocumentListener(listener);
   }

   public void setLeftAligned() {
      this.textField.setHorizontalAlignment(2);
   }

   public JComponent getContent() {
      return this.textField;
   }

   public void requestFocus() {
      this.textField.requestFocus();
   }

   public void addActionListener(ActionListener actionListener) {
      this.textField.addActionListener(actionListener);
   }

   public void addFocusListener(FocusListener focusListener) {
      this.textField.addFocusListener(focusListener);
   }

   public void addDocumentListener(DocumentListener documentListener) {
      this.textField.getDocument().addDocumentListener(documentListener);
   }

   public void setForeground(Color color) {
      this.textField.setForeground(color);
   }

   public void setBackground(Color color) {
      this.textField.setBackground(color);
   }

   public void setBorder(Border border) {
      this.textField.setBorder(border);
   }

   public void setEnabled(boolean enabled) {
      this.textField.setEditable(enabled);
   }

   public String getFormattedDouble() {
      return this.textField.getText();
   }

   public void clear() {
      this.textField.setText("");
   }

   public void selectAll() {
      this.textField.selectAll();
   }

   public static class DoubleFilter implements StringFilter {
      private final NumberFormat format;

      public DoubleFilter(NumberFormat format) {
         this.format = format;
      }

      @Override
      public boolean acceptFilterText(String text) {
         try {
            if (text.length() != 0 && !text.equals("-")) {
               if (this.format == null) {
                  Double.valueOf(text);
               } else {
                  this.format.parse(text);
               }
            }

            return true;
         } catch (NumberFormatException var3) {
            return false;
         } catch (ParseException var4) {
            return false;
         }
      }
   }

   private static class InternalComponent extends FilteredTextField {
      private DoubleField doubleField;

      public InternalComponent(DoubleField.DoubleFilter doubleFilter, String valueText, int columnCount) {
         super(doubleFilter, valueText, columnCount);
      }
   }
}
