package net.disy.commons.swing.objectfield;

public class IntegerValidator implements IObjectValidator<String> {
   @Override
   public boolean isValid(String text) {
      if (text != null && text.trim().length() != 0) {
         try {
            Integer.valueOf(text);
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      } else {
         return true;
      }
   }
}
