package net.disy.commons.core.adaptable;

public interface IAdaptable<S> {
   <T extends S> T get(Class<T> var1);
}
