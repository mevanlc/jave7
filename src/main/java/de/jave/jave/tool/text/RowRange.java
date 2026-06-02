package de.jave.jave.tool.text;

import net.dizzy.commons.core.util.Ensure;

public class RowRange {
   private final int rowEndIndex;
   private final int rowStartIndex;

   public RowRange(int rowStartIndex, int rowEndIndex) {
      Ensure.ensureTrue("startIndex must not be greater than endIndex", rowStartIndex <= rowEndIndex);
      this.rowStartIndex = rowStartIndex;
      this.rowEndIndex = rowEndIndex;
   }

   public int getRowStartIndex() {
      return this.rowStartIndex;
   }

   public int getRowEndIndex() {
      return this.rowEndIndex;
   }

   public int getRowCount() {
      return this.rowEndIndex - this.rowStartIndex + 1;
   }
}
