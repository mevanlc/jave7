package net.disy.commons.swing.layout.util;

public class GridCellSizeList {
   private final GridCellSize[] sizes;

   public GridCellSizeList(int size) {
      this.sizes = new GridCellSize[size];

      for (int i = 0; i < this.sizes.length; i++) {
         this.sizes[i] = new GridCellSize();
      }
   }

   public GridCellSizeList(GridCellSize[] sizes) {
      this.sizes = sizes;
   }

   public GridCellSize get(int index) {
      return this.sizes[index];
   }

   public int size() {
      return this.sizes.length;
   }

   public void increaseMinimumSizes(int startIndex, int span, int totalIncrement) {
      if (span < 1) {
         throw new IllegalArgumentException("Illegal span " + span);
      } else {
         int increment = totalIncrement / span;

         for (int i = startIndex; i < startIndex + span; i++) {
            this.sizes[i].incrementMinimumSize(increment);
         }

         int remainder = totalIncrement - increment * span;

         for (int i = 0; i < remainder; i++) {
            int sizeIndex = startIndex + span - 1 - i;
            this.sizes[sizeIndex].incrementMinimumSize(1);
         }
      }
   }

   public void increasePreferredSizes(int startIndex, int span, int totalIncrement) {
      if (span < 1) {
         throw new IllegalArgumentException("Illegal span " + span);
      } else {
         int increment = totalIncrement / span;

         for (int i = startIndex; i < startIndex + span; i++) {
            this.sizes[i].incrementPreferredSize(increment);
         }

         int remainder = totalIncrement - increment * span;

         for (int i = 0; i < remainder; i++) {
            int sizeIndex = startIndex + span - 1 - i;
            this.sizes[sizeIndex].incrementPreferredSize(1);
         }
      }
   }

   public void increaseSizes(int startIndex, int span, int totalIncrement) {
      if (span < 1) {
         throw new IllegalArgumentException("Illegal span " + span);
      } else {
         int increment = totalIncrement / span;

         for (int i = startIndex; i < startIndex + span; i++) {
            this.sizes[i].incrementSize(increment);
         }

         int remainder = totalIncrement - increment * span;

         for (int i = 0; i < remainder; i++) {
            int sizeIndex = startIndex + span - 1 - i;
            this.sizes[sizeIndex].incrementSize(1);
         }
      }
   }

   public int getAvailableMinimumSize(int startIndex, int span, int spacing) {
      if (span < 0) {
         throw new IllegalArgumentException("Illegal span " + span);
      } else {
         int size = 0;

         for (int i = startIndex; i < startIndex + span; i++) {
            size += this.sizes[i].getMinimumSize();
         }

         return size + (span - 1) * spacing;
      }
   }

   public int getAvailablePreferredSize(int startIndex, int span, int spacing) {
      if (span < 0) {
         throw new IllegalArgumentException("Illegal span " + span);
      } else {
         int size = 0;

         for (int i = startIndex; i < startIndex + span; i++) {
            size += this.sizes[i].getPreferredSize();
         }

         return size + (span - 1) * spacing;
      }
   }

   public void increaseSizes(int totalIncrement) {
      this.increaseSizes(0, this.size(), totalIncrement);
   }

   public void adjustToPreferredSizes() {
      for (int i = 0; i < this.sizes.length; i++) {
         this.sizes[i].adjustToPreferredSize();
      }
   }
}
