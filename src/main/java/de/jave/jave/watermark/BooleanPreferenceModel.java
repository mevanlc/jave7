package de.jave.jave.preferences;

import de.jave.preferences.SmartPreferences;
import net.disy.commons.core.model.AbstractChangeableModel;

public class BooleanPreferenceModel extends AbstractChangeableModel {
   private final boolean defaultValue;
   private final String key;
   private final SmartPreferences preferences;

   public BooleanPreferenceModel(SmartPreferences preferences, String key, boolean defaultValue) {
      this.preferences = preferences;
      this.key = key;
      this.defaultValue = defaultValue;
   }

   public boolean getValue() {
      return this.preferences.getBoolean(this.key, this.defaultValue);
   }

   public void setValue(boolean value) {
      if (value != this.getValue()) {
         this.preferences.put(this.key, value);
         this.fireChangeEvent();
      }
   }
}
