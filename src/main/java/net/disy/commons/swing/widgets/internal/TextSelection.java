package net.disy.commons.swing.widgets.internal;

import net.disy.commons.core.util.Ensure;

public class TextSelection {
   public final TextPosition endPosition;
   public final TextPosition startPosition;

   public static TextSelection createSelection(TextPosition position1, TextPosition position2) {
      Ensure.ensureArgumentNotNull(position1);
      Ensure.ensureArgumentNotNull(position2);
      if (position1.getBlockIndex() < position2.getBlockIndex()) {
         return new TextSelection(position1, position2);
      } else {
         return position1.getBlockIndex() == position2.getBlockIndex() && position1.getIndexInBlock() <= position2.getIndexInBlock()
            ? new TextSelection(position1, position2)
            : new TextSelection(position2, position1);
      }
   }

   private TextSelection(TextPosition startPosition, TextPosition endPosition) {
      this.startPosition = startPosition;
      this.endPosition = endPosition;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof TextSelection)) {
         return false;
      } else {
         TextSelection other = (TextSelection)obj;
         return this.startPosition.equals(other.startPosition) && this.endPosition.equals(other.endPosition);
      }
   }

   @Override
   public int hashCode() {
      return this.startPosition.hashCode() + this.endPosition.hashCode() * 17;
   }
}
