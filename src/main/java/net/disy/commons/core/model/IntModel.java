package net.disy.commons.core.model;

public class IntModel extends AbstractChangeableModel {
   private int value;

   public IntModel() {
      this(0);
   }

   public IntModel(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      if (this.value != value) {
         this.value = value;
         this.fireChangeEvent();
      }
   }
}
