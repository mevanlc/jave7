package net.disy.commons.core.number;

import net.disy.commons.core.util.Ensure;

public class MaxDoubleValueDataBuilder<T> {
   private final IDoubleValueGetter<T> valueGetter;
   private T maxData;

   public MaxDoubleValueDataBuilder(IDoubleValueGetter<T> valueGetter) {
      Ensure.ensureArgumentNotNull(valueGetter);
      this.valueGetter = valueGetter;
   }

   public T getMaxData() {
      return this.maxData;
   }

   public double getMaxValue() {
      return this.maxData == null ? Double.NaN : this.valueGetter.getValue(this.maxData);
   }

   public void addData(T data) {
      if (this.maxData == null || this.valueGetter.getValue(data) > this.valueGetter.getValue(this.maxData)) {
         this.maxData = data;
      }
   }
}
