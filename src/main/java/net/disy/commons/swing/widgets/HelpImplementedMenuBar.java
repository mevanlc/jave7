package net.disy.commons.swing.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class HelpImplementedMenuBar extends JMenuBar {
   private JMenu helpMenu;

   @Override
   public void setHelpMenu(JMenu menu) {
      if (this.helpMenu != null) {
         this.remove(this.helpMenu);
      }

      this.helpMenu = menu;
      super.add(this.helpMenu);
   }

   @Override
   public JMenu add(JMenu menu) {
      return this.helpMenu != null ? (JMenu)this.add(menu, this.getComponentCount() - 1) : super.add(menu);
   }

   @Override
   public JMenu getHelpMenu() {
      return this.helpMenu;
   }

   public void remove(JMenu menu) {
      if (menu == this.helpMenu) {
         this.helpMenu = null;
      }

      super.remove(menu);
   }

   @Override
   public void removeAll() {
      super.removeAll();
      this.helpMenu = null;
   }
}
