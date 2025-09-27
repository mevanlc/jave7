package net.disy.commons.swing.dialog.input.number;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.textfield.DoubleModelTextField;

public class DoubleModelSmartDialogPanel extends AbstractLabeledSmartDialogPanel {
   private final ObjectModel<Double> model;
   private final DoubleModelTextField doubleField;

   public DoubleModelSmartDialogPanel(String label, ObjectModel<Double> model, IMessageProducingValidator validator) {
      super(label, validator);
      this.model = model;
      this.doubleField = new DoubleModelTextField(12, model);
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.model.addChangeListener(listener);
   }

   @Override
   protected int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   protected JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      panel.add(this.doubleField.getContent());
      return this.doubleField.getContent();
   }

   @Override
   public void requestFocus() {
      this.doubleField.requestFocus();
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.doubleField.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.doubleField.getContent()};
   }
}
