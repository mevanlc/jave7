package net.disy.commons.core.string;

import java.util.Collection;
import net.disy.commons.core.util.Ensure;

public class StringConcatenationBuilder implements IStringConcatenationBuilder {
   private final String separator;
   private final StringBuffer stringBuffer = new StringBuffer();
   private boolean appended = false;

   public static String createConcatenatedString(String separator, Collection<String> values) {
      return createConcatenatedString(separator, values.toArray(new String[0]));
   }

   public static String createConcatenatedString(String separator, String[] values) {
      StringConcatenationBuilder builder = new StringConcatenationBuilder(separator);
      builder.append(values);
      return builder.getString();
   }

   public StringConcatenationBuilder() {
      this("");
   }

   public StringConcatenationBuilder(String separator) {
      Ensure.ensureArgumentNotNull(separator);
      this.separator = separator;
   }

   @Override
   public String getString() {
      return this.stringBuffer.toString();
   }

   @Override
   public String toString() {
      return this.getString();
   }

   public void append(int value) {
      this.append(String.valueOf(value));
   }

   public void append(String[] values) {
      for (String value : values) {
         this.append(value);
      }
   }

   @Override
   public void append(String value) {
      if (this.appended) {
         this.stringBuffer.append(this.separator);
      } else {
         this.appended = true;
      }

      this.stringBuffer.append(value);
   }
}
