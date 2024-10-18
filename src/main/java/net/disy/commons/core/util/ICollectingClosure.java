package net.disy.commons.core.util;

public interface ICollectingClosure<T, R> extends IClosure<T> {
   R getResult();
}
