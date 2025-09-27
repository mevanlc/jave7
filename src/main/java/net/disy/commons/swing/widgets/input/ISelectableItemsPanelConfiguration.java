package net.disy.commons.swing.dialog.input;

import net.disy.commons.swing.ui.IObjectUi;

public interface ISelectableItemsPanelConfiguration<T> {
   T[] getItems();

   IObjectUi<T> getObjectUi();
}
