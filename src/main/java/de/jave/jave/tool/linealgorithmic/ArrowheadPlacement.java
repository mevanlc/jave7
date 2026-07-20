package de.jave.jave.tool.linealgorithmic;

public enum ArrowheadPlacement {
   NONE("None"),
   START("Start"),
   END("End"),
   BOTH("Both");

   private final String label;

   ArrowheadPlacement(String label) {
      this.label = label;
   }

   public ArrowheadPlacement next() {
      ArrowheadPlacement[] values = values();
      return values[(this.ordinal() + 1) % values.length];
   }

   @Override
   public String toString() {
      return this.label;
   }
}
