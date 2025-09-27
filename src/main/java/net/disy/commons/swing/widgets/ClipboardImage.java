package net.disy.commons.swing.image;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import net.disy.commons.core.util.Ensure;

public class ClipboardImage implements Transferable, ClipboardOwner {
   private final Image image;

   public ClipboardImage(Image image) {
      Ensure.ensureArgumentNotNull(image);
      this.image = image;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{DataFlavor.imageFlavor};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return DataFlavor.imageFlavor.equals(flavor);
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (!this.isDataFlavorSupported(flavor)) {
         throw new UnsupportedFlavorException(flavor);
      } else {
         return this.image;
      }
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }
}
