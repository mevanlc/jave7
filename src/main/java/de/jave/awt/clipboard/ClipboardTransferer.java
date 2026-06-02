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
         return createSelection(clipboard.getContents(null));
      } catch (UnsupportedFlavorException var3) {
         return null;
      } catch (IOException var4) {
         return null;
      }
   }

   static JaveClipboardSelection createSelection(Object clipboardContent) throws UnsupportedFlavorException, IOException {
      if (clipboardContent == null) {
         return null;
      } else if (clipboardContent instanceof JaveClipboardSelection) {
         return normalizeSelection((JaveClipboardSelection)clipboardContent);
      } else if (clipboardContent instanceof Transferable) {
         Transferable transferable = (Transferable)clipboardContent;
         if (!transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return null;
         }
         String sel = (String)transferable.getTransferData(DataFlavor.stringFlavor);
         if (sel == null || sel.length() == 0) {
            return null;
         }
         JaveClipboardSelection selection = sel.indexOf(9) != -1
            ? new JaveClipboardSelection(CharacterPlate.tabelize(sel))
            : new JaveClipboardSelection(sel);
         return normalizeSelection(selection);
      } else {
         return null;
      }
   }

   private static JaveClipboardSelection normalizeSelection(JaveClipboardSelection selection) {
      CharacterPlate content = selection.getContent();
      return content != null && content.getWidth() > 0 && content.getHeight() > 0 ? selection : null;
   }
}
