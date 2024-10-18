package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
   private final BooleanModel smallFrameOptionsDialogModel;

   public AdvancedPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      this.nativeFileChooserModel = new BooleanModel(preferences.getUseAwtFileChooserModel().getValue());
      this.smallFrameOptionsDialogModel = new BooleanModel(preferences.isSmallOptionsDialog());
      SmartToggleAction awtFileChooserToggleAction = new SmartToggleAction(this.nativeFileChooserModel, "Use native file chooser dialog");
      panel.add(ActionWidgetFactory.createCheckBox(awtFileChooserToggleAction));
      SmartToggleAction smallFrameOptionsDialogToggleAction = new SmartToggleAction(this.smallFrameOptionsDialogModel, "Use small frame for options dialog");
      panel.add(ActionWidgetFactory.createCheckBox(smallFrameOptionsDialogToggleAction));
      panel.add(new JLabel("(Might not work on some systems. Will be applied after restart)"));
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
      this.preferences.setSmallFrame(this.smallFrameOptionsDialogModel.getValue());
   }
}
