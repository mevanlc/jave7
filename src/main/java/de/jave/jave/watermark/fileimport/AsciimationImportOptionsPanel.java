package de.jave.jave.actions.fileimport;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class AsciimationImportOptionsPanel {
   private final JComponent content;
   private final JSpinner skipTopSpinner;

   public AsciimationImportOptionsPanel(final AsciimationOptionsModel model) {
      final SpinnerNumberModel skipTopModel = new SpinnerNumberModel(model.getSkipTop(), 0, 200, 1);
      final SpinnerNumberModel frameHeightModel = new SpinnerNumberModel(model.getFrameHeight(), 1, 200, 1);
      final SpinnerNumberModel frameGapModel = new SpinnerNumberModel(model.getFrameGap(), 0, 10, 1);
      final SpinnerNumberModel frameDurationModel = new SpinnerNumberModel(model.getFrameDuration(), 0, 5000, 2);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            skipTopModel.setValue(model.getSkipTop());
            frameHeightModel.setValue(model.getFrameHeight());
            frameGapModel.setValue(model.getFrameGap());
            frameDurationModel.setValue(model.getFrameDuration());
         }
      });
      skipTopModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setSkipTop(skipTopModel.getNumber().intValue());
         }
      });
      frameHeightModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setFrameHeight(frameHeightModel.getNumber().intValue());
         }
      });
      frameGapModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setFrameGap(frameGapModel.getNumber().intValue());
         }
      });
      frameDurationModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            model.setFrameDuration(frameDurationModel.getNumber().intValue());
         }
      });
      JPanel panel1 = new JPanel(new GridDialogLayout(3, false));
      panel1.add(new JLabel("Skip beginning:"), GridDialogLayoutData.RIGHT);
      this.skipTopSpinner = new JSpinner(skipTopModel);
      GridDialogLayoutData spinnerLayoutData = new GridDialogLayoutData();
      spinnerLayoutData.setHorizontalAlignment(GridAlignment.FILL);
      panel1.add(this.skipTopSpinner, spinnerLayoutData);
      panel1.add(new JLabel("lines"));
      panel1.add(new JLabel("Frame height:"), GridDialogLayoutData.RIGHT);
      panel1.add(new JSpinner(frameHeightModel), spinnerLayoutData);
      panel1.add(new JLabel("lines"));
      JPanel panel2 = new JPanel(new GridDialogLayout(3, false));
      panel2.add(new JLabel("Frame gap:"), GridDialogLayoutData.RIGHT);
      panel2.add(new JSpinner(frameGapModel), spinnerLayoutData);
      panel2.add(new JLabel("lines"));
      panel2.add(new JLabel("Frame duration:"), GridDialogLayoutData.RIGHT);
      panel2.add(new JSpinner(frameDurationModel), spinnerLayoutData);
      panel2.add(new JLabel("ms"));
      JPanel panel = new JPanel(new GridDialogLayout(2, true, 10, 0));
      panel.add(panel1);
      panel.add(panel2);
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }

   public void requestFocus() {
      this.skipTopSpinner.requestFocus();
   }
}
