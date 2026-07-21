package de.jave.jave;

import java.awt.Point;
import java.awt.Toolkit;

/**
 * Tracks a multiple-click sequence that began while the selection tool was
 * active. The sequence can span the selection and text tools because a single
 * selection click places the text cursor.
 */
public final class SelectionClickSequence {
   private static final long DEFAULT_MULTI_CLICK_INTERVAL_MILLIS = 500L;
   private final long multiClickIntervalMillis;
   private boolean active;
   private Point firstClickLocation;
   private long firstClickWhen;
   private boolean secondClickPressed;

   public SelectionClickSequence() {
      this(getMultiClickIntervalMillis());
   }

   SelectionClickSequence(long multiClickIntervalMillis) {
      if (multiClickIntervalMillis <= 0L) {
         throw new IllegalArgumentException("multiClickIntervalMillis must be positive");
      }

      this.multiClickIntervalMillis = multiClickIntervalMillis;
   }

   public void start(Point location, long when) {
      if (location == null) {
         this.cancel();
         return;
      }

      this.active = true;
      this.firstClickLocation = new Point(location);
      this.firstClickWhen = when;
      this.secondClickPressed = false;
   }

   public void cancel() {
      this.active = false;
      this.firstClickLocation = null;
      this.secondClickPressed = false;
   }

   public boolean isActive() {
      return this.active;
   }

   public boolean prepareSecondClick(Point location, long when) {
      long elapsed = when - this.firstClickWhen;
      if (
         !this.active
            || location == null
            || !this.firstClickLocation.equals(location)
            || elapsed < 0L
            || elapsed > this.multiClickIntervalMillis
      ) {
         this.cancel();
         return false;
      }

      this.secondClickPressed = true;
      return true;
   }

   public boolean completeSecondClick(Point location) {
      boolean selectSingleCell = this.active
         && this.secondClickPressed
         && location != null
         && this.firstClickLocation.equals(location);
      this.cancel();
      return selectSingleCell;
   }

   private static long getMultiClickIntervalMillis() {
      Object interval = Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
      if (interval instanceof Number && ((Number)interval).longValue() > 0L) {
         return ((Number)interval).longValue();
      }

      return DEFAULT_MULTI_CLICK_INTERVAL_MILLIS;
   }
}
