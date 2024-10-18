package net.disy.commons.swing.widgets.internal;

import net.disy.commons.core.util.Ensure;

public class TextBlock {
   public final String text;
   public final TextBlockDelimiter delimiter;

   public TextBlock(String text, TextBlockDelimiter delimiter) {
      Ensure.ensureArgumentNotNull(text);
      Ensure.ensureArgumentNotNull(delimiter);
      this.text = text;
      this.delimiter = delimiter;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof TextBlock)) {
         return false;
      } else {
         TextBlock other = (TextBlock)obj;
         return this.delimiter == other.delimiter && this.text.equals(other.text);
      }
   }

   @Override
   public int hashCode() {
      return this.delimiter.hashCode() * 17 + this.text.hashCode() * 3;
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + "{" + "text='" + this.text + "', delimiter=" + this.delimiter + "}";
   }
}
