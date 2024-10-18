package net.disy.commons.swing.widgets.internal;

public class TextPosition {
   private final int indexInBlock;
   private final int blockIndex;

   public TextPosition(int blockIndex, int indexInBlock) {
      if (blockIndex < 0) {
         throw new IllegalArgumentException("Illegal block index " + blockIndex);
      } else if (indexInBlock < 0) {
         throw new IllegalArgumentException("Illegal index in block " + indexInBlock);
      } else {
         this.blockIndex = blockIndex;
         this.indexInBlock = indexInBlock;
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof TextPosition)) {
         return false;
      } else {
         TextPosition other = (TextPosition)obj;
         return this.indexInBlock == other.indexInBlock && this.blockIndex == other.blockIndex;
      }
   }

   @Override
   public int hashCode() {
      return this.blockIndex * 5 + this.indexInBlock * 13;
   }

   public int getBlockIndex() {
      return this.blockIndex;
   }

   public int getIndexInBlock() {
      return this.indexInBlock;
   }
}
