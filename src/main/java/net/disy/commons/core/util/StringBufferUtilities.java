package net.disy.commons.core.util;

import net.disy.commons.core.exception.UnreachableCodeReachedException;

public class StringBufferUtilities {
   private StringBufferUtilities() {
      throw new UnreachableCodeReachedException();
   }

   public static void append(StringBuffer buffer, char character, int count) {
      Ensure.ensureArgumentTrue("Positive number of characters must be appended.", count >= 0);

      for (int index = 0; index < count; index++) {
         buffer.append(character);
      }
   }
}
