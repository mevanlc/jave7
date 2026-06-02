package net.dizzy.commons.swing.util;

import java.awt.Component;

import javax.swing.AbstractButton;

public final class ToggleComponentEnabler {
   private ToggleComponentEnabler() {
   }

   public static void connect(AbstractButton button, Component... components) {
      Runnable update = () -> {
         for (Component component : components) {
            component.setEnabled(button.isSelected());
         }
      };
      button.addActionListener(event -> update.run());
      update.run();
   }
}
