package de.jave.jave.actions;

import java.awt.Insets;

public class MinInsetsBuilder {
   private Insets currentInsets = null;

   public void add(Insets insets) {
      if (this.currentInsets == null) {
         this.currentInsets = (Insets)insets.clone();
      } else {
         this.currentInsets.top = Math.min(this.currentInsets.top, insets.top);
         this.currentInsets.left = Math.min(this.currentInsets.left, insets.left);
         this.currentInsets.bottom = Math.min(this.currentInsets.bottom, insets.bottom);
         this.currentInsets.right = Math.min(this.currentInsets.right, insets.right);
      }
   }

   public Insets getInsets() {
      return this.currentInsets;
   }
}
