package net.disy.commons.swing.dialog.input.select;

import net.disy.commons.swing.dialog.input.ISelectableItemsPanelConfiguration;
import net.disy.commons.swing.list.ListSelectionMode;

public interface ISomeOutOfManyDialogPanelConfiguration<T> extends ISelectableItemsPanelConfiguration<T> {
   String getLabel();

   String getNoItemSelectedErrorMessageText();

   ListSelectionMode getListSelectionMode();
}
