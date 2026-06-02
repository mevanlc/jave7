package net.dizzy.commons.swing.ui;

import javax.swing.Icon;

public interface IObjectUi<T> {
   default String getLabel(T object) {
      return String.valueOf(object);
   }

   default String getText(T object) {
      return getLabel(object);
   }

   Icon getIcon(T object);

   default String getToolTipText(T object) {
      return getLabel(object);
   }
}
