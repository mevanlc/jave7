package net.disy.commons.swing.ui;

import javax.swing.Icon;

public abstract class AbstractObjectUi<T> implements IObjectUi<T> {
   @Override
   public Icon getIcon(T value) {
      return null;
   }

   @Override
   public String getLabel(T value) {
      return null;
   }

   @Override
   public String getToolTipText(T value) {
      return this.getLabel(value);
   }
}
