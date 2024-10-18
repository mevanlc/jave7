package de.jave.image2ascii.dialog;

import de.jave.braille.BrailleDisplay;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.IImage2AsciiAlgorithm;
import de.jave.image2ascii.IImage2AsciiAlgorithmItem;
import de.jave.image2ascii.Image2AsciiAlgorithmFactory;
import de.jave.image2ascii.Image2AsciiAlgorithmItemUi;
import de.jave.image2ascii.Image2Texter;
import de.jave.image2ascii.algorithm.Image2AsciiAlgorithmBraille;
import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IDisposableComponentContainer;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;
import net.disy.commons.swing.widgets.HorizontalLine;

public class Image2AsciiConversionOptionsPanel {
   private final IChangeListener algorithmOptionsChangeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         Image2AsciiConversionOptionsPanel.this.optionsModel.fireAlgorithmOptionsChanged();
      }
   };
   private final JComponent content;
   private final Image2AsciiOptionsModel optionsModel;
   private IImage2AsciiAlgorithm currentAlgorithm;
   private IDisposableComponentContainer currentAdjustmentComponent;
   private JPanel algorithmOptionsPanel;

   public Image2AsciiConversionOptionsPanel(
      Image2AsciiOptionsModel optionsModel,
      FontModel displayFontModel,
      CardPanel outputPanel,
      BrailleDisplay brailleDisplay,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.optionsModel = optionsModel;
      this.content = this.createConversionOptionsPanel(
         displayFontModel, outputPanel, brailleDisplay, gradientConfiguration, greyscaleTableConfiguration, filter
      );
   }

   private JPanel createConversionOptionsPanel(
      FontModel displayFontModel,
      final CardPanel outputPanel,
      final BrailleDisplay brailleDisplay,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      IImage2AsciiAlgorithmItem[] algorithms = Image2AsciiAlgorithmFactory.createAlgorithmItems(
         displayFontModel, gradientConfiguration, greyscaleTableConfiguration, filter
      );
      final JComboBox chAlgorithm = new JComboBox<>(algorithms);
      chAlgorithm.setRenderer(new ObjectUiListCellRenderer(new Image2AsciiAlgorithmItemUi()));
      this.currentAlgorithm = algorithms[0].getAlgorithm();
      this.optionsModel.setAlgorithm(this.currentAlgorithm);
      this.currentAlgorithm.getOptionsModel().addChangeListener(this.algorithmOptionsChangeListener);
      chAlgorithm.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            IImage2AsciiAlgorithmItem selectedItem = (IImage2AsciiAlgorithmItem)chAlgorithm.getSelectedItem();
            Image2AsciiConversionOptionsPanel.this.handleAlgorithmChanged(selectedItem, outputPanel, brailleDisplay);
         }
      });
      this.algorithmOptionsPanel = new JPanel(new GridDialogLayout(1, false));
      this.algorithmOptionsPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
      this.currentAdjustmentComponent = algorithms[0].createAdjustmentComponent();
      this.algorithmOptionsPanel.add(this.currentAdjustmentComponent.getContent());
      JPanel algorithmPanel = new JPanel(new GridDialogLayout(2, false));
      algorithmPanel.add(new JLabel("Algorithm:"));
      algorithmPanel.add(chAlgorithm);
      GridDialogLayoutData gridDialogLayoutData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      gridDialogLayoutData.setHorizontalSpan(2);
      algorithmPanel.add(new HorizontalLine(), gridDialogLayoutData);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(algorithmPanel, "North");
      panel.add(this.algorithmOptionsPanel, "Center");
      return panel;
   }

   private void handleAlgorithmChanged(IImage2AsciiAlgorithmItem selectedAlgorithmItem, CardPanel outputPanel, BrailleDisplay brailleDisplay) {
      if (this.currentAlgorithm != null) {
         this.currentAlgorithm.getOptionsModel().removeChangeListener(this.algorithmOptionsChangeListener);
         this.currentAdjustmentComponent.dispose();
      }

      this.optionsModel.setAlgorithm(selectedAlgorithmItem.getAlgorithm());
      this.currentAlgorithm = selectedAlgorithmItem.getAlgorithm();
      this.currentAlgorithm.getOptionsModel().addChangeListener(this.algorithmOptionsChangeListener);
      this.algorithmOptionsPanel.removeAll();
      this.currentAdjustmentComponent = selectedAlgorithmItem.createAdjustmentComponent();
      Ensure.ensureNotNull("Algorithm adjustment component may bot be null", this.currentAdjustmentComponent);
      this.algorithmOptionsPanel.add(this.currentAdjustmentComponent.getContent());
      this.algorithmOptionsPanel.revalidate();
      this.algorithmOptionsPanel.repaint();
      if (this.currentAlgorithm instanceof Image2AsciiAlgorithmBraille) {
         ((Image2AsciiAlgorithmBraille)this.currentAlgorithm).setBrailleDisplay(brailleDisplay);
         outputPanel.setSelectedSubPanel(Image2Texter.CARD_PANEL_KEY_BRAILLE);
      } else {
         outputPanel.setSelectedSubPanel(Image2Texter.CARD_PANEL_KEY_TEXT);
      }
   }

   public JComponent getContent() {
      return this.content;
   }
}
