package net.dizzy.commons.swing.ui;

import javax.swing.Icon;

public abstract class AbstractObjectUi<T> implements IObjectUi<T> {
   @Override
   public Icon getIcon(T object) {
      return null;
   }
}
