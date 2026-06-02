package de.jave.asciimation.export.gif;

import de.jave.asciimation.AnimationOutputOptionsConfiguration;
import de.jave.asciimation.export.AdditionalAnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizardModel;
import de.jave.asciimation.export.DefaultAnimationOutputOptionsPage;
import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.jave.export.Ascii2ImageOptions;
import de.jave.jave.export.GifExportOptionsPanel;
import de.jave.jave.preferences.ColorScheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class GifAnimationsOutputOptionsPage extends DefaultAnimationOutputOptionsPage {
   public GifAnimationsOutputOptionsPage(AnimationExportWizardModel model, boolean isLoopAvailable) {
      super(model, new AnimationOutputOptionsConfiguration(new ExtensionFileFilter[]{ExtensionFileFilters.GIF}, isLoopAvailable, false));
   }

   @Override
   protected void addAdditionalOptionsComponents(GridDialogPanelBuilder dialogPanel) {
      final GifExportOptionsPanel gifOptionsPanel = new GifExportOptionsPanel(this.getModel().getDisplayFont(), true, ColorScheme.BLACK_ON_WHITE);
      dialogPanel.add(new IDialogComponent() {
         @Override
         public void fillInto(JPanel panel, int columnCount) {
            GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
            data.setHorizontalSpan(columnCount);
            panel.add(gifOptionsPanel.getContent(), data);
         }

         @Override
         public int getColumnCount() {
            return 1;
         }
      });
      gifOptionsPanel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            GifAnimationsOutputOptionsPage.this.saveGifExportOptions(gifOptionsPanel);
         }
      });
      this.saveGifExportOptions(gifOptionsPanel);
   }

   private void saveGifExportOptions(GifExportOptionsPanel gifOptionsPanel) {
      AdditionalAnimationExportOptions options = this.getModel().getExportOptions().getAdditionalOptions();
      Ascii2ImageOptions selectedOptions = gifOptionsPanel.getSelectedOptions();
      options.setGifScale(selectedOptions.getFont().getSize());
      options.setGifFont(selectedOptions.getFont());
      options.setConnectedLinesView(selectedOptions.isConnectedLinesView());
   }
}
