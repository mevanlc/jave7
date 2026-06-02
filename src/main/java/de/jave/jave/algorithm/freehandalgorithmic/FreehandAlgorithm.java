package de.jave.jave.algorithm.freehandalgorithmic;

import de.jave.jave.algorithm.GeneralAlgorithm;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterMode;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.lib.LocatedCharacterPlate;
import net.dizzy.commons.core.util.Ensure;

public class FreehandAlgorithm {
   private final Filter filter;

   public FreehandAlgorithm(Filter filter) {
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   public LocatedCharacterPlate convertMarksToFreehandLine(PixelPlate plate, FreehandAlgorithmicMode mode) {
      LocatedCharacterPlate result = plate.convert();
      if (mode == FreehandAlgorithmicMode.CHARACTERS) {
         return result;
      } else {
         GeneralAlgorithm.replace(result, '\u0000', ' ');
         if (mode == FreehandAlgorithmicMode.LINES_ROUNDED) {
            this.filter.filter(result, FilterMode.SOFT);
         } else if (mode == FreehandAlgorithmicMode.LINES_MIDDLE) {
            this.filter.filter(result, FilterMode.MID);
         }

         GeneralAlgorithm.replace(result, ' ', '\u0000');
         return result;
      }
   }
}
