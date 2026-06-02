package net.dizzy.commons.core.progress;

public final class NonCancelable implements ICancelable {
   private static final NonCancelable INSTANCE = new NonCancelable();

   private NonCancelable() {
   }

   public static NonCancelable getInstance() {
      return INSTANCE;
   }

   @Override
   public boolean isCanceled() {
      return false;
   }
}
