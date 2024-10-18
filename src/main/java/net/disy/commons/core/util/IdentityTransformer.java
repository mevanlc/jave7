package net.disy.commons.core.util;

public class IdentityTransformer<T> implements ITransformer<T, T> {
   @Override
   public T transform(T input) {
      return input;
   }
}
