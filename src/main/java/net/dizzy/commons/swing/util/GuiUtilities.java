package net.dizzy.commons.swing.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public final class GuiUtilities {
   private GuiUtilities() {
   }

   public static Window getWindowFor(Component component) {
      if (component == null) {
         return null;
      }
      return component instanceof Window ? (Window) component : SwingUtilities.getWindowAncestor(component);
   }

   public static Window getWindowFor(MouseEvent event) {
      return getWindowFor(event.getComponent());
   }

   public static void centerOnScreen(Window window) {
      window.setLocationRelativeTo(null);
   }
}
