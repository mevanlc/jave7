package net.disy.commons.swing.layout.grid;

import java.util.ArrayList;
import java.util.List;

public class GridCoverage {
   private List<boolean[]> coverageRows = new ArrayList<>();
   private final int columnCount;

   public GridCoverage(int columnCount) {
      this.columnCount = columnCount;
   }

   public void add(GridCell gridCell) {
      for (int columnIndex = gridCell.getColumnIndex(); columnIndex < gridCell.getColumnIndex() + gridCell.getColumnSpan(); columnIndex++) {
         for (int rowIndex = gridCell.getRowIndex(); rowIndex < gridCell.getRowIndex() + gridCell.getRowSpan(); rowIndex++) {
            this.setCovered(columnIndex, rowIndex);
         }
      }
   }

   private void setCovered(int columnIndex, int rowIndex) {
      this.getRow(rowIndex)[columnIndex] = true;
   }

   private boolean[] getRow(int rowIndex) {
      while (this.coverageRows.size() < rowIndex + 1) {
         this.coverageRows.add(new boolean[this.columnCount]);
      }

      return this.coverageRows.get(rowIndex);
   }

   public boolean isCovered(int columnIndex, int rowIndex) {
      return this.getRow(rowIndex)[columnIndex];
   }
}
