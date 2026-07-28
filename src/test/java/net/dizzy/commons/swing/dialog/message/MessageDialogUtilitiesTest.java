package net.dizzy.commons.swing.dialog.message;

import static org.junit.Assert.assertEquals;

import javax.swing.JOptionPane;
import org.junit.Test;

public class MessageDialogUtilitiesTest {
   @Test
   public void saveDiscardCancelResultsKeepTheirButtonSemantics() {
      assertEquals(YesNoCancel.YES, MessageDialogUtilities.saveDiscardCancelResult(0));
      assertEquals(YesNoCancel.NO, MessageDialogUtilities.saveDiscardCancelResult(1));
      assertEquals(YesNoCancel.CANCEL, MessageDialogUtilities.saveDiscardCancelResult(2));
      assertEquals(YesNoCancel.CANCEL, MessageDialogUtilities.saveDiscardCancelResult(JOptionPane.CLOSED_OPTION));
   }
}
