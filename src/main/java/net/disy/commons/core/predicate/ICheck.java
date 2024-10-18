package net.disy.commons.core.predicate;

public interface ICheck {
   ICheck PASS_ALWAYS = new PassAlwaysCheck();

   boolean isConfirmed();
}
