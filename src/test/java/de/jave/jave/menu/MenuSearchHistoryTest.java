package de.jave.jave.menu;

import de.jave.preferences.SmartPreferences;
import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;
import javax.swing.JMenuBar;
import org.junit.Assert;
import org.junit.Test;

public class MenuSearchHistoryTest {
   @Test
   public void persistsUniqueRecentSearchCommandsButNotOrdinaryInvocationsOrTransientWindows() throws Exception {
      Preferences node = Preferences.userRoot().node("JavE-menu-search-test-" + UUID.randomUUID());
      try {
         MenuSearchHistory history = new MenuSearchHistory(new SmartPreferences(node));
         MenuHub hub = new MenuHub(new JMenuBar());
         var ordinary = hub.item("ordinary", MenuHub.action("Ordinary", parent -> {}));
         ordinary.doClick(0);
         Assert.assertEquals(List.of(), history.ids());
         MenuCommand first = new MenuCommand("first", MenuHub.action("First", parent -> {}));
         MenuCommand second = new MenuCommand("second", MenuHub.action("Second", parent -> {}));
         history.record(first);
         history.record(second);
         history.record(first);
         history.record(new MenuCommand("window.unsaved", MenuHub.action("Untitled", parent -> {}), false));
         Assert.assertEquals(List.of("window.unsaved", "first", "second"), history.ids());
         MenuSearchHistory restored = new MenuSearchHistory(new SmartPreferences(node));
         Assert.assertEquals(List.of("first", "second"), restored.ids());
      } finally {
         node.removeNode();
         Preferences.userRoot().flush();
      }
   }
}
