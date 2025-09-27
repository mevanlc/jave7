package de.jave.jave.actions.preferences;

import de.jave.ascii.font.ChooseDisplayFontAction;
import de.jave.jave.actions.quickstart.QuickStartAction;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class GeneralPreferencesPanel implements IJavePreferencesPanel {
   private final JaveApplicationPreferences preferences;
   private final JComponent content;
   private final SpinnerNumberModel recentFileMaxCountModel;
   private final FontModel fontModel;
   private final JComboBox colorSchemeComboBox;

   public GeneralPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      this.recentFileMaxCountModel = new SpinnerNumberModel(preferences.getRecentFileList().getMaxSize(), 3, 20, 1);
      panel.add(new JLabel("Files in recent file list:"), GridDialogLayoutData.RIGHT);
      panel.add(new JSpinner(this.recentFileMaxCountModel));
      GridDialogLayoutData twoColumnsData = GridDialogLayoutDataFactory.createHorizontalSpanData(2);
      panel.add(QuickStartAction.createShowQuickStartCheckBox(preferences), twoColumnsData);
      this.fontModel = new FontModel(preferences.getDisplayFontModel().getFont());
      panel.add(new JButton(new ChooseDisplayFontAction(this.fontModel)), twoColumnsData);
      ColorScheme[] colorSchemes = ColorScheme.getAll();
      this.colorSchemeComboBox = new JComboBox<>(colorSchemes);
      this.colorSchemeComboBox.setSelectedItem(preferences.getDefaultColorSchemeModel().getValue());
      this.colorSchemeComboBox.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<ColorScheme>() {
         public String getLabel(ColorScheme value) {
            return value.getName();
         }

         public Icon getIcon(ColorScheme value) {
            return value.getIcon();
         }
      }));
      panel.add(new JLabel("Default Color Scheme:"), GridDialogLayoutData.RIGHT);
      panel.add(this.colorSchemeComboBox);
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "General";
   }

   @Override
   public void savePreferences() {
      int maxSize = this.recentFileMaxCountModel.getNumber().intValue();
      this.preferences.getRecentFileList().setMaxSize(maxSize);
      this.preferences.getDisplayFontModel().setFont(this.fontModel.getFont());
      this.preferences.getDefaultColorSchemeModel().setValue((ColorScheme)this.colorSchemeComboBox.getSelectedItem());
   }
}
