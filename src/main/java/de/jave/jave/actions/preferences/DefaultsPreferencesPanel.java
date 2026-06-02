package de.jave.jave.actions.preferences;

import de.jave.jave.Tool;
import de.jave.jave.plate.ToolManager;
import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class DefaultsPreferencesPanel implements IJavePreferencesPanel {

   private final JaveApplicationPreferences preferences;
   private final JComboBox<ToolEntry> startupToolCombo;
   private final JComponent content;

   public DefaultsPreferencesPanel(JaveApplicationPreferences preferences, ToolManager toolManager) {
      this.preferences = preferences;

      Tool[] tools = toolManager.getTools();
      ToolEntry[] entries = new ToolEntry[tools.length];
      for (int i = 0; i < tools.length; i++) {
         entries[i] = new ToolEntry(i, tools[i].getName());
      }
      this.startupToolCombo = new JComboBox<>(entries);

      int savedIndex = preferences.getStartupToolIndex();
      if (savedIndex >= 0 && savedIndex < entries.length) {
         this.startupToolCombo.setSelectedIndex(savedIndex);
      }

      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("Startup Tool:"), GridDialogLayoutData.RIGHT);
      panel.add(this.startupToolCombo);
      this.content = panel;
   }

   @Override
   public String getTitle() {
      return "Defaults";
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void savePreferences() {
      ToolEntry selected = (ToolEntry) this.startupToolCombo.getSelectedItem();
      if (selected != null) {
         this.preferences.setStartupToolIndex(selected.index);
      }
   }

   private static final class ToolEntry {
      final int index;
      final String name;

      ToolEntry(int index, String name) {
         this.index = index;
         this.name = name;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }
}
