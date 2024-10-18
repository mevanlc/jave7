package net.disy.commons.swing.dialog.input.number;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;

public class ByteModelSmartDialogPanel extends IntegralNumberSmartDialogPanel<Byte> {
   public ByteModelSmartDialogPanel(String label, ObjectModel<Byte> model, IMessageProducingValidator validator) {
      super(label, model, validator, (byte) 0, (byte)-128, (byte)127);
   }

   protected Byte convertToNumber(Long value) {
      return value.byteValue();
   }
}
