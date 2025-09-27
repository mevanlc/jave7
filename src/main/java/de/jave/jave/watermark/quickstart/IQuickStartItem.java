package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import javax.swing.Icon;

public interface IQuickStartItem {
   Icon getIcon();

   String getName();

   String getDescription();

   void perform(JavEApplication var1);
}
