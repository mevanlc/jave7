package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.swing.dialog.input.ISmartDialogPanel;
import net.disy.commons.swing.util.IEnableableComponentContainer;

public interface IUpdatableSmartDialogPanel extends ISmartDialogPanel, IEnableableComponentContainer {
   void update();
}
