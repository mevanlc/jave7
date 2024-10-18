package net.disy.commons.swing.dialog.input.select;

import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.swing.dialog.input.ISmartDialogPanel;
import net.disy.commons.swing.list.ListSelectionMode;

public abstract class AbstractOneOutOfManyDialogConfiguration<T> implements ISomeOutOfManyDialogConfiguration<T> {
   @Override
   public String getDescription() {
      return this.getTitle();
   }

   @Override
   public ISmartDialogPanel[] createAdditionalPanels(FixedOptionsObjectSelectionModel<T> selectionModel) {
      return new ISmartDialogPanel[0];
   }

   @Override
   public ListSelectionMode getListSelectionMode() {
      return ListSelectionMode.SINGLE_SELECTION;
   }
}
