package net.disy.commons.core.errorhandling;

import java.io.PrintStream;
import java.text.MessageFormat;

public class ListItem {
   private final String message;

   public ListItem(String message) {
      this.message = message;
   }

   public void print(PrintStream errorStream) {
      errorStream.println(MessageFormat.format("- {0}", this.message));
   }
}
