package de.jave.image2ascii.algorithm.dialog;

import de.jave.gui.layout.Gap;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.algorithm.Algorithm4OptionsModel;
import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersPanel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTablePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public class Image2AsciiAlgorithm4OptionsPanel implements IDisposableComponentContainer {
   private final Algorithm4OptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final BannedCharactersPanel bannedCharactersPanel;
   private final GreyScaleTablePanel greyScaleTablePanel;
   private final JComponent content;

   public Image2AsciiAlgorithm4OptionsPanel(final Algorithm4OptionsModel optionsModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.optionsModel = optionsModel;
      final JCheckBox cbOptimize = new JCheckBox("Consider line distance", optionsModel.isOptimize());
      cbOptimize.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setOptimize(cbOptimize.isSelected());
         }
      });
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            cbOptimize.setSelected(optionsModel.isOptimize());
         }
      };
      optionsModel.addChangeListener(this.optionsModelChangeListener);
      GridDialogPanelBuilder gridPanel = new GridDialogPanelBuilder();
      this.greyScaleTablePanel = new GreyScaleTablePanel(
         optionsModel.getGreyScaleTableSelectionModel(), optionsModel.getDisplayFontModel(), greyscaleTableConfiguration
      );
      gridPanel.add(this.greyScaleTablePanel);
      this.bannedCharactersPanel = new BannedCharactersPanel(optionsModel.getBannedCharactersModel(), optionsModel.getDisplayFontModel());
      gridPanel.add(this.bannedCharactersPanel);
      gridPanel.add(new IDialogComponent() {
         @Override
         public void fillInto(JPanel panel, int columnCount) {
            panel.add(new Gap());
            GridDialogLayoutData layoutData = new GridDialogLayoutData();
            layoutData.setHorizontalSpan(columnCount - 1);
            panel.add(cbOptimize);
         }

         @Override
         public int getColumnCount() {
            return 2;
         }
      });
      this.content = gridPanel.createPanel();
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void dispose() {
      this.optionsModel.removeChangeListener(this.optionsModelChangeListener);
      this.greyScaleTablePanel.dispose();
      this.bannedCharactersPanel.dispose();
   }
}
