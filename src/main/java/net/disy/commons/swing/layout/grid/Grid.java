package net.disy.commons.swing.layout.grid;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.swing.layout.util.GridCellSize;
import net.disy.commons.swing.layout.util.GridCellSizeList;

public class Grid {
   private final GridCellSizeList rowSizes;
   private final GridCellSizeList columnSizes;
   private final GridCell[] cells;

   public Grid(GridCellSizeList rowSizes, GridCellSizeList columnSizes, GridCell[] cells) {
      this.rowSizes = rowSizes;
      this.columnSizes = columnSizes;
      this.cells = cells;
   }

   public GridCellSize getColumn(int columnIndex) {
      return this.columnSizes.get(columnIndex);
   }

   public GridCellSize getRow(int rowIndex) {
      return this.rowSizes.get(rowIndex);
   }

   public int getRowCount() {
      return this.rowSizes.size();
   }

   public int getCellCount() {
      return this.cells.length;
   }

   public GridCell getCell(int i) {
      return this.cells[i];
   }

   public int getColumnCount() {
      return this.columnSizes.size();
   }

   public GridCellSizeList getColumns() {
      return this.columnSizes;
   }

   public GridCellSizeList getRows() {
      return this.rowSizes;
   }

   public void makeColumnsEqualWidth() {
      int minimumColumnWidth = 0;

      for (int i = 0; i < this.columnSizes.size(); i++) {
         if (this.columnSizes.get(i).getMinimumSize() > minimumColumnWidth) {
            minimumColumnWidth = this.columnSizes.get(i).getMinimumSize();
         }
      }

      for (int ix = 0; ix < this.columnSizes.size(); ix++) {
         this.columnSizes.get(ix).setMinimumSize(minimumColumnWidth);
      }

      int preferredColumnWidth = 0;

      for (int ix = 0; ix < this.columnSizes.size(); ix++) {
         if (this.columnSizes.get(ix).getPreferredSize() > preferredColumnWidth) {
            preferredColumnWidth = this.columnSizes.get(ix).getPreferredSize();
         }
      }

      for (int ixx = 0; ixx < this.columnSizes.size(); ixx++) {
         this.columnSizes.get(ixx).setPreferredSize(preferredColumnWidth);
      }
   }

   public int getTotalMinimumWidth(int horizontalSpacing, Insets insets) {
      int totalWidth = 0;

      for (int i = 0; i < this.getColumnCount(); i++) {
         GridCellSize column = this.getColumn(i);
         totalWidth += column.getMinimumSize();
      }

      if (this.getColumnCount() > 0) {
         totalWidth += (this.getColumnCount() - 1) * horizontalSpacing;
      }

      return totalWidth + insets.left + insets.right;
   }

   public int getTotalPreferredWidth(int horizontalSpacing, Insets insets) {
      int totalWidth = 0;

      for (int i = 0; i < this.getColumnCount(); i++) {
         GridCellSize column = this.getColumn(i);
         totalWidth += column.getPreferredSize();
      }

      if (this.getColumnCount() > 0) {
         totalWidth += (this.getColumnCount() - 1) * horizontalSpacing;
      }

      return totalWidth + insets.left + insets.right;
   }

   public int getTotalWidth(int horizontalSpacing, Insets insets) {
      int totalWidth = 0;

      for (int i = 0; i < this.getColumnCount(); i++) {
         GridCellSize column = this.getColumn(i);
         totalWidth += column.getSize();
      }

      if (this.getColumnCount() > 0) {
         totalWidth += (this.getColumnCount() - 1) * horizontalSpacing;
      }

      return totalWidth + insets.left + insets.right;
   }

   public int getTotalMinimumHeight(int verticalSpacing, Insets insets) {
      int totalHeight = 0;

      for (int i = 0; i < this.getRowCount(); i++) {
         GridCellSize row = this.getRow(i);
         totalHeight += row.getMinimumSize();
      }

      if (this.getRowCount() > 0) {
         totalHeight += (this.getRowCount() - 1) * verticalSpacing;
      }

      return totalHeight + insets.top + insets.bottom;
   }

   public int getTotalPreferredHeight(int verticalSpacing, Insets insets) {
      int totalHeight = 0;

      for (int i = 0; i < this.getRowCount(); i++) {
         GridCellSize row = this.getRow(i);
         totalHeight += row.getPreferredSize();
      }

      if (this.getRowCount() > 0) {
         totalHeight += (this.getRowCount() - 1) * verticalSpacing;
      }

      return totalHeight + insets.top + insets.bottom;
   }

   public int getTotalHeight(int verticalSpacing, Insets insets) {
      int totalHeight = 0;

      for (int i = 0; i < this.getRowCount(); i++) {
         GridCellSize row = this.getRow(i);
         totalHeight += row.getSize();
      }

      if (this.getRowCount() > 0) {
         totalHeight += (this.getRowCount() - 1) * verticalSpacing;
      }

      return totalHeight + insets.top + insets.bottom;
   }

   public GridCellSizeList getGrabbingColumnSizes() {
      return this.getGrabbingColumnSizes(0, this.getColumnCount());
   }

   public GridCellSizeList getGrabbingColumnSizes(int startIndex, int span) {
      List<GridCellSize> grabbingColumns = new ArrayList<>();

      for (int i = startIndex; i < startIndex + span; i++) {
         if (this.getColumn(i).isGrabExcessSpace()) {
            grabbingColumns.add(this.getColumn(i));
         }
      }

      return new GridCellSizeList(grabbingColumns.toArray(new GridCellSize[0]));
   }

   public GridCellSizeList getNonGrabbingColumnSizes() {
      List<GridCellSize> nonGrabbingColumns = new ArrayList<>();

      for (int i = 0; i < this.getColumnCount(); i++) {
         if (!this.getColumn(i).isGrabExcessSpace()) {
            nonGrabbingColumns.add(this.getColumn(i));
         }
      }

      return new GridCellSizeList(nonGrabbingColumns.toArray(new GridCellSize[0]));
   }

   public GridCellSizeList getGrabbingRowSizes() {
      return this.getGrabbingRowSizes(0, this.getRowCount());
   }

   public GridCellSizeList getGrabbingRowSizes(int startIndex, int span) {
      List<GridCellSize> grabbingRows = new ArrayList<>();

      for (int i = startIndex; i < startIndex + span; i++) {
         if (this.getRow(i).isGrabExcessSpace()) {
            grabbingRows.add(this.getRow(i));
         }
      }

      return new GridCellSizeList(grabbingRows.toArray(new GridCellSize[0]));
   }

   public GridCellSizeList getNonGrabbingRowSizes() {
      List<GridCellSize> nonGrabbingRows = new ArrayList<>();

      for (int i = 0; i < this.getRowCount(); i++) {
         if (!this.getRow(i).isGrabExcessSpace()) {
            nonGrabbingRows.add(this.getRow(i));
         }
      }

      return new GridCellSizeList(nonGrabbingRows.toArray(new GridCellSize[0]));
   }
}
