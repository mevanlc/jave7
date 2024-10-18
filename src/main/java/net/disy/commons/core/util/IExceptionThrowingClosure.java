package net.disy.commons.core.util;

public interface IExceptionThrowingClosure<T, E extends Throwable> {
   void execute(T var1) throws E;
}
