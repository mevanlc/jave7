package net.disy.commons.swing.dialog.input;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;

public abstract class AbstractValidatingSmartDialogPanel extends AbstractSmartDialogPanel {
   protected final IMessageProducingValidator validator;

   public AbstractValidatingSmartDialogPanel(IMessageProducingValidator validator) {
      Ensure.ensureArgumentNotNull(validator);
      this.validator = validator;
   }

   @Override
   public IBasicMessage createOptionalCurrentMessage() {
      return this.validator.createOptionalCurrentMessage();
   }
}
