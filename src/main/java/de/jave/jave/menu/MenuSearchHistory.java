package de.jave.jave.menu;

import de.jave.preferences.SmartPreferences;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Only the search controller records invocations; normal menu/toolbar/shortcut use is untracked. */
public final class MenuSearchHistory {
   private final SmartPreferences preferences;
   private final Map<String, Boolean> recent = new LinkedHashMap<>();

   public MenuSearchHistory(SmartPreferences preferences) {
      this.preferences = preferences;
      int count = preferences.getInt("count", 0);
      for (int i = 0; i < count; i++) {
         String id = preferences.get("item." + i, null);
         if (id != null) {
            recent.putIfAbsent(id, true);
         }
      }
   }

   public List<String> ids() {
      return List.copyOf(recent.keySet());
   }

   public void record(MenuCommand command) {
      Map<String, Boolean> previous = new LinkedHashMap<>(recent);
      recent.clear();
      recent.put(command.id(), command.persistent());
      previous.forEach(recent::putIfAbsent);
      List<String> stored = new ArrayList<>();
      recent.forEach((id, persistent) -> {
         if (persistent) {
            stored.add(id);
         }
      });
      int oldCount = preferences.getInt("count", 0);
      for (int i = 0; i < stored.size(); i++) {
         preferences.put("item." + i, stored.get(i));
      }
      for (int i = stored.size(); i < oldCount; i++) {
         preferences.remove("item." + i);
      }
      preferences.put("count", stored.size());
      preferences.flush();
   }
}
