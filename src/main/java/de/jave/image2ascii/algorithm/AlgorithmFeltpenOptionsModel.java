package de.jave.image2ascii.algorithm;

import net.disy.commons.core.model.AbstractChangeableModel;

public class AlgorithmFeltpenOptionsModel extends AbstractChangeableModel {
   private boolean medianCut = true;

   public void setMedianCut(boolean medianCut) {
      if (this.medianCut != medianCut) {
         this.medianCut = medianCut;
         this.fireChangeEvent();
      }
   }

   public boolean isMedianCut() {
      return this.medianCut;
   }
}
