package de.jave.jave.actions.preferences;

import javax.swing.JComponent;

public interface IJavePreferencesPanel {
   String getTitle();

   JComponent getContent();

   void savePreferences();

   /**
    * Undo any in-flight (live-preview) state mutations performed while the
    * dialog was open. Called when the user cancels the dialog.
    */
   default void revert() {
   }
}
