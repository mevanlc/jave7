package net.disy.commons.core.errorhandling;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CompositeErrorHandling {
   public static IErrorHandling createFor(final IErrorHandling... errorHandlings) {
      return (IErrorHandling)Proxy.newProxyInstance(CompositeErrorHandling.class.getClassLoader(), new Class[]{IErrorHandling.class}, new InvocationHandler() {
         @Override
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (IErrorHandling errorHandling : errorHandlings) {
               method.invoke(errorHandling, args);
            }

            return null;
         }
      });
   }
}
