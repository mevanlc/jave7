package net.disy.commons.swing.dialog.input.number;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;

public class IntegerModelSmartDialogPanel extends IntegralNumberSmartDialogPanel<Integer> {
   public IntegerModelSmartDialogPanel(String label, ObjectModel<Integer> model, IMessageProducingValidator validator) {
      this(label, model, validator, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   public IntegerModelSmartDialogPanel(
      String label, ObjectModel<Integer> model, IMessageProducingValidator validator, int nullValue, int minValue, int maxValue
   ) {
      super(label, model, validator, nullValue, minValue, maxValue);
   }

   protected Integer convertToNumber(Long value) {
      return value.intValue();
   }
}
