package net.disy.commons.swing.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import net.disy.commons.core.util.Ensure;

public class TreeNodeTransferableAdapter<T> implements Transferable {
   private final T data;
   private final DataFlavor defaultFlavor;

   public TreeNodeTransferableAdapter(T data, DataFlavor defaultFlavor) {
      Ensure.ensureArgumentNotNull(data);
      Ensure.ensureArgumentNotNull(defaultFlavor);
      this.data = data;
      this.defaultFlavor = defaultFlavor;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{this.defaultFlavor};
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (flavor.equals(this.defaultFlavor)) {
         return this.data;
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(this.defaultFlavor);
   }
}
