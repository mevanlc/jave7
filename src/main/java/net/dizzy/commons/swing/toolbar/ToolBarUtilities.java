package net.dizzy.commons.swing.toolbar;

import javax.swing.JToolBar;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;

public final class ToolBarUtilities {
   private ToolBarUtilities() {
   }

   public static void configureDefault(JToolBar toolBar) {
      toolBar.setFloatable(false);
   }

   public static AbstractButton createToolBarButton(Action action) {
      JButton button = new JButton(action);
      button.setFocusable(false);
      return button;
   }
}
