package net.disy.commons.swing.smarttable.celleditors;

import javax.swing.JComponent;
import net.disy.commons.swing.component.tristate.TriState;
import net.disy.commons.swing.component.tristate.TristateCheckBox;

public class TriStateCellEditor extends AbstractDelegatingCellEditor {
   private final boolean dontCareSelection;

   public TriStateCellEditor(boolean dontCareSelection) {
      this.dontCareSelection = dontCareSelection;
   }

   @Override
   protected final EditorDelegate createDelegate(JComponent editorComponent) {
      final TristateCheckBox checkBox = (TristateCheckBox)editorComponent;
      return new EditorDelegate(this, 1) {
         @Override
         public void setValue(Object value) {
            checkBox.setState((TriState)value);
         }

         @Override
         public Object getCellEditorValue() {
            return checkBox.getState();
         }
      };
   }

   @Override
   protected JComponent createEditorComponent() {
      return new TristateCheckBox(this.dontCareSelection);
   }
}
