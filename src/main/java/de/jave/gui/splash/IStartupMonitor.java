package de.jave.gui.splash;

import java.awt.Component;

public interface IStartupMonitor {
   int UNKNOWN = -1;

   void beginTask(String var1, int var2);

   void subTask(String var1);

   void dispose();

   Component getParentComponent();
}
