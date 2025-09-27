package net.disy.commons.swing.dialog.input.object.labeled;

import net.disy.commons.core.util.ObjectUtilities;

public class LabeledValue implements ILabeledValue {
   private final Object value;
   private final String label;

   public LabeledValue(String label, Object value) {
      this.label = label;
      this.value = value;
   }

   @Override
   public String getLabel() {
      return this.label;
   }

   @Override
   public Object getValue() {
      return this.value;
   }

   @Override
   public boolean equals(Object obj) {
      return !(obj instanceof ILabeledValue) ? false : ObjectUtilities.equals(this.value, ((ILabeledValue)obj).getValue());
   }

   @Override
   public int hashCode() {
      return this.value == null ? ILabeledValue.class.hashCode() : this.value.hashCode();
   }
}
