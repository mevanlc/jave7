package net.disy.commons.core.model;

public class BooleanModel extends AbstractChangeableModel implements IModifiableBooleanModel {
   private boolean value;

   public BooleanModel() {
      this(false);
   }

   public BooleanModel(boolean value) {
      this.value = value;
   }

   @Override
   public boolean getValue() {
      return this.value;
   }

   @Override
   public void setValue(boolean selected) {
      if (this.value != selected) {
         this.value = selected;
         this.fireChangeEvent();
      }
   }
}
