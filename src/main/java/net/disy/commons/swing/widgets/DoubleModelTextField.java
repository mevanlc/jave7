package net.disy.commons.swing.textfield;

import javax.swing.JComponent;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;

public class DoubleModelTextField implements IComponentContainer {
   private final ObjectModel<Double> model;
   private final DoubleField doubleField;

   public DoubleModelTextField(int columnCount, final ObjectModel<Double> model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.doubleField = new DoubleField(columnCount);
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DoubleModelTextField.this.updateDoubleField();
         }
      });
      this.updateDoubleField();
      this.doubleField.addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            model.setValue(DoubleModelTextField.this.doubleField.getDoubleValue());
         }
      });
   }

   private void updateDoubleField() {
      Double value = this.model.getValue();
      this.doubleField.setValue(value);
   }

   @Override
   public JComponent getContent() {
      return this.doubleField.getContent();
   }

   public void requestFocus() {
      this.doubleField.requestFocus();
   }

   public void setEnabled(boolean enabled) {
      this.doubleField.setEnabled(enabled);
   }
}
