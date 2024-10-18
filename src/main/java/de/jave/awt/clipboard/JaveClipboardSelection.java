package de.jave.awt.clipboard;

import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.StringReader;

public class JaveClipboardSelection implements Transferable {
   private final CharacterPlate content;
   private BooleanArea mask;
   private static final int JAVE_SELECTION = 0;
   private static final int STRING = 1;
   private static final int PLAIN_TEXT = 2;
   private static final DataFlavor[] flavors = new DataFlavor[3];

   public JaveClipboardSelection(String s) {
      this.content = new CharacterPlate(s);
      this.mask = null;
   }

   public JaveClipboardSelection(CharacterPlate content) {
      this(content, null);
   }

   public JaveClipboardSelection(CharacterPlate content, BooleanArea mask) {
      this.content = content.getClone();
      if (mask != null) {
         this.mask = mask.getClone();
      }
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return flavors.clone();
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      for (int i = 0; i < flavors.length; i++) {
         if (flavor.equals(flavors[i])) {
            return true;
         }
      }

      return false;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if (flavor.equals(flavors[0])) {
         return this;
      } else if (flavor.equals(flavors[1])) {
         return this.content.toString();
      } else if (flavor.equals(flavors[2])) {
         return new StringReader(this.content.toString());
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }

   public CharacterPlate getContent() {
      return this.content;
   }

   public BooleanArea getMask() {
      return this.mask;
   }

   static {
      flavors[0] = new DataFlavor(JaveSelection.class, "JavE Selection");
      flavors[1] = DataFlavor.stringFlavor;
      flavors[2] = DataFlavor.plainTextFlavor;
   }
}
