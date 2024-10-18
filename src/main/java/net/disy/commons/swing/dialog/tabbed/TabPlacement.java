package net.disy.commons.swing.dialog.tabbed;

public enum TabPlacement {
   TOP(1),
   BOTTOM(3),
   LEFT(2),
   RIGHT(4);

   private final int swingValue;

   private TabPlacement(int swingValue) {
      this.swingValue = swingValue;
   }

   public int getSwingValue() {
      return this.swingValue;
   }
}
