package net.disy.commons.swing.dialog.input.object.labeled;

public class LabeledValueTransformer implements ILabeledValueTransformer<Object> {
   public Object transform(ILabeledValue input) {
      return input == null ? null : input.getValue();
   }
}
