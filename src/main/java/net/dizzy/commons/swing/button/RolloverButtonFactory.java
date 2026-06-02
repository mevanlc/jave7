package net.dizzy.commons.swing.button;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToggleButton;

public final class RolloverButtonFactory {
   private RolloverButtonFactory() {
   }

   public static JButton createRolloverButton(Action action) {
      JButton button = new JButton(action);
      button.setRolloverEnabled(true);
      return button;
   }

   public static JToggleButton createToggleButton(Action action) {
      JToggleButton button = new JToggleButton(action);
      button.setRolloverEnabled(true);
      return button;
   }
}
