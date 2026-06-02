package net.dizzy.commons.swing.image;

import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardImage implements Transferable, ClipboardOwner {
   private final Image image;

   public ClipboardImage(Image image) {
      this.image = image;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { DataFlavor.imageFlavor };
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return DataFlavor.imageFlavor.equals(flavor);
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (!isDataFlavorSupported(flavor)) {
         throw new UnsupportedFlavorException(flavor);
      }
      return image;
   }

   @Override
   public void lostOwnership(Clipboard clipboard, Transferable contents) {
   }
}
