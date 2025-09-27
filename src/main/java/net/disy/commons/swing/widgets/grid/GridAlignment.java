package net.disy.commons.swing.layout.grid;

public class GridAlignment {
   public static final GridAlignment BEGINNING = new GridAlignment("beginning");
   public static final GridAlignment CENTER = new GridAlignment("center");
   public static final GridAlignment END = new GridAlignment("end");
   public static final GridAlignment FILL = new GridAlignment("fill");
   private final String name;

   private GridAlignment(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return "GridAlignment{" + this.name + "}";
   }
}
