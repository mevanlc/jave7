package net.disy.commons.core.model;

public class ObjectModel<T> extends SmartChangeableModel implements IObjectModel<T> {
   private final SmartChangeableModel.IProperty<T> property = new DefaultProperty<>();

   public ObjectModel() {
      this(null);
   }

   public ObjectModel(T value) {
      this.property.setValue(value);
   }

   @Override
   public T getValue() {
      return this.getValue(this.property);
   }

   @Override
   public void setValue(T value) {
      this.setValue(this.property, value);
   }

   @Override
   public String toString() {
      return "ObjectModel{" + this.property.getValue() + "}";
   }
}
