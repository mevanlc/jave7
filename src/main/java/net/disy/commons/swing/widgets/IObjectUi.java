package net.disy.commons.swing.ui;

import javax.swing.Icon;

public interface IObjectUi<T> {
   Icon getIcon(T var1);

   String getLabel(T var1);

   String getToolTipText(T var1);
}
