package net.dizzy.commons.core.model;

public class BooleanModel extends ObjectModel<Boolean> {
   public BooleanModel() {
      super(Boolean.FALSE);
   }

   public BooleanModel(boolean value) {
      super(Boolean.valueOf(value));
   }
}
