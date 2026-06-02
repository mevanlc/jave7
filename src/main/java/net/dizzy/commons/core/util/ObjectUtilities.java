package net.dizzy.commons.core.util;

import java.util.Objects;

public final class ObjectUtilities {
   private ObjectUtilities() {
   }

   public static boolean equals(Object left, Object right) {
      return Objects.equals(left, right);
   }
}
