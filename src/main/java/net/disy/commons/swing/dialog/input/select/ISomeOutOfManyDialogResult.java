package net.disy.commons.swing.dialog.input.select;

import java.util.List;
import net.disy.commons.swing.dialog.core.IDialogResult;

public interface ISomeOutOfManyDialogResult<T> extends IDialogResult {
   T getSelectedItem();

   List<T> getSelectedItems();
}
