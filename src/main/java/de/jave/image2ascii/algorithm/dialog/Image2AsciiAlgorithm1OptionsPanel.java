package de.jave.image2ascii.algorithm.dialog;

import de.jave.gui.layout.Gap;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.algorithm.Algorithm1OptionsModel;
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

public class Image2AsciiAlgorithm1OptionsPanel implements IDisposableComponentContainer {
   private final Algorithm1OptionsModel optionsModel;
   private final IChangeListener optionsModelChangeListener;
   private final GreyScaleTablePanel greyScaleTablePanel;
   private final BannedCharactersPanel bannedCharactersPanel;
   private final JComponent content;

   public Image2AsciiAlgorithm1OptionsPanel(final Algorithm1OptionsModel optionsModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.optionsModel = optionsModel;
      final JCheckBox cbDithering = new JCheckBox("Error correction (dithering)", optionsModel.isDithering());
      cbDithering.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            optionsModel.setDithering(cbDithering.isSelected());
         }
      });
      this.optionsModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            cbDithering.setSelected(optionsModel.isDithering());
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
            panel.add(cbDithering);
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
