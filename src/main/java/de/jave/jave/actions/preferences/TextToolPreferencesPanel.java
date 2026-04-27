package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.input.select.RadioButtonPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class TextToolPreferencesPanel implements IJavePreferencesPanel {
   private final JaveApplicationPreferences preferences;
   private final ObjectModel<Boolean> cursorBlockStyleModel;
   private final ObjectModel<Boolean> selectionlessCutCopyOnCellModel;

   public TextToolPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.cursorBlockStyleModel = new ObjectModel<>(preferences.getCursorBlockStyleModel().getValue());
      this.selectionlessCutCopyOnCellModel = new ObjectModel<>(preferences.getSelectionlessCutCopyOnCellModel().getValue());
   }

   @Override
   public JComponent getContent() {
      Boolean[] values = new Boolean[]{Boolean.FALSE, Boolean.TRUE};
      RadioButtonPanel<Boolean> cursorStylePanel = new RadioButtonPanel<>(values, this.cursorBlockStyleModel, new AbstractObjectUi<Boolean>() {
         public String getLabel(Boolean value) {
            return value ? "Block" : "Horizontal line";
         }
      });
      RadioButtonPanel<Boolean> cutCopyModePanel = new RadioButtonPanel<>(values, this.selectionlessCutCopyOnCellModel, new AbstractObjectUi<Boolean>() {
         public String getLabel(Boolean value) {
            return value ? "Operates on cell" : "Operates on document";
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Cursor style in overwrite mode:"));
      panel.add(cursorStylePanel.getContent());
      panel.add(new JSeparator());
      panel.add(new JLabel("Selectionless Cut / Copy:"));
      panel.add(cutCopyModePanel.getContent());
      return panel;
   }

   @Override
   public String getTitle() {
      return "Text Tool";
   }

   @Override
   public void savePreferences() {
      this.preferences.getCursorBlockStyleModel().setValue(this.cursorBlockStyleModel.getValue());
      this.preferences.getSelectionlessCutCopyOnCellModel().setValue(this.selectionlessCutCopyOnCellModel.getValue());
   }
}
