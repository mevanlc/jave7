package de.jave.jave.open;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;

interface IImageOpenPerformStrategy {
   String getName();

   void perform(Component var1, File var2);

   Icon getIcon();

   String getToolTipText();
}
