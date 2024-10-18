package net.disy.commons.core.errorhandling;

import java.io.PrintStream;
import java.text.MessageFormat;
import net.disy.commons.core.util.Ensure;

public class Warning implements IProblem {
   private final String message;

   public Warning(String message) {
      Ensure.ensureArgumentNotNull(message);
      this.message = message;
   }

   @Override
   public void print(PrintStream errorStream) {
      errorStream.println(MessageFormat.format("WARNING: {0}", this.message));
   }
}
