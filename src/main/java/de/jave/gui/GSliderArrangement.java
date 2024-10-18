package de.jave.gui;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public class GSliderArrangement implements IDialogComponent {
   private final GSlider slider;
   private final JLabel label;
   private final JSpinner spinner;
   private final int minValue;
   private final int maxValue;
   private final int defaultValue;
   private final int divide;
   private boolean isResetButtonAvailable = false;
   private final SpinnerNumberModel model;

   public GSliderArrangement(String labelText, int minValue, int maxValue, int defaultValue, int step, final int divide) {
      this.minValue = minValue;
      this.maxValue = maxValue;
      this.defaultValue = defaultValue;
      this.divide = divide;
      this.label = new JLabel(labelText);
      if (divide == 1) {
         this.model = new SpinnerNumberModel(defaultValue, minValue, maxValue, step);
      } else {
         this.model = new SpinnerNumberModel(
            (double)defaultValue / (double)divide, (double)minValue / (double)divide, (double)maxValue / (double)divide, (double)step / (double)divide
         );
      }

      final SpinnerNumberModel sliderModel = new SpinnerNumberModel(defaultValue, minValue, maxValue, step);
      sliderModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            if (divide == 1) {
               GSliderArrangement.this.model.setValue(sliderModel.getValue());
            } else {
               GSliderArrangement.this.model.setValue(sliderModel.getNumber().doubleValue() / (double) divide);
            }
         }
      });
      this.model.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            if (divide == 1) {
               sliderModel.setValue(GSliderArrangement.this.model.getValue());
            } else {
               sliderModel.setValue((int) (GSliderArrangement.this.model.getNumber().doubleValue() * (double) divide));
            }
         }
      });
      this.slider = new GSlider(sliderModel);
      this.spinner = new JSpinner(this.model);
   }

   public void setDefault() {
      this.reset();
   }

   public void reset() {
      this.setValue(this.defaultValue);
   }

   public void setEnabled(boolean enabled) {
      this.spinner.setEnabled(enabled);
      this.slider.setEnabled(enabled);
      this.label.setEnabled(enabled);
   }

   public int getValue() {
      return this.model.getNumber().intValue();
   }

   public double getDValue() {
      return this.model.getNumber().doubleValue();
   }

   public void setValue(int i) {
      if (this.divide == 1) {
         this.model.setValue(i);
      } else {
         this.model.setValue((double) i / (double) this.divide);
      }
   }

   public int getOrientation() {
      return 0;
   }

   public int getMinimum() {
      return this.minValue;
   }

   public int getMaximum() {
      return this.maxValue;
   }

   @Override
   public int getColumnCount() {
      return 3 + (this.isResetButtonAvailable ? 1 : 0);
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(this.label, GridDialogLayoutData.RIGHT);
      panel.add(this.slider, GridDialogLayoutData.FILL_HORIZONTAL);
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalAlignment(GridAlignment.FILL);
      data.setWidthHint(55);
      panel.add(this.spinner, data);
      if (this.isResetButtonAvailable) {
         panel.add(new JButton(new SmartAction("Reset") {
            @Override
            protected void execute(Component parentComponent) {
               GSliderArrangement.this.setDefault();
            }
         }));
      }
   }

   public Component createPanel() {
      GridDialogPanelBuilder panel = new GridDialogPanelBuilder();
      panel.add(this);
      return panel.createPanel();
   }

   public void setShowsResetButton(boolean isResetButtonAvailable) {
      this.isResetButtonAvailable = isResetButtonAvailable;
   }

   public SpinnerNumberModel getModel() {
      return this.model;
   }
}
