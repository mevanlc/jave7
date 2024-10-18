package net.disy.commons.core.predicate;

public class PassAlwaysCheck implements ICheck {
   @Override
   public boolean isConfirmed() {
      return true;
   }
}
