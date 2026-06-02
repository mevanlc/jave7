package de.jave.image2ascii.algorithm;

import net.dizzy.commons.core.model.AbstractChangeableModel;

public class AlgorithmEdgeTracingOptionsModel extends AbstractChangeableModel {
   private boolean fill;
   private boolean smoothing;

   public void setFill(boolean fill) {
      if (this.fill != fill) {
         this.fill = fill;
         this.fireChangeEvent();
      }
   }

   public boolean isFill() {
      return this.fill;
   }

   public void setSmoothing(boolean smoothing) {
      if (this.smoothing != smoothing) {
         this.smoothing = smoothing;
         this.fireChangeEvent();
      }
   }

   public boolean isSmoothing() {
      return this.smoothing;
   }
}
