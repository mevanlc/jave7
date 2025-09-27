package de.jave.jave.tool.ellipsealgorithmic;

import de.jave.jave.JaveMessages;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.MouseCharacterPanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class EllipseAlgorithmicOptionsPanel {
   private final JComponent content;
   private final EllipseAlgorithmicOptions options;

   public EllipseAlgorithmicOptionsPanel(final EllipseAlgorithmicOptions options, BooleanModel mixCharactersModel, MouseCharacterModel mouseCharacterModel) {
      this.options = options;
      final MouseCharacterPanel mouseCharacterPanel = new MouseCharacterPanel(mouseCharacterModel);
      final JComboBox chMode = new JComboBox<>(AlgorithmicEllipseStyle.values());
      chMode.setRenderer(new ObjectUiListCellRenderer(new AlgorithmicEllipseStyleUi()));
      chMode.setSelectedItem(options.getStyle());
      chMode.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            options.setStyle((AlgorithmicEllipseStyle)chMode.getSelectedItem());
            EllipseAlgorithmicOptionsPanel.this.updateMouseCharacterPanelEnabled(mouseCharacterPanel);
         }
      });
      this.updateMouseCharacterPanelEnabled(mouseCharacterPanel);
      MergeCharactersPanel mixCharactersPanel = new MergeCharactersPanel(mixCharactersModel);
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel(JaveMessages.Tool_EllipseAlgorithmic_StyleLabel));
      panel.add(chMode);
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalSpan(2);
      panel.add(mouseCharacterPanel.getContent(), data);
      panel.add(mixCharactersPanel.getContent(), data);
      this.content = panel;
   }

   protected void updateMouseCharacterPanelEnabled(MouseCharacterPanel mouseCharacterPanel) {
      mouseCharacterPanel.setEnabled(this.options.getStyle() == AlgorithmicEllipseStyle.CHARACTERS);
   }

   public JComponent getContent() {
      return this.content;
   }
}
