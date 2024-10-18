package de.jave.image2ascii.algorithm;

import net.disy.commons.core.model.AbstractChangeableModel;

public class AlgorithmEdgeDetectOptionsModel extends AbstractChangeableModel {
   private boolean hires = true;

   public boolean isHires() {
      return this.hires;
   }

   public void setHires(boolean hires) {
      if (this.hires != hires) {
         this.hires = hires;
         this.fireChangeEvent();
      }
   }
}
