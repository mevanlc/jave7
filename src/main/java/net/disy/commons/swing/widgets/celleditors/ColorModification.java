package net.disy.commons.swing.smarttable.celleditors;

import java.awt.Color;

public class ColorModification implements IColorModificationEntry {
   private final boolean modified;
   private final Color color;

   public ColorModification(boolean modified, Color color) {
      this.modified = modified;
      this.color = color;
   }

   @Override
   public boolean isModified() {
      return this.modified;
   }

   @Override
   public Color getColor() {
      return this.color;
   }
}
