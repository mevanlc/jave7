package net.disy.commons.swing.ui;

import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public final class ToStringUi<P> extends AbstractObjectUi<P> {
   private static final String NOTHING_CHOSEN_LABEL = DisyCommonsSwingMessages.getString("ToStringUi.DefaultNothingChosenLabel");
   private final String nullValueText;

   public ToStringUi() {
      this(NOTHING_CHOSEN_LABEL);
   }

   public ToStringUi(String nullValueText) {
      this.nullValueText = nullValueText;
   }

   @Override
   public String getLabel(P value) {
      return value == null ? this.nullValueText : value.toString();
   }
}
