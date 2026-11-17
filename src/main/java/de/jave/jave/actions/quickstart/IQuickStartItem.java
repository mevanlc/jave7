package de.jave.jave.actions.quickstart;

import de.jave.jave.JaveApplication;
import javax.swing.Icon;

public interface IQuickStartItem {
   Icon getIcon();

   String getName();

   String getDescription();

   void perform(JaveApplication var1);
}
