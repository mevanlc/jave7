package net.disy.commons.core.errorhandling;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Error implements IProblem {
   private final List<ListItem> listItems = new ArrayList<>();
   private final String message;
   private final Throwable throwable;

   public Error(String messagePattern, Object... parameters) {
      this(null, messagePattern, parameters);
   }

   public Error(Throwable throwable, String messagePattern, Object... parameters) {
      this.throwable = throwable;
      this.message = MessageFormat.format(messagePattern, parameters);
   }

   public Error(Throwable throwable, String message) {
      this.throwable = throwable;
      this.message = message;
   }

   @Override
   public void print(PrintStream errorStream) {
      errorStream.println(MessageFormat.format("ERROR: {0}", this.message));
      if (this.throwable != null) {
         this.throwable.printStackTrace(errorStream);
      }

      for (ListItem item : this.listItems) {
         item.print(errorStream);
      }
   }

   public void addListItem(String filename) {
      ListItem item = new ListItem(filename);
      this.listItems.add(item);
   }

   public String getMessage() {
      return this.message;
   }
}
