package net.disy.commons.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

@Deprecated
public class SmartButtonGroup extends ButtonGroup {
   public void clear() {
      while (this.getButtonCount() > 0) {
         this.remove(this.getElements().nextElement());
      }
   }

   public int getSelectedIndex() {
      for (int i = 0; i < this.buttons.size(); i++) {
         if (this.getButton(i).isSelected()) {
            return i;
         }
      }

      return -1;
   }

   public void setSelectedIndex(int index) {
      AbstractButton button = this.getButton(index);
      button.setSelected(true);
   }

   public AbstractButton getButton(int index) {
      return this.buttons.get(index);
   }
}
