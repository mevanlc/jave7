package de.jave.jave.algorithm;

import de.jave.gui.GSliderArrangement;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTablePanel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTableSelectionModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class BrightnessOptionsPanel implements JaveAlgorithmOptionsPanel {
   private final JComponent content;

   public BrightnessOptionsPanel(final BrightnessOptions options, final FontModel fontModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      options.setChar('X');
      final GSliderArrangement slaNewBrightness = new GSliderArrangement("Brightness:", -20, 20, 0, 1, 20);
      slaNewBrightness.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            double brighten = slaNewBrightness.getDValue();
            options.setFactor(brighten);
         }
      });
      final JComboBox chAlgo = new JComboBox<>(new String[]{"1 Pixel per character", "4 Pixels per character", "Random delete/set"});
      chAlgo.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            int algorithmIndex = chAlgo.getSelectedIndex();
            options.setAlgorithm(algorithmIndex);
         }
      });
      GridDialogPanelBuilder panel = new GridDialogPanelBuilder();
      panel.add(slaNewBrightness);
      final GreyScaleTableSelectionModel greyScaleTableSelectionModel = new GreyScaleTableSelectionModel(greyscaleTableConfiguration);
      GreyScaleTablePanel greyScaleTablePanel = new GreyScaleTablePanel(greyScaleTableSelectionModel, fontModel, greyscaleTableConfiguration);
      greyScaleTableSelectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            options.setGreyscaleTable(greyScaleTableSelectionModel.getActualGreyscaleTable(fontModel));
         }
      });
      options.setGreyscaleTable(greyScaleTableSelectionModel.getActualGreyscaleTable(fontModel));
      panel.add(greyScaleTablePanel);
      panel.add(new IDialogComponent() {
         @Override
         public int getColumnCount() {
            return 2;
         }

         @Override
         public void fillInto(JPanel panel, int columnCount) {
            panel.add(new JLabel("Algorithm:"), GridDialogLayoutData.RIGHT);
            GridDialogLayoutData data = new GridDialogLayoutData();
            data.setHorizontalSpan(columnCount - 1);
            panel.add(chAlgo, data);
         }
      });
      this.content = panel.createPanel();
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
