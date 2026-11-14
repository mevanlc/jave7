package de.jave.jave;

import de.jave.jave.plate.JaveMainPanel;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.RootPaneContainer;

/** Identifies pickers that insert into the active canvas while keeping keyboard focus. */
public final class CharacterPickerFocus {
   private static final String MAIN_PANEL_PROPERTY = "jave.characterPickerMainPanel";

   private CharacterPickerFocus() {
   }

   public static void register(JDialog dialog, JaveMainPanel mainPanel) {
      dialog.getRootPane().putClientProperty(MAIN_PANEL_PROPERTY, mainPanel);
   }

   public static boolean isFocusedFor(Plate plate) {
      Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
      if (window instanceof RootPaneContainer container
         && container.getRootPane().getClientProperty(MAIN_PANEL_PROPERTY) instanceof JaveMainPanel mainPanel) {
         return mainPanel.getPlate() == plate;
      }
      return false;
   }
}
