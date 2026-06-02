package net.dizzy.commons.swing.textfield;

import javax.swing.JComponent;

import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.swing.component.IComponentContainer;

public class DoubleModelTextField implements IComponentContainer {
   private final DoubleField field;

   public DoubleModelTextField(int columns, ObjectModel<Double> model) {
      field = new DoubleField(columns);
      if (model.getValue() != null) {
         field.setValue(model.getValue().doubleValue());
      }
      field.addDocumentListener(new net.dizzy.commons.swing.events.AbstractDocumentChangeListener() {
         @Override protected void documentChanged() { model.setValue(Double.valueOf(field.getValue())); }
      });
   }

   @Override public JComponent getContent() { return field.getContent(); }
}
