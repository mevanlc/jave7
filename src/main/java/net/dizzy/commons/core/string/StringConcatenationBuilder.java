package net.dizzy.commons.core.string;

import java.util.StringJoiner;

public class StringConcatenationBuilder {
   private final StringJoiner joiner;

   public StringConcatenationBuilder(String separator) {
      joiner = new StringJoiner(separator);
   }

   public void append(String value) {
      joiner.add(value);
   }

   public String getString() {
      return joiner.toString();
   }
}
