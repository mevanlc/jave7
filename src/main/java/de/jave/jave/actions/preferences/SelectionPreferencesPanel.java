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

public class SelectionPreferencesPanel implements IJavePreferencesPanel {
   private final JaveApplicationPreferences preferences;
   private final ObjectModel<Boolean> pasteVFillsSelectionModel;

   public SelectionPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.pasteVFillsSelectionModel = new ObjectModel<>(preferences.getPasteVFillsSelectionModel().getValue());
   }

   @Override
   public JComponent getContent() {
      Boolean[] values = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
      RadioButtonPanel<Boolean> radioButtonPanel = new RadioButtonPanel<>(values, this.pasteVFillsSelectionModel, new AbstractObjectUi<Boolean>() {
         public String getLabel(Boolean value) {
            return value ? "Fills Selection" : "Ignores Selection";
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Pasting into a Selection:"));
      panel.add(radioButtonPanel.getContent());
      return panel;
   }

   @Override
   public String getTitle() {
      return "Selection";
   }

   @Override
   public void savePreferences() {
      this.preferences.getPasteVFillsSelectionModel().setValue(this.pasteVFillsSelectionModel.getValue());
   }
}
