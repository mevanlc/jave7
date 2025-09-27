package net.disy.commons.swing.objectfield;

public final class IntegerFormater extends AbstractObjectFormater<Integer> {
   public IntegerFormater() {
      this(new IntegerValidator());
   }

   public IntegerFormater(IObjectValidator<String> validator) {
      super(validator);
   }

   public Integer parse(String text) {
      return text != null && text.trim().length() != 0 ? Integer.valueOf(text) : null;
   }

   public String format(Integer object) {
      return object.toString();
   }
}
