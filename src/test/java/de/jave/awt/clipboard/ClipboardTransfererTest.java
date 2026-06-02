package de.jave.awt.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class ClipboardTransfererTest {
   @Test
   public void emptyStringClipboardContentIsTreatedAsNoSelection() throws Exception {
      Assert.assertNull(ClipboardTransferer.createSelection(new StringSelection("")));
   }

   @Test
   public void emptyJaveClipboardSelectionIsTreatedAsNoSelection() throws Exception {
      Assert.assertNull(ClipboardTransferer.createSelection(new JaveClipboardSelection("")));
   }

   @Test
   public void whitespaceClipboardContentIsKept() throws Exception {
      JaveClipboardSelection selection = ClipboardTransferer.createSelection(new StringSelection(" "));

      Assert.assertNotNull(selection);
      Assert.assertEquals(1, selection.getContent().getWidth());
      Assert.assertEquals(1, selection.getContent().getHeight());
      Assert.assertEquals(' ', selection.getContent().get(0, 0));
   }

   @Test
   public void nonStringTransferableClipboardContentIsTreatedAsNoSelection() throws Exception {
      Assert.assertNull(ClipboardTransferer.createSelection(new Transferable() {
         @Override
         public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
         }

         @Override
         public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
         }

         @Override
         public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            throw new UnsupportedFlavorException(flavor);
         }
      }));
   }
}
