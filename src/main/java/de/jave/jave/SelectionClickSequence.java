package de.jave.jave;

/**
 * Tracks a multiple-click sequence that began while the selection tool was
 * active. The sequence can span the selection and text tools because a single
 * selection click places the text cursor.
 */
public final class SelectionClickSequence {
   private boolean active;
   private boolean startedWithSelection;

   public void start(boolean hasSelection) {
      this.active = true;
      this.startedWithSelection = hasSelection;
   }

   public void cancel() {
      this.active = false;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean selectsSingleCellAt(int clickCount) {
      return this.active && clickCount == 2;
   }

   public boolean selectsFloodRegionAt(int clickCount) {
      return this.active && !this.startedWithSelection && clickCount == 3;
   }
}
