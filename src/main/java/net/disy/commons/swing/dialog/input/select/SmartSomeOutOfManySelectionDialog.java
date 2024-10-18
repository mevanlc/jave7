package net.disy.commons.swing.dialog.input.select;

import java.awt.Component;
import java.util.List;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class SmartSomeOutOfManySelectionDialog {
   private SmartSomeOutOfManySelectionDialog() {
   }

   public static <T> ISomeOutOfManyDialogResult<T> showSelectOneOutOfManyDialog(Component parentComponent, ISomeOutOfManyDialogConfiguration<T> configuration) {
      SelectSomeOutOfManyDialogPage<T> page = new SelectSomeOutOfManyDialogPage<>(configuration);
      UserDialog dialog = new UserDialog(parentComponent, page);
      final IDialogResult result = dialog.show();
      final List<T> selectedItems = page.getSelectedItems();
      return new ISomeOutOfManyDialogResult<T>() {
         @Override
         public boolean isCanceled() {
            return result.isCanceled();
         }

         @Override
         public List<T> getSelectedItems() {
            return selectedItems;
         }

         @Override
         public T getSelectedItem() {
            return selectedItems.size() == 0 ? null : selectedItems.get(0);
         }
      };
   }

   public static <T> UserDialog createDemoDialog(Component parentComponent, ISomeOutOfManyDialogConfiguration<T> configuration) {
      SelectSomeOutOfManyDialogPage<T> page = new SelectSomeOutOfManyDialogPage<>(configuration);
      return new UserDialog(parentComponent, page);
   }
}
