package net.disy.commons.swing.dialog.input.number;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;

public class LongModelSmartDialogPanel extends IntegralNumberSmartDialogPanel<Long> {
   public LongModelSmartDialogPanel(String label, ObjectModel<Long> model, IMessageProducingValidator validator) {
      super(label, model, validator, 0L, Long.MIN_VALUE, Long.MAX_VALUE);
   }

   protected Long convertToNumber(Long value) {
      return value;
   }
}
