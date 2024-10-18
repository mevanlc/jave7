package net.disy.commons.swing.objectfield;

public abstract class AbstractObjectFormater<T> implements IObjectFormater<T> {
   private final IObjectValidator<String> validator;

   public AbstractObjectFormater(IObjectValidator<String> validator) {
      this.validator = validator;
   }

   @Override
   public boolean isValid(String text) {
      return this.validator.isValid(text);
   }
}
