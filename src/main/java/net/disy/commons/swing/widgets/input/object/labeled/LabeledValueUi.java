package net.disy.commons.swing.dialog.input.object.labeled;

import net.disy.commons.swing.resources.DisyCommonsSwingMessages;
import net.disy.commons.swing.ui.AbstractObjectUi;

public class LabeledValueUi extends AbstractObjectUi<ILabeledValue> {
   private static final String NOTHING_CHOSEN_LABEL = DisyCommonsSwingMessages.getString("ToStringUi.DefaultNothingChosenLabel");
   private final String nullValueText;

   public LabeledValueUi() {
      this(NOTHING_CHOSEN_LABEL);
   }

   public LabeledValueUi(String nullValueText) {
      this.nullValueText = nullValueText;
   }

   public String getLabel(ILabeledValue value) {
      return value == null ? this.nullValueText : value.getLabel();
   }

   public String getToolTipText(ILabeledValue value) {
      return value == null ? this.nullValueText : value.getLabel();
   }
}
