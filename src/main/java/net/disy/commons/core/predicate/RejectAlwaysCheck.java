package net.disy.commons.core.predicate;

public class RejectAlwaysCheck implements ICheck {
   @Override
   public boolean isConfirmed() {
      return false;
   }
}
