package de.jave.image2ascii.algorithm.dialog;

import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.algorithm.AlgorithmJavEOptionsModel;
import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersPanel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTablePanel;
import javax.swing.JComponent;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IDisposableComponentContainer;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;

public class Image2AsciiAlgorithmJavEOptionsPanel implements IDisposableComponentContainer {
   private final BannedCharactersPanel bannedCharactersPanel;
   private final GreyScaleTablePanel greyScaleTablePanel;
   private final JComponent content;

   public Image2AsciiAlgorithmJavEOptionsPanel(AlgorithmJavEOptionsModel optionsModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      GridDialogPanelBuilder gridPanel = new GridDialogPanelBuilder();
      this.greyScaleTablePanel = new GreyScaleTablePanel(
         optionsModel.getGreyScaleTableSelectionModel(), optionsModel.getDisplayFontModel(), greyscaleTableConfiguration
      );
      gridPanel.add(this.greyScaleTablePanel);
      this.bannedCharactersPanel = new BannedCharactersPanel(optionsModel.getBannedCharactersModel(), optionsModel.getDisplayFontModel());
      gridPanel.add(this.bannedCharactersPanel);
      this.content = gridPanel.createPanel();
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void dispose() {
      this.greyScaleTablePanel.dispose();
      this.bannedCharactersPanel.dispose();
   }
}
