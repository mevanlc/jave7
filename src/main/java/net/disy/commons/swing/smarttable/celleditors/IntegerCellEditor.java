package net.disy.commons.swing.smarttable.celleditors;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class IntegerCellEditor extends AbstractDelegatingCellEditor {
   private final SpinnerNumberModel spinnerNumberModel;

   public IntegerCellEditor(int minimum, int maximum, int stepsize) {
      this.spinnerNumberModel = new SpinnerNumberModel(minimum, minimum, maximum, stepsize);
   }

   @Override
   protected final EditorDelegate createDelegate(JComponent editorComponent) {
      final JSpinner spinner = (JSpinner)editorComponent;
      return new EditorDelegate(this) {
         @Override
         public void setValue(Object value) {
            spinner.setModel(IntegerCellEditor.this.spinnerNumberModel);
            IntegerCellEditor.this.spinnerNumberModel.setValue(value);
         }

         @Override
         public Object getCellEditorValue() {
            return IntegerCellEditor.this.spinnerNumberModel.getNumber();
         }
      };
   }

   @Override
   protected JComponent createEditorComponent() {
      return new JSpinner();
   }
}
