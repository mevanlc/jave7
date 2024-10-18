package net.disy.commons.core.util;

public interface IBlock extends IExceptionThrowingBlock<RuntimeException> {
   IBlock EMPTY = new NullBlock();
}
