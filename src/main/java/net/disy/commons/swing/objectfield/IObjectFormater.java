package net.disy.commons.swing.objectfield;

public interface IObjectFormater<T> extends IObjectValidator<String> {
   T parse(String var1);

   String format(T var1);
}
