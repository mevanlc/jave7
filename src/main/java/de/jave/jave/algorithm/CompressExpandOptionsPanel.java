package de.jave.jave.algorithm;

import de.jave.gui.GSliderArrangement;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;

public class CompressExpandOptionsPanel implements JaveAlgorithmOptionsPanel {
   private final JComponent content;

   public CompressExpandOptionsPanel(final CompressExpandOptions options) {
      int maxWidth = options.getMaxWidth();
      int defaultWidth = options.getDefaultWidth();
      int maxHeight = options.getMaxHeight();
      int defaultHeight = options.getDefaultHeight();
      final GSliderArrangement slaNewWidth = new GSliderArrangement("New width:", 1, maxWidth, defaultWidth, 1, 1);
      slaNewWidth.setShowsResetButton(true);
      slaNewWidth.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int newWidth = slaNewWidth.getValue();
            options.setNewWidth(newWidth);
         }
      });
      final GSliderArrangement slaNewHeight = new GSliderArrangement("New height:", 1, maxHeight, defaultHeight, 1, 1);
      slaNewHeight.setShowsResetButton(true);
      slaNewHeight.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int newHeight = slaNewHeight.getValue();
            options.setNewHeight(newHeight);
         }
      });
      GridDialogPanelBuilder panel = new GridDialogPanelBuilder();
      panel.add(slaNewWidth);
      panel.add(slaNewHeight);
      this.content = panel.createPanel();
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }
}
