package de.jave.jave.tool.linealgorithmic;

import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.MouseCharacterPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class LineAlgorithmicOptionsPanel implements IInlineToolOptions {
   private final JComponent content;
   private final LineAlgorithmicOptions options;

   public LineAlgorithmicOptionsPanel(final LineAlgorithmicOptions options, BooleanModel mixCharactersModel, MouseCharacterModel mouseCharacterModel) {
      this.options = options;
      final JComboBox<AlgorithmicLineStyle> chMode = new JComboBox<>(AlgorithmicLineStyle.values());
      final MouseCharacterPanel mouseCharacterPanel = new MouseCharacterPanel(mouseCharacterModel);
      chMode.setRenderer(new ObjectUiListCellRenderer(new AlgorithmicLineStyleUi()));
      chMode.setSelectedItem(options.getStyle());
      chMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setStyle((AlgorithmicLineStyle)chMode.getSelectedItem());
            LineAlgorithmicOptionsPanel.this.updateMouseCharactersPanelEnabled(mouseCharacterPanel);
         }
      });
      this.updateMouseCharactersPanelEnabled(mouseCharacterPanel);

      final JComboBox<ArrowheadPlacement> arrowheadPlacementComboBox = new JComboBox<>(ArrowheadPlacement.values());
      arrowheadPlacementComboBox.setSelectedItem(options.getArrowheadPlacement());
      final SpinnerNumberModel arrowheadSizeModel = new SpinnerNumberModel(
         options.getArrowheadSize(), LineAlgorithmicOptions.ARROWHEAD_SIZE_MIN, LineAlgorithmicOptions.ARROWHEAD_SIZE_MAX, 1
      );
      final JSpinner arrowheadSizeSpinner = new JSpinner(arrowheadSizeModel);
      final SpinnerNumberModel arrowheadAngleModel = new SpinnerNumberModel(
         options.getArrowheadAngle(), LineAlgorithmicOptions.ARROWHEAD_ANGLE_MIN, LineAlgorithmicOptions.ARROWHEAD_ANGLE_MAX, 5
      );
      final JSpinner arrowheadAngleSpinner = new JSpinner(arrowheadAngleModel);
      final JCheckBox cardinalTipsCheckBox = new JCheckBox("Cardinal tips", options.isCardinalTips());

      arrowheadPlacementComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setArrowheadPlacement((ArrowheadPlacement)arrowheadPlacementComboBox.getSelectedItem());
         }
      });
      arrowheadSizeModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            options.setArrowheadSize(arrowheadSizeModel.getNumber().intValue());
         }
      });
      arrowheadAngleModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            options.setArrowheadAngle(arrowheadAngleModel.getNumber().intValue());
         }
      });
      cardinalTipsCheckBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setCardinalTips(cardinalTipsCheckBox.isSelected());
         }
      });
      options.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            arrowheadPlacementComboBox.setSelectedItem(options.getArrowheadPlacement());
            arrowheadSizeModel.setValue(Integer.valueOf(options.getArrowheadSize()));
            arrowheadAngleModel.setValue(Integer.valueOf(options.getArrowheadAngle()));
            cardinalTipsCheckBox.setSelected(options.isCardinalTips());
         }
      });

      JPanel optionsPanel = new JPanel(new GridDialogLayout(1, false));
      optionsPanel.add(new JLabel("Style:"));
      optionsPanel.add(chMode, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(createSpinnerPanel("Arrow:", arrowheadPlacementComboBox), GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(createSpinnerPanel("Size:", arrowheadSizeSpinner), GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(createSpinnerPanel("Angle:", arrowheadAngleSpinner), GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(cardinalTipsCheckBox, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(mouseCharacterPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(new MergeCharactersPanel(mixCharactersModel).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.content = optionsPanel;
   }

   private void updateMouseCharactersPanelEnabled(MouseCharacterPanel mouseCharacterPanel) {
      mouseCharacterPanel.setEnabled(this.options.getStyle() == AlgorithmicLineStyle.CHARACTERS);
   }

   private static JComponent createSpinnerPanel(String label, JComponent component) {
      JPanel panel = new JPanel(new BorderLayout(4, 0));
      panel.add(new JLabel(label), BorderLayout.WEST);
      panel.add(component, BorderLayout.CENTER);
      return panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
