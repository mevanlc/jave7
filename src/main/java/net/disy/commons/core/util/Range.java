package net.disy.commons.core.util;

import java.util.Set;
import java.util.TreeSet;

public class Range {
   private final int lowerBound;
   private final int upperBound;

   public Range(int lowerBound, int upperBound) {
      if (lowerBound > upperBound) {
         throw new IllegalArgumentException("LowerBound may not be less than upperBound: " + lowerBound + " " + upperBound);
      } else {
         this.lowerBound = lowerBound;
         this.upperBound = upperBound;
      }
   }

   public int getLowerBound() {
      return this.lowerBound;
   }

   public int getUpperBound() {
      return this.upperBound;
   }

   public boolean contains(int value) {
      return value >= this.lowerBound && value <= this.upperBound;
   }

   public Set<String> getStringSet() {
      Set<String> content = new TreeSet<>();

      for (int value = this.lowerBound; value <= this.upperBound; value++) {
         content.add(String.valueOf(value));
      }

      return content;
   }

   @Override
   public String toString() {
      return "Range[" + this.getLowerBound() + "," + this.getUpperBound() + "]";
   }

   public int getWidth() {
      return this.getUpperBound() - this.getLowerBound();
   }
}
