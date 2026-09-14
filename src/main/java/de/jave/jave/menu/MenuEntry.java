package de.jave.jave.menu;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/** A command's current location in the primary menu definition. */
public record MenuEntry(MenuCommand command, JMenuItem item, List<JMenu> parents) {
   public MenuEntry {
      parents = List.copyOf(parents);
   }

   public boolean isEnabled() {
      return command.action().isEnabled() && item.isEnabled() && parents.stream().allMatch(JMenu::isEnabled);
   }

   public String location() {
      return String.join(" > ", parents.stream().map(JMenu::getText).toList()) + " > " + command.label();
   }
}
