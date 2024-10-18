package net.disy.commons.core.provider;

public class StaticProvider<T> implements IProvider<T> {
   private final T providedObject;

   public StaticProvider(T providedObject) {
      this.providedObject = providedObject;
   }

   @Override
   public T getObject() {
      return this.providedObject;
   }
}
