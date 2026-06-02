package net.dizzy.commons.swing.button;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

public class DropDownButton extends JButton {
   private JPopupMenu popupMenu;

   public DropDownButton() {
      this(null);
   }

   public DropDownButton(Action action) {
      super(action);
      addActionListener(event -> {
         if (popupMenu != null) {
            popupMenu.show(this, 0, getHeight());
         }
      });
   }

   public void setPopupMenu(JPopupMenu popupMenu) {
      this.popupMenu = popupMenu;
   }

   public void setMenu(JPopupMenu popupMenu) {
      setPopupMenu(popupMenu);
   }

   public JButton getComponent() {
      return this;
   }
}
