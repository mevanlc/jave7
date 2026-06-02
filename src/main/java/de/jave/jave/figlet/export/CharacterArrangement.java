package de.jave.jave.figlet.export;

import net.dizzy.commons.core.util.Ensure;

public class CharacterArrangement {
   public static final CharacterArrangement[] ALL = new CharacterArrangement[]{
      new CharacterArrangement("Minimal", "ABCDEFG\nHIJKLMN\nOPQRSTU\nVWXYZ \n!\"=+-:;"),
      new CharacterArrangement(
         "Complete", "ABCDEFGHIJ\nKLMNOPQRST\nUVWXYZ\n0123456789\nabcdefghij\nklmnopqrst\nuvwxyz\näöüÄÖÜß\n !\"#$%&'\n()*+,-./\n:;<=>?@\n[\\]^_´{|}~"
      )
   };
   private final String name;
   private final String field;

   private CharacterArrangement(String name, String field) {
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(field);
      this.name = name;
      this.field = field;
   }

   public String getName() {
      return this.name;
   }

   public String getField() {
      return this.field;
   }
}
