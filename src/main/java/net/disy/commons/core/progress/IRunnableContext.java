package net.disy.commons.core.progress;

import java.lang.reflect.InvocationTargetException;

public interface IRunnableContext {
   void run(IInterruptableRunnableWithProgress var1) throws InterruptedException, InvocationTargetException;

   void run(INonInterruptableRunnableWithProgress var1) throws InvocationTargetException;
}
