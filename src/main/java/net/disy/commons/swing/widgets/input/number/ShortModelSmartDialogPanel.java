package net.disy.commons.swing.dialog.input.number;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;

public class ShortModelSmartDialogPanel extends IntegralNumberSmartDialogPanel<Short> {
   public ShortModelSmartDialogPanel(String label, ObjectModel<Short> model, IMessageProducingValidator validator) {
      super(label, model, validator, (short) 0, (short)-32768, (short)32767);
   }

   protected Short convertToNumber(Long value) {
      return value.shortValue();
   }
}
