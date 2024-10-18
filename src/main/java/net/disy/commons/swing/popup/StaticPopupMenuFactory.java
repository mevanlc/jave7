package net.disy.commons.swing.popup;

import javax.swing.JPopupMenu;

@Deprecated
public class StaticPopupMenuFactory implements IPopupMenuFactory {
   private final JPopupMenu popup;

   public StaticPopupMenuFactory(JPopupMenu popup) {
      this.popup = popup;
   }

   @Override
   public JPopupMenu createPopupMenu() {
      return this.popup;
   }
}
