package net.disy.commons.core.string;

public class QuotingStringConverter implements IStringConverter {
   @Override
   public String convert(String text) {
      return '"' + text + '"';
   }
}
