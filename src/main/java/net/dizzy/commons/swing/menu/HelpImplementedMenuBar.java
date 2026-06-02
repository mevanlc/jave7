package net.dizzy.commons.swing.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class HelpImplementedMenuBar extends JMenuBar {
   @Override
   public void setHelpMenu(JMenu menu) {
      add(menu);
   }
}
