package net.disy.commons.core.util;

public interface IReturningClosure<R, T extends Throwable> {
   R execute() throws T;
}
