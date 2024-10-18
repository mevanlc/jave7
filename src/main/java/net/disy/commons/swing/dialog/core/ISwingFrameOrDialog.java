package net.disy.commons.swing.dialog.core;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.WindowListener;
import javax.swing.JRootPane;

public interface ISwingFrameOrDialog {
   void setTitle(String var1);

   JRootPane getRootPane();

   void pack();

   void setModal(boolean var1);

   Container getContentPane();

   void setDefaultCloseOperation(int var1);

   void addWindowListener(WindowListener var1);

   void removeWindowListener(WindowListener var1);

   void dispose();

   void validate();

   void repaint();

   Window getWindow();

   void setResizable(boolean var1);

   void show();

   void setVisible(boolean var1);

   boolean isVisible();
}
