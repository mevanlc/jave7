package net.disy.commons.swing.dialog.input.select;

import net.disy.commons.core.model.FixedOptionsObjectSelectionModel;
import net.disy.commons.swing.dialog.input.IInputDialogConfiguration;
import net.disy.commons.swing.dialog.input.ISmartDialogPanel;

public interface ISomeOutOfManyDialogConfiguration<T> extends IInputDialogConfiguration, ISomeOutOfManyDialogPanelConfiguration<T> {
   ISmartDialogPanel[] createAdditionalPanels(FixedOptionsObjectSelectionModel<T> var1);

   String getDescription();
}
