package net.disy.commons.swing.smarttable.celleditors;

import javax.swing.JFormattedTextField.AbstractFormatter;
import net.disy.commons.swing.text.NullAsEmptyFormatterDecorator;

public enum NullValueStrategy {
   EMPTY {
      @Override
      public String getStringValue() {
         return "";
      }

      @Override
      public AbstractFormatter decorateFormatter(AbstractFormatter formatter) {
         return new NullAsEmptyFormatterDecorator(formatter);
      }
   },
   DISALLOW {
      @Override
      public String getStringValue() {
         throw new NullPointerException("null value not allowed");
      }

      @Override
      public AbstractFormatter decorateFormatter(AbstractFormatter formatter) {
         return formatter;
      }
   };

   private NullValueStrategy() {
   }

   public abstract String getStringValue();

   public abstract AbstractFormatter decorateFormatter(AbstractFormatter var1);
}
