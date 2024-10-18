package net.disy.commons.core.transformer;

import net.disy.commons.core.util.ITransformer;

public final class ToStringTransformer<T> implements ITransformer<T, String> {
   private final String nullValueText;

   public ToStringTransformer() {
      this(null);
   }

   public ToStringTransformer(String nullValueText) {
      this.nullValueText = nullValueText;
   }

   public String transform(T input) {
      return input == null ? this.nullValueText : input.toString();
   }
}
