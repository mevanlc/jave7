package net.disy.commons.core.provider;

public class CachingProvider<T> implements IProvider<T> {
   private final IProvider<T> provider;
   private T object;

   public CachingProvider(IProvider<T> provider) {
      this.provider = provider;
   }

   @Override
   public T getObject() {
      if (this.object == null) {
         this.object = this.provider.getObject();
      }

      return this.object;
   }
}
