package de.jave.image2ascii.algorithm;

import de.jave.jave.pixelplate.PixelPlateMode;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;

public class AlgorithmPixelPlateOptionsModel extends AbstractChangeableModel {
   private PixelPlateMode mode = PixelPlateMode.THREE_BY_TWO;

   public void setPixelPlateMode(PixelPlateMode mode) {
      Ensure.ensureArgumentNotNull(mode);
      if (this.mode != mode) {
         this.mode = mode;
         this.fireChangeEvent();
      }
   }

   public PixelPlateMode getPixelPlateMode() {
      return this.mode;
   }
}
