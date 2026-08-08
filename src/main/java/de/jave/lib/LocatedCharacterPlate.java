package de.jave.lib;

public class LocatedCharacterPlate extends CharacterPlate {
   private final int originY;
   private final int originX;

   public LocatedCharacterPlate(int[][] content, int originX, int originY) {
      super(content);
      this.originX = originX;
      this.originY = originY;
   }

   public int getOriginX() {
      return this.originX;
   }

   public int getOriginY() {
      return this.originY;
   }

   public void pasteInto(CharacterPlate target) {
      int height = this.getHeight();
      int width = this.getWidth();

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            if (target.contains(x + this.originX, y + this.originY)) {
               int ch = this.glyphAt(x, y);
               if (ch == 160) {
                  target.setForce(x + this.originX, y + this.originY, ' ');
               } else if (ch != 0 && ch != ' ') {
                  target.set(x + this.originX, y + this.originY, ch);
               }
            }
         }
      }
   }
}
