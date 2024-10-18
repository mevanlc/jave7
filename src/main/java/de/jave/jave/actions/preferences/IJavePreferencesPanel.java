package de.jave.jave.actions.preferences;

import javax.swing.JComponent;

public interface IJavePreferencesPanel {
   String getTitle();

   JComponent getContent();

   void savePreferences();
}
