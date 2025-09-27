package de.jave.jave.algorithm.camel;

public final class CamelRun {
   private final int startX;
   private final int endX;
   private final int y;
   private String text;

   public CamelRun(int startX, int endX, int y) {
      this.startX = startX;
      this.endX = endX;
      this.y = y;
   }

   public int getStartX() {
      return this.startX;
   }

   public int getEndX() {
      return this.endX;
   }

   public int getY() {
      return this.y;
   }

   public int getLength() {
      return this.endX - this.startX + 1;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public String toString() {
      return "[" + this.startX + ".." + this.endX + ";" + this.y + "]=" + this.getLength() + "<" + this.text + ">";
   }
}
