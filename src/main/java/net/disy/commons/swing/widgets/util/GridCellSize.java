package net.disy.commons.swing.layout.util;

public class GridCellSize {
   private int minimumSize = 0;
   private int preferredSize = 0;
   private int start;
   private int size;
   private boolean grabExcessSpace = false;

   public int getMinimumSize() {
      return this.minimumSize;
   }

   public int getPreferredSize() {
      return this.preferredSize;
   }

   public void setMinimumSize(int minimumSize) {
      this.minimumSize = minimumSize;
   }

   public void setPreferredSize(int preferredSize) {
      this.preferredSize = preferredSize;
   }

   public void guaranteeMinimumSize(int minimum) {
      if (this.minimumSize < minimum) {
         this.minimumSize = minimum;
      }
   }

   public void guaranteePreferredSize(int preferred) {
      if (this.preferredSize < preferred) {
         this.preferredSize = preferred;
      }
   }

   public void setStart(int start) {
      this.start = start;
   }

   public int getSize() {
      return this.size;
   }

   public int getStart() {
      return this.start;
   }

   public boolean isGrabExcessSpace() {
      return this.grabExcessSpace;
   }

   public void setGrabExcessSpace(boolean grabExcessSpace) {
      this.grabExcessSpace = grabExcessSpace;
   }

   public void incrementMinimumSize(int increment) {
      this.minimumSize += increment;
      if (this.minimumSize < 0) {
         this.minimumSize = 0;
      }
   }

   public void incrementPreferredSize(int increment) {
      this.preferredSize += increment;
      if (this.preferredSize < 0) {
         this.preferredSize = 0;
      }
   }

   public void adjustToPreferredSize() {
      this.size = this.preferredSize;
   }

   public void incrementSize(int increment) {
      this.size += increment;
      if (this.size < 0) {
         this.size = 0;
      }
   }

   public void setSize(int size) {
      this.size = size;
   }
}
