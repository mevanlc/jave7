package net.disy.commons.swing.dialog.input.number;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;

public abstract class IntegralNumberSmartDialogPanel<T extends Number> extends AbstractLabeledSmartDialogPanel {
   private final ObjectModel<T> model;
   private final JSpinner spinner;

   public IntegralNumberSmartDialogPanel(
      String label, final ObjectModel<T> model, IMessageProducingValidator validator, final T nullValue, T minimum, T maximum
   ) {
      super(label, validator);
      this.model = model;
      Long initialValue = this.getNonNullValue(nullValue);
      final SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(
         initialValue, minimum == null ? null : minimum.longValue(), maximum == null ? null : maximum.longValue(), Integer.valueOf(1)
      );
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            spinnerNumberModel.setValue(IntegralNumberSmartDialogPanel.this.getNonNullValue(nullValue));
         }
      });
      spinnerNumberModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent changeEvent) {
            model.setValue(IntegralNumberSmartDialogPanel.this.convertToNumber((Long)spinnerNumberModel.getValue()));
         }
      });
      this.spinner = new JSpinner(spinnerNumberModel);
      JComponent editor = this.spinner.getEditor();
      if (editor instanceof DefaultEditor) {
         DefaultEditor numberEditor = (DefaultEditor)editor;
         numberEditor.getTextField().setColumns(12);
      }
   }

   protected abstract T convertToNumber(Long var1);

   private Long getNonNullValue(T nullValue) {
      return this.model.getValue() == null ? Long.valueOf(nullValue.longValue()) : Long.valueOf(this.model.getValue().longValue());
   }

   @Override
   public final void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   protected final int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   protected final JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      panel.add(this.spinner);
      return this.spinner;
   }

   @Override
   public final void requestFocus() {
      this.spinner.requestFocus();
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.spinner.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.spinner};
   }
}
