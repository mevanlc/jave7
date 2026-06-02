package de.jave.figlet.swing.action;

import java.util.HashSet;
import java.util.Set;
import net.dizzy.commons.core.util.ArrayUtilities;
import net.dizzy.commons.core.util.Ensure;

public class CharacterRanking {
   private final char[] characters;

   private CharacterRanking(char[] characters) {
      Ensure.ensureArgumentNotNull(characters);
      this.characters = characters;
   }

   public double rank(FigLetFontSample sample) {
      int containedCharacters = 0;

      for (int i = 0; i < this.characters.length; i++) {
         if (sample.contains(this.characters[i])) {
            containedCharacters++;
         }
      }

      return (double)containedCharacters / (double)this.characters.length;
   }

   public static CharacterRanking compile(String searchString) {
      char[] characterArray = getContainedCharacters(searchString);
      return new CharacterRanking(characterArray);
   }

   public static char[] getContainedCharacters(String searchString) {
      Set<Character> set = new HashSet<>();

      for (int i = 0; i < searchString.length(); i++) {
         char character = searchString.charAt(i);
         if (character != '\n') {
            set.add(character);
         }
      }

      Character[] array = set.toArray(new Character[0]);
      return ArrayUtilities.toPrimitive(array);
   }
}
