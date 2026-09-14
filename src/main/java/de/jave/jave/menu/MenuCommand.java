package de.jave.jave.menu;

import java.util.Objects;
import javax.swing.Action;

/** The identity and shared behavior of an executable menu entry. Labels are not identities. */
public record MenuCommand(String id, Action action, boolean persistent) {
   public MenuCommand {
      Objects.requireNonNull(id);
      Objects.requireNonNull(action);
      if (id.isBlank()) {
         throw new IllegalArgumentException("A menu command needs an ID");
      }
   }

   public MenuCommand(String id, Action action) {
      this(id, action, true);
   }

   public String label() {
      Object name = action.getValue(Action.NAME);
      return name == null ? "" : name.toString();
   }
}
