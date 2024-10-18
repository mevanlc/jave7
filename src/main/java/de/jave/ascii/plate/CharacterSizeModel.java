package de.jave.ascii.plate;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class CharacterSizeModel extends AbstractChangeableModel {
   private CharacterMetrics characterMetrics = new CharacterMetrics(1, 1, 0);

   public void setCharSize(CharacterMetrics characterSize) {
      Ensure.ensureArgumentNotNull(characterSize);
      if (!this.characterMetrics.equals(characterSize)) {
         this.characterMetrics = characterSize;
         this.fireChangeEvent();
      }
   }

   public CharacterMetrics getCharacterSize() {
      return this.characterMetrics;
   }
}
