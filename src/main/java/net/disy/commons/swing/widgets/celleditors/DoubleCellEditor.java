package net.disy.commons.swing.smarttable.celleditors;

import javax.swing.JComponent;
import net.disy.commons.swing.textfield.DoubleField;

public class DoubleCellEditor extends AbstractDelegatingCellEditor {
   @Override
   protected final EditorDelegate createDelegate(JComponent editorComponent) {
      final DoubleField textField = DoubleField.getDoubleField(editorComponent);
      return new EditorDelegate(this) {
         @Override
         public void setValue(Object value) {
            textField.setValue(((Number)value).doubleValue());
            textField.selectAll();
         }

         @Override
         public Object getCellEditorValue() {
            return textField.getValue();
         }
      };
   }

   @Override
   protected JComponent createEditorComponent() {
      return new DoubleField().getContent();
   }
}
