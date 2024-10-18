package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class AnimationEditorPreferencesPanel implements IJavePreferencesPanel {
   private final JComponent content;
   private final JaveApplicationPreferences preferences;
   private final PlatePreferences platePreferences;
   private final JCheckBox autoResizeCheckBox;
   private final SpinnerNumberModel defaultWidthModel;
   private final SpinnerNumberModel defaultHeightModel;

   public AnimationEditorPreferencesPanel(JaveApplicationPreferences preferences, PlatePreferences platePreferences) {
      Ensure.ensureArgumentNotNull(preferences);
      Ensure.ensureArgumentNotNull(platePreferences);
      this.preferences = preferences;
      this.platePreferences = platePreferences;
      this.defaultWidthModel = new SpinnerNumberModel(preferences.getDefaultAnimationSize().width, 10, 100, 1);
      this.defaultHeightModel = new SpinnerNumberModel(preferences.getDefaultAnimationSize().height, 10, 100, 1);
      this.autoResizeCheckBox = new JCheckBox("Auto expand when dropping selection", platePreferences.isAutoResizeOnDropForAnimationEditor());
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("Default frame width:"), GridDialogLayoutData.RIGHT);
      panel.add(new JSpinner(this.defaultWidthModel));
      panel.add(new JLabel("Default frame height:"), GridDialogLayoutData.RIGHT);
      panel.add(new JSpinner(this.defaultHeightModel));
      panel.add(this.autoResizeCheckBox, GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "Animation Editor";
   }

   @Override
   public void savePreferences() {
      this.preferences.setDefaultAnimationSize(new Dimension(this.defaultWidthModel.getNumber().intValue(), this.defaultHeightModel.getNumber().intValue()));
      this.platePreferences.setAutoResizeOnDropForAnimationEditor(this.autoResizeCheckBox.isSelected());
   }
}
