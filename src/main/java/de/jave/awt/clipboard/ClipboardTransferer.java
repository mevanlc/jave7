package de.jave.awt.clipboard;

import de.jave.lib.CharacterPlate;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardTransferer {
   public static final void setClipboardContent(JaveClipboardSelection sel) {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(sel, DefaultClipboardOwner.getInstance());
   }

   public static final JaveClipboardSelection getClipboardContent() {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

      try {
         Object o = clipboard.getContents(null);
         if (o == null) {
            return null;
         } else if (o instanceof JaveClipboardSelection) {
            return (JaveClipboardSelection)o;
         } else {
            String sel = (String)((Transferable)o).getTransferData(DataFlavor.stringFlavor);
            return sel.indexOf(9) != -1 ? new JaveClipboardSelection(CharacterPlate.tabelize(sel)) : new JaveClipboardSelection(sel);
         }
      } catch (UnsupportedFlavorException var3) {
         return null;
      } catch (IOException var4) {
         return null;
      }
   }
}
