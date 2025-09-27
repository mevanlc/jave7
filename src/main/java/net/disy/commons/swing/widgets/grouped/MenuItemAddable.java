package net.disy.commons.swing.action.grouped;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.disy.commons.core.grouped.IStructuredItemAddable;

public final class MenuItemAddable implements IStructuredItemAddable<JMenuItem> {
   private final JMenu menu;

   public MenuItemAddable(JMenu menu) {
      this.menu = menu;
   }

   @Override
   public void addSeparator() {
      this.menu.addSeparator();
   }

   public void add(JMenuItem item) {
      this.menu.add(item);
   }
}
