package de.jave.figlet.swing.action;

public class RankedItem<T> implements Comparable<RankedItem<T>> {
   private final T value;
   private final double rank;

   public RankedItem(double rank, T value) {
      this.rank = rank;
      this.value = value;
   }

   public int compareTo(RankedItem<T> o) {
      return new Double(this.rank).compareTo(o.rank);
   }

   public T getValue() {
      return this.value;
   }

   public double getRank() {
      return this.rank;
   }
}
