package net.disy.commons.swing.dialog.smarttree;

import java.awt.Component;
import net.disy.commons.swing.dialog.message.MessageDialogUtilities;
import net.disy.commons.swing.tree.ISmartTreeDeleteStrategy;

public abstract class AbstractConfirmSmartTreeDeleteStrategy<T> implements ISmartTreeDeleteStrategy<T> {
   @Override
   public final boolean deleteNode(Component parentComponent, T parentNode, int index, T node) {
      return MessageDialogUtilities.confirmUserOperation(parentComponent, this.getConfirmText()) ? this.doDelete(parentNode, index, node) : false;
   }

   protected abstract String getConfirmText();

   protected abstract boolean doDelete(T var1, int var2, T var3);
}
