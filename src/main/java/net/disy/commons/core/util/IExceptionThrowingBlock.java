package net.disy.commons.core.util;

public interface IExceptionThrowingBlock<T extends Throwable> {
   void execute() throws T;
}
