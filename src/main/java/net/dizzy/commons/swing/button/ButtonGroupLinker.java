package net.dizzy.commons.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import net.dizzy.commons.core.model.ObjectModel;

public class ButtonGroupLinker<T> {
   private final ButtonGroup group = new ButtonGroup();
   private final ObjectModel<T> model;

   public ButtonGroupLinker(ObjectModel<T> model) {
      this.model = model;
   }

   public static ButtonGroup link(AbstractButton... buttons) {
      ButtonGroup group = new ButtonGroup();
      for (AbstractButton button : buttons) {
         group.add(button);
      }
      return group;
   }

   public void addButton(AbstractButton button, T value) {
      group.add(button);
      button.setSelected(java.util.Objects.equals(model.getValue(), value));
      button.addActionListener(event -> model.setValue(value));
   }
}
