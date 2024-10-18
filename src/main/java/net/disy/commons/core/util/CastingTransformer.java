package net.disy.commons.core.util;

public final class CastingTransformer<I, O> implements ITransformer<I, O> {
   @Override
   public O transform(I input) {
      return (O)input;
   }
}
