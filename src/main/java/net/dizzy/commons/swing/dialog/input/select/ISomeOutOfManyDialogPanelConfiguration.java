package net.dizzy.commons.swing.dialog.input.select;

import net.dizzy.commons.swing.list.ListSelectionMode;
import net.dizzy.commons.swing.ui.IObjectUi;

public interface ISomeOutOfManyDialogPanelConfiguration<T> {
   default T[] getValues() { return getItems(); }
   default T[] getItems() { return null; }
   default String getLabel(T value) { return getObjectUi().getLabel(value); }
   default IObjectUi<T> getObjectUi() { return null; }
   default String getNoItemSelectedErrorMessageText() { return ""; }
   default ListSelectionMode getListSelectionMode() { return ListSelectionMode.SINGLE_SELECTION; }
   default String getLabel() { return ""; }
   default String getDefaultMessageText() { return ""; }
   default String getTitle() { return ""; }
}
