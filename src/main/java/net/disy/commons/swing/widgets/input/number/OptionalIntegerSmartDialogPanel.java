package net.disy.commons.swing.dialog.input.number;

import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.optional.OptionalSmartDialogPanel;

public class OptionalIntegerSmartDialogPanel extends OptionalSmartDialogPanel<Integer> {
   public OptionalIntegerSmartDialogPanel(
      ObjectModel<Integer> model,
      IMessageProducingValidator messageProducingValidator,
      String checkBoxLabel,
      String fieldLabel,
      int nullValue,
      int minValue,
      int maxValue
   ) {
      this(model, messageProducingValidator, checkBoxLabel, fieldLabel, nullValue, minValue, maxValue, new ObjectModel<>(nullValue));
   }

   private OptionalIntegerSmartDialogPanel(
      ObjectModel<Integer> model,
      IMessageProducingValidator messageProducingValidator,
      String checkBoxLabel,
      String fieldLabel,
      int nullValue,
      int minValue,
      int maxValue,
      ObjectModel<Integer> fieldModel
   ) {
      super(
         model,
         checkBoxLabel,
         new IntegerModelSmartDialogPanel(fieldLabel, fieldModel, messageProducingValidator, nullValue, minValue, maxValue),
         fieldModel,
         false
      );
   }
}
