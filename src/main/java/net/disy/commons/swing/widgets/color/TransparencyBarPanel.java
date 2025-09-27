package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class TransparencyBarPanel implements IDialogComponent {
   private final JSlider alphaSlider;
   private final SpinnerNumberModel spinnerModel;
   private final boolean showLabel;

   public TransparencyBarPanel(ColorModel colorModel) {
      this(colorModel, true);
   }

   public TransparencyBarPanel(final ColorModel colorModel, boolean showLabel) {
      this.showLabel = showLabel;
      this.alphaSlider = new JSlider();
      this.alphaSlider.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent changeEvent) {
            int percentage = TransparencyBarPanel.this.alphaSlider.getValue();
            TransparencyBarPanel.this.updateModel(colorModel, percentage);
         }
      });
      this.spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
      this.spinnerModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent changeEvent) {
            TransparencyBarPanel.this.updateModel(colorModel, TransparencyBarPanel.this.spinnerModel.getNumber().intValue());
         }
      });
      colorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            TransparencyBarPanel.this.updateView(colorModel);
         }
      });
      this.updateView(colorModel);
   }

   private void updateModel(ColorModel colorModel, int percentage) {
      int alpha = 255 - (int)((double)percentage * 2.55);
      Color color = colorModel.getColor();
      Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
      colorModel.setColor(alphaColor);
   }

   private void updateView(ColorModel colorModel) {
      int alpha = colorModel.getColor().getAlpha();
      int percentage = 100 - (int)Math.round((double)alpha / 2.55);
      this.alphaSlider.setValue(percentage);
      this.spinnerModel.setValue(percentage);
   }

   @Override
   public int getColumnCount() {
      return this.showLabel ? 4 : 3;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      if (this.showLabel) {
         panel.add(new JLabel(DisyCommonsSwingMessages.getString("TransparencyBarPanel.Transparency")), GridDialogLayoutData.RIGHT);
      }

      panel.add(this.alphaSlider, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new JSpinner(this.spinnerModel));
      panel.add(new JLabel("%"));
   }
}
