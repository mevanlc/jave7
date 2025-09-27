package net.disy.commons.swing.objectfield;

public final class StringFormater extends AbstractObjectFormater<String> {
   public StringFormater() {
      this(new StringValidator());
   }

   public StringFormater(IObjectValidator<String> validator) {
      super(validator);
   }

   public String parse(String text) {
      return text != null && text.trim().length() != 0 ? text : null;
   }

   public String format(String object) {
      return object;
   }
}
