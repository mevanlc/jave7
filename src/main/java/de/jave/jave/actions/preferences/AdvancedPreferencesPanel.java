package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class AdvancedPreferencesPanel implements IJavePreferencesPanel {
   private final JaveApplicationPreferences preferences;
   private final JComponent content;
   private final BooleanModel nativeFileChooserModel;

   public AdvancedPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      this.nativeFileChooserModel = new BooleanModel(preferences.getUseAwtFileChooserModel().getValue());
      SmartToggleAction awtFileChooserToggleAction = new SmartToggleAction(this.nativeFileChooserModel, "Use native file chooser dialog");
      panel.add(ActionWidgetFactory.createCheckBox(awtFileChooserToggleAction));
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "Advanced";
   }

   @Override
   public void savePreferences() {
      this.preferences.getUseAwtFileChooserModel().setValue(this.nativeFileChooserModel.getValue());
   }
}
