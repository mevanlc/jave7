package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.JCheckBox;
import javax.swing.table.TableCellEditor;
import net.disy.commons.swing.component.tristate.TriState;
import net.disy.commons.swing.component.tristate.TristateCheckBox;
import net.disy.commons.swing.smarttable.celleditors.DecoratingCellEditor;
import net.disy.commons.swing.smarttable.celleditors.TriStateCellEditor;

public class TriStateTableColumnSettings extends AbstractCheckBoxTableColumnSettings<TriState> {
   private final boolean dontCareSelectionState;

   public TriStateTableColumnSettings(boolean dontCareSelectionState) {
      this.dontCareSelectionState = dontCareSelectionState;
   }

   @Override
   protected JCheckBox createRenderCheckBox(Object value) {
      TristateCheckBox checkBox = new TristateCheckBox(this.dontCareSelectionState);
      checkBox.setState((TriState)value);
      return checkBox;
   }

   @Override
   public TableCellEditor getEditor() {
      return new DecoratingCellEditor(new TriStateCellEditor(this.dontCareSelectionState), new WrapInPanelAsCenteredComponentDecorator());
   }
}
