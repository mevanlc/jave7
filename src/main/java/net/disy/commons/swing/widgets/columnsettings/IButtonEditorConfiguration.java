package net.disy.commons.swing.smarttable.columnsettings;

import javax.swing.Icon;
import net.disy.commons.swing.smarttable.celleditors.action.CellEditorAction;

public interface IButtonEditorConfiguration {
   CellEditorAction createAction(IEditStoppedHandler var1, int var2, int var3, Object var4);

   Icon getLargestButtonIcon();

   String getLongestButtonLabel();
}
