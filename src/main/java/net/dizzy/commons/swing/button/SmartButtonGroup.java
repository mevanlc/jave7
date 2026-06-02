package net.dizzy.commons.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.util.ArrayList;
import java.util.List;

public class SmartButtonGroup extends ButtonGroup {
   private final List<AbstractButton> buttons = new ArrayList<>();

   public SmartButtonGroup(AbstractButton... buttons) {
      for (AbstractButton button : buttons) {
         add(button);
      }
   }

   @Override
   public void add(AbstractButton button) {
      super.add(button);
      buttons.add(button);
   }

   public AbstractButton getButton(int index) {
      return buttons.get(index);
   }

   public int getSelectedIndex() {
      for (int i = 0; i < buttons.size(); i++) {
         if (buttons.get(i).isSelected()) {
            return i;
         }
      }
      return -1;
   }

   public void setSelectedIndex(int index) {
      buttons.get(index).setSelected(true);
   }
}
