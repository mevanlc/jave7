package net.disy.commons.swing.action.grouped;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.disy.commons.core.grouped.IStructuredItemAddable;

public final class PopupMenuItemAddable implements IStructuredItemAddable<JMenuItem> {
   private final JPopupMenu menu;

   public PopupMenuItemAddable(JPopupMenu menu) {
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
