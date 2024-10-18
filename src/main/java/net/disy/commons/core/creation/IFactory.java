package net.disy.commons.core.creation;

public interface IFactory<T, E extends Exception> {
   T createInstance() throws E;
}
