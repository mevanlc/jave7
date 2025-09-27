package net.disy.commons.swing.smarttable.celleditors;

import java.text.Format;
import java.text.ParseException;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.text.DefaultFormatterFactory;

public abstract class AbstractFormattedCellEditor extends AbstractDelegatingCellEditor {
   private final int alignment;

   public AbstractFormattedCellEditor(Format format, NullValueStrategy nullValueStrategy, int alignment) {
      this.alignment = alignment;
      AbstractFormatter formatter = this.createFormatter(format);
      formatter = nullValueStrategy.decorateFormatter(formatter);
      ((JFormattedTextField)this.getEditorComponent()).setFormatterFactory(new DefaultFormatterFactory(formatter));
   }

   protected abstract AbstractFormatter createFormatter(Format var1);

   @Override
   protected final EditorDelegate createDelegate(JComponent editorComponent) {
      final JFormattedTextField textField = (JFormattedTextField)editorComponent;
      textField.setHorizontalAlignment(this.alignment);
      return new EditorDelegate(this) {
         @Override
         public void setValue(Object value) {
            textField.setValue(value);
            textField.selectAll();
         }

         @Override
         public Object getCellEditorValue() {
            try {
               textField.commitEdit();
            } catch (ParseException var2) {
            }

            return textField.getValue();
         }
      };
   }

   @Override
   protected final JComponent createEditorComponent() {
      return new JFormattedTextField();
   }
}
