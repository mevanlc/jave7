package de.jave.jave.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/** Isolates the interactive probe from the user's saved application preferences. */
public final class ProbePreferencesFactory implements PreferencesFactory {
   private final Preferences root = new MemoryPreferences(null, "");
   @Override public Preferences userRoot() { return root; }
   @Override public Preferences systemRoot() { return root; }

   private static final class MemoryPreferences extends AbstractPreferences {
      private final Map<String, String> values = new HashMap<>();
      private final Map<String, MemoryPreferences> children = new HashMap<>();
      MemoryPreferences(AbstractPreferences parent, String name) { super(parent, name); }
      @Override protected void putSpi(String key, String value) { values.put(key, value); }
      @Override protected String getSpi(String key) { return values.get(key); }
      @Override protected void removeSpi(String key) { values.remove(key); }
      @Override protected void removeNodeSpi() { values.clear(); children.clear(); }
      @Override protected String[] keysSpi() { return values.keySet().toArray(String[]::new); }
      @Override protected String[] childrenNamesSpi() { return children.keySet().toArray(String[]::new); }
      @Override protected AbstractPreferences childSpi(String name) { return children.computeIfAbsent(name, key -> new MemoryPreferences(this, key)); }
      @Override protected void syncSpi() { }
      @Override protected void flushSpi() { }
   }
}
