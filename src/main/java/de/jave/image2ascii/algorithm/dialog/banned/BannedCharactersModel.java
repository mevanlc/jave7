package de.jave.image2ascii.algorithm.dialog.banned;

import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;

public class BannedCharactersModel extends AbstractChangeableModel {
   public static final String DEFAUL_BANNED_CHARACTERS = "<>_()[]{}/~-";
   private String bannedCharacters = "<>_()[]{}/~-";

   public void setBannedCharacters(String bannedCharacters) {
      Ensure.ensureArgumentNotNull(bannedCharacters);
      if (!bannedCharacters.equals(this.bannedCharacters)) {
         this.bannedCharacters = bannedCharacters;
         this.fireChangeEvent();
      }
   }

   public String getBannedCharacters() {
      return this.bannedCharacters;
   }
}
