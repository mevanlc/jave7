package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.input.select.RadioButtonPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class TextToolPreferencesPanel implements IJavePreferencesPanel {
   private final JaveApplicationPreferences preferences;
   private final ObjectModel<Boolean> model;

   public TextToolPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.model = new ObjectModel<>(preferences.getCursorBlockStyleModel().getValue());
   }

   @Override
   public JComponent getContent() {
      Boolean[] values = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
      RadioButtonPanel<Boolean> radioButtonPanel = new RadioButtonPanel<>(values, this.model, new AbstractObjectUi<Boolean>() {
         public String getLabel(Boolean value) {
            return value ? "Block" : "Horizontal line";
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Cursor style in overwrite mode:"));
      panel.add(radioButtonPanel.getContent());
      return panel;
   }

   @Override
   public String getTitle() {
      return "Text Tool";
   }

   @Override
   public void savePreferences() {
      this.preferences.getCursorBlockStyleModel().setValue(this.model.getValue());
   }
}
