package net.disy.commons.core.util;

import net.disy.commons.core.provider.IProvider;

public class NullProvider<T> implements IProvider<T> {
   @Override
   public T getObject() {
      return null;
   }
}
