package net.dizzy.commons.core.util;

public interface ITransformer<S, T> {
   T transform(S source);
}
