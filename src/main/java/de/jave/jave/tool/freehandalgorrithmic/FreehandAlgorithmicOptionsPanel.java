package de.jave.jave.tool.freehandalgorrithmic;

import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.MouseCharacterPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class FreehandAlgorithmicOptionsPanel implements IInlineToolOptions {
   private final JComponent content;
   private final FreehandAlgorithmicOptions options;

   public FreehandAlgorithmicOptionsPanel(final FreehandAlgorithmicOptions options, MouseCharacterModel mouseCharacterModel, BooleanModel mixCharactersModel) {
      Ensure.ensureArgumentNotNull(options);
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      Ensure.ensureArgumentNotNull(mixCharactersModel);
      this.options = options;
      final MouseCharacterPanel mouseCharacterPanel = new MouseCharacterPanel(mouseCharacterModel);
      final JComboBox chMode = new JComboBox<>(FreehandAlgorithmicMode.values());
      chMode.setRenderer(new ObjectUiListCellRenderer(new FreehandAlgorithmicModeUi()));
      chMode.setSelectedItem(options.getMode());
      chMode.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            options.setMode((FreehandAlgorithmicMode)chMode.getSelectedItem());
            FreehandAlgorithmicOptionsPanel.this.updateMouseCharactersPanelEnabled(mouseCharacterPanel);
         }
      });
      this.updateMouseCharactersPanelEnabled(mouseCharacterPanel);
      JPanel optionsPanel = new JPanel(new GridDialogLayout(1, false));
      optionsPanel.add(new JLabel("Style:"));
      optionsPanel.add(chMode, GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(mouseCharacterPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      optionsPanel.add(new MergeCharactersPanel(mixCharactersModel).getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.content = optionsPanel;
   }

   private void updateMouseCharactersPanelEnabled(MouseCharacterPanel mouseCharacterPanel) {
      mouseCharacterPanel.setEnabled(this.options.getMode() == FreehandAlgorithmicMode.CHARACTERS);
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
