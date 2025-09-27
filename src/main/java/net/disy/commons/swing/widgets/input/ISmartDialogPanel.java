package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public interface ISmartDialogPanel extends IDialogComponent, IOptionalCurrentMessageFactory {
   void addChangeListener(IChangeListener var1);

   void addRequestFinishListener(IRequestFinishListener var1);

   void requestFocus();
}
