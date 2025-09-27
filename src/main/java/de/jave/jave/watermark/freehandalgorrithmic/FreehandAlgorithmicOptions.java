package de.jave.jave.tool.freehandalgorrithmic;

import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
import net.disy.commons.core.util.Ensure;

public class FreehandAlgorithmicOptions {
   private FreehandAlgorithmicMode mode = FreehandAlgorithmicMode.LINES_ROUNDED;

   public void setMode(FreehandAlgorithmicMode mode) {
      Ensure.ensureArgumentNotNull(mode);
      this.mode = mode;
   }

   public FreehandAlgorithmicMode getMode() {
      return this.mode;
   }
}
