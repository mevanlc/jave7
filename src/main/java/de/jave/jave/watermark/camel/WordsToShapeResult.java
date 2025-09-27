package de.jave.jave.algorithm.camel;

import de.jave.lib.CharacterPlate;
import net.disy.commons.core.util.Ensure;

public final class WordsToShapeResult {
   private final int difference;
   private final CharacterPlate plate;

   public WordsToShapeResult(CharacterPlate plate, int difference) {
      Ensure.ensureArgumentNotNull(plate);
      this.plate = plate;
      this.difference = difference;
   }

   public CharacterPlate getPlate() {
      return this.plate;
   }

   public int getDifference() {
      return this.difference;
   }
}
