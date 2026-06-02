package net.dizzy.commons.core.util;

public interface IClosure<T> {
   void execute(T value) throws RuntimeException;
}
