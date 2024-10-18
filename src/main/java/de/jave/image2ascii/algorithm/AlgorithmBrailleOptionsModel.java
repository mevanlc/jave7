package de.jave.image2ascii.algorithm;

import de.jave.braille.table.BrailleTables;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

import java.util.Objects;

public class AlgorithmBrailleOptionsModel extends AbstractChangeableModel {
   private String brailleTableName = BrailleTables.getDefaultTable().getName();

   public void setBrailleTableName(String brailleTableName) {
      Ensure.ensureArgumentNotNull(brailleTableName);
      if (!Objects.equals(this.brailleTableName, brailleTableName)) {
         this.brailleTableName = brailleTableName;
         this.fireChangeEvent();
      }
   }

   public String getBrailleTableName() {
      return this.brailleTableName;
   }
}
