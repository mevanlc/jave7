package net.disy.commons.swing.ui;

public class DefaultObjectUi<T> extends AbstractObjectUi<T> {
   @Override
   public String getLabel(T value) {
      return value == null ? null : value.toString();
   }
}
