package net.disy.commons.core.number;

import java.text.DecimalFormat;
import net.disy.commons.core.exception.UnreachableCodeReachedException;

public class NumberFormatUtilities {
   private NumberFormatUtilities() {
      throw new UnreachableCodeReachedException();
   }

   public static boolean isLegalDecimalFormatPattern(String pattern) {
      try {
         new DecimalFormat(pattern);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }
}
