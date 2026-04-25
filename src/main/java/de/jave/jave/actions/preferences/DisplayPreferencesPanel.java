package de.jave.jave.actions.preferences;

import de.jave.ascii.plate.CellScalingMode;
import de.jave.jave.preferences.PlatePreferences;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.ui.AbstractObjectUi;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class DisplayPreferencesPanel implements IJavePreferencesPanel {
   private static final double SCALE_MIN = 0.1;
   private static final double SCALE_MAX = 20.0;
   private static final double SCALE_STEP = 0.05;

   private final PlatePreferences platePreferences;
   private final JComponent content;
   private final JComboBox<CellScalingMode> cellScalingComboBox;
   private final JPanel scaleFieldsPanel;
   private final SpinnerNumberModel widthScaleModel;
   private final SpinnerNumberModel heightScaleModel;

   private final CellScalingMode originalMode;
   private final float originalWidthScale;
   private final float originalHeightScale;

   public DisplayPreferencesPanel(PlatePreferences platePreferences) {
      Ensure.ensureArgumentNotNull(platePreferences);
      this.platePreferences = platePreferences;
      this.originalMode = platePreferences.getCellScalingMode();
      this.originalWidthScale = platePreferences.getCellScalingWidth();
      this.originalHeightScale = platePreferences.getCellScalingHeight();

      JPanel panel = new JPanel(new GridDialogLayout(2, false));

      this.cellScalingComboBox = new JComboBox<>(CellScalingMode.values());
      this.cellScalingComboBox.setSelectedItem(this.originalMode);
      this.cellScalingComboBox.setRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<CellScalingMode>() {
         public String getLabel(CellScalingMode value) {
            return value.getDisplayName();
         }
      }));
      panel.add(new JLabel("Cell scaling:"), GridDialogLayoutData.RIGHT);
      panel.add(this.cellScalingComboBox);

      this.widthScaleModel = new SpinnerNumberModel((double)this.originalWidthScale, SCALE_MIN, SCALE_MAX, SCALE_STEP);
      this.heightScaleModel = new SpinnerNumberModel((double)this.originalHeightScale, SCALE_MIN, SCALE_MAX, SCALE_STEP);
      this.scaleFieldsPanel = new JPanel(new GridDialogLayout(4, false));
      this.scaleFieldsPanel.add(new JLabel("Width:"), GridDialogLayoutData.RIGHT);
      this.scaleFieldsPanel.add(new JSpinner(this.widthScaleModel));
      this.scaleFieldsPanel.add(new JLabel("Height:"), GridDialogLayoutData.RIGHT);
      this.scaleFieldsPanel.add(new JSpinner(this.heightScaleModel));
      panel.add(this.scaleFieldsPanel, GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      this.scaleFieldsPanel.setVisible(this.originalMode == CellScalingMode.SCALED);

      this.cellScalingComboBox.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
               CellScalingMode mode = (CellScalingMode)DisplayPreferencesPanel.this.cellScalingComboBox.getSelectedItem();
               if (mode != null) {
                  DisplayPreferencesPanel.this.platePreferences.getCellScalingModeModel().setValue(mode);
                  DisplayPreferencesPanel.this.scaleFieldsPanel.setVisible(mode == CellScalingMode.SCALED);
                  DisplayPreferencesPanel.this.scaleFieldsPanel.revalidate();
               }
            }
         }
      });

      ChangeListener scaleListener = new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            DisplayPreferencesPanel.this.platePreferences.getCellScalingWidthModel().setValue((float)DisplayPreferencesPanel.this.widthScaleModel.getNumber().doubleValue());
            DisplayPreferencesPanel.this.platePreferences.getCellScalingHeightModel().setValue((float)DisplayPreferencesPanel.this.heightScaleModel.getNumber().doubleValue());
         }
      };
      this.widthScaleModel.addChangeListener(scaleListener);
      this.heightScaleModel.addChangeListener(scaleListener);

      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "Display";
   }

   @Override
   public void savePreferences() {
      CellScalingMode mode = (CellScalingMode)this.cellScalingComboBox.getSelectedItem();
      if (mode != null) {
         this.platePreferences.setCellScalingMode(mode);
      }
      this.platePreferences.setCellScalingWidth((float)this.widthScaleModel.getNumber().doubleValue());
      this.platePreferences.setCellScalingHeight((float)this.heightScaleModel.getNumber().doubleValue());
   }

   @Override
   public void revert() {
      this.platePreferences.getCellScalingModeModel().setValue(this.originalMode);
      this.platePreferences.getCellScalingWidthModel().setValue(this.originalWidthScale);
      this.platePreferences.getCellScalingHeightModel().setValue(this.originalHeightScale);
   }
}
