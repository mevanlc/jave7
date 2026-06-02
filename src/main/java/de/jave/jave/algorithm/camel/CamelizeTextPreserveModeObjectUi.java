package de.jave.jave.algorithm.camel;

import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class CamelizeTextPreserveModeObjectUi extends AbstractObjectUi<CamelizeTextPreserveMode> {
   public String getLabel(CamelizeTextPreserveMode value) {
      switch (value) {
         case PRESERVE_NONE:
            return "None";
         case PRESERVE_WHITESPACE:
            return "Preserve Whitespace";
         case PRESERVE_WORDS:
            return "Preserve Words";
         default:
            throw new IllegalArgumentException();
      }
   }
}
