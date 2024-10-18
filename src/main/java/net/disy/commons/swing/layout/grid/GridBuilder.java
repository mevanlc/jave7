package net.disy.commons.swing.layout.grid;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.swing.layout.util.GridCellSizeList;

public class GridBuilder {
   private int columnIndex = 0;
   private int rowIndex = 0;
   private final GridCoverage gridCoverage;
   private final List<GridCell> gridCells = new ArrayList<>();
   private final int columnCount;

   public GridBuilder(int columnCount) {
      if (columnCount < 1) {
         throw new IllegalArgumentException("ColumnCount must be >=1, was " + columnCount);
      } else {
         this.columnCount = columnCount;
         this.gridCoverage = new GridCoverage(columnCount);
      }
   }

   public void add(Component component, IGridDialogLayoutData layoutData) {
      if (component instanceof EndOfLineMarkerComponent) {
         if (this.columnIndex != 0) {
            this.columnIndex = 0;
            this.rowIndex++;
         }
      } else {
         while (this.gridCoverage.isCovered(this.columnIndex, this.rowIndex)) {
            this.gotoNextCell(1);
         }

         if (this.columnIndex + layoutData.getHorizontalSpan() > this.columnCount) {
            throw new IllegalArgumentException(
               "Illegal horizontal span "
                  + layoutData.getHorizontalSpan()
                  + " in GridLayoutData for component at column "
                  + this.columnIndex
                  + " in "
                  + this.columnCount
                  + " columns GridDialogLayout."
            );
         } else {
            GridCell gridCell = new GridCell(component, layoutData, this.columnIndex, this.rowIndex);
            this.gridCells.add(gridCell);
            this.gridCoverage.add(gridCell);
            this.gotoNextCell(layoutData.getHorizontalSpan());
         }
      }
   }

   private void gotoNextCell(int count) {
      this.columnIndex += count;
      if (this.columnIndex >= this.columnCount) {
         this.columnIndex = 0;
         this.rowIndex++;
      }
   }

   public Grid createGrid(int horizontalSpacing, int verticalSpacing) {
      int rowCount = 0;

      for (int i = 0; i < this.gridCells.size(); i++) {
         GridCell cell = this.gridCells.get(i);
         int maxRowIndex = cell.getRowIndex() + cell.getRowSpan() - 1;
         if (maxRowIndex + 1 > rowCount) {
            rowCount = maxRowIndex + 1;
         }
      }

      GridCellSizeList rowSizes = new GridCellSizeList(rowCount);
      GridCellSizeList columnSizes = new GridCellSizeList(this.columnCount);
      Grid grid = new Grid(rowSizes, columnSizes, this.gridCells.toArray(new GridCell[0]));
      this.initializeExcessFlags(rowSizes, columnSizes);

      for (int ix = 0; ix < this.gridCells.size(); ix++) {
         GridCell cell = this.gridCells.get(ix);
         if (cell.getColumnSpan() == 1) {
            int minimumWidth = cell.getMinimumSize().width;
            columnSizes.get(cell.getColumnIndex()).guaranteeMinimumSize(minimumWidth);
            int preferredWidth = cell.getPreferredSize().width;
            columnSizes.get(cell.getColumnIndex()).guaranteePreferredSize(preferredWidth);
         }

         if (cell.getRowSpan() == 1) {
            int minimumHeight = cell.getMinimumSize().height;
            rowSizes.get(cell.getRowIndex()).guaranteeMinimumSize(minimumHeight);
            int preferredHeight = cell.getPreferredSize().height;
            rowSizes.get(cell.getRowIndex()).guaranteePreferredSize(preferredHeight);
         }
      }

      for (int ix = 0; ix < this.gridCells.size(); ix++) {
         GridCell cellx = this.gridCells.get(ix);
         if (cellx.getColumnSpan() > 1) {
            int availableMinimumWidth = columnSizes.getAvailableMinimumSize(cellx.getColumnIndex(), cellx.getColumnSpan(), horizontalSpacing);
            if (cellx.getMinimumSize().width > availableMinimumWidth) {
               GridCellSizeList grabbingColumnSizes = grid.getGrabbingColumnSizes(cellx.getColumnIndex(), cellx.getColumnSpan());
               if (grabbingColumnSizes.size() > 0) {
                  grabbingColumnSizes.increaseMinimumSizes(0, grabbingColumnSizes.size(), cellx.getMinimumSize().width - availableMinimumWidth);
               } else {
                  columnSizes.increaseMinimumSizes(cellx.getColumnIndex(), cellx.getColumnSpan(), cellx.getMinimumSize().width - availableMinimumWidth);
               }
            }

            int availablePreferredWidth = columnSizes.getAvailablePreferredSize(cellx.getColumnIndex(), cellx.getColumnSpan(), horizontalSpacing);
            if (cellx.getPreferredSize().width > availablePreferredWidth) {
               GridCellSizeList grabbingColumnSizes = grid.getGrabbingColumnSizes(cellx.getColumnIndex(), cellx.getColumnSpan());
               if (grabbingColumnSizes.size() > 0) {
                  grabbingColumnSizes.increasePreferredSizes(0, grabbingColumnSizes.size(), cellx.getPreferredSize().width - availablePreferredWidth);
               } else {
                  columnSizes.increasePreferredSizes(cellx.getColumnIndex(), cellx.getColumnSpan(), cellx.getPreferredSize().width - availablePreferredWidth);
               }
            }
         }

         if (cellx.getRowSpan() > 1) {
            int availableMinimumHeight = rowSizes.getAvailableMinimumSize(cellx.getRowIndex(), cellx.getRowSpan(), verticalSpacing);
            if (cellx.getMinimumSize().height > availableMinimumHeight) {
               GridCellSizeList grabbingRowSizes = grid.getGrabbingRowSizes(cellx.getRowIndex(), cellx.getRowSpan());
               if (grabbingRowSizes.size() > 0) {
                  grabbingRowSizes.increaseMinimumSizes(0, grabbingRowSizes.size(), cellx.getMinimumSize().height - availableMinimumHeight);
               } else {
                  rowSizes.increaseMinimumSizes(cellx.getRowIndex(), cellx.getRowSpan(), cellx.getMinimumSize().height - availableMinimumHeight);
               }
            }

            int availablePreferredHeight = rowSizes.getAvailablePreferredSize(cellx.getRowIndex(), cellx.getRowSpan(), verticalSpacing);
            if (cellx.getPreferredSize().height > availablePreferredHeight) {
               GridCellSizeList grabbingRowSizes = grid.getGrabbingRowSizes(cellx.getRowIndex(), cellx.getRowSpan());
               if (grabbingRowSizes.size() > 0) {
                  grabbingRowSizes.increasePreferredSizes(0, grabbingRowSizes.size(), cellx.getPreferredSize().height - availablePreferredHeight);
               } else {
                  rowSizes.increasePreferredSizes(cellx.getRowIndex(), cellx.getRowSpan(), cellx.getPreferredSize().height - availablePreferredHeight);
               }
            }
         }
      }

      return grid;
   }

   private void initializeExcessFlags(GridCellSizeList rowSizes, GridCellSizeList columnSizes) {
      for (int i = 0; i < this.gridCells.size(); i++) {
         GridCell cell = this.gridCells.get(i);
         if (cell.getLayoutData().isGrabExcessHorizontalSpace()) {
            columnSizes.get(cell.getColumnIndex() + cell.getColumnSpan() - 1).setGrabExcessSpace(true);
         }

         if (cell.getLayoutData().isGrabExcessVerticalSpace()) {
            rowSizes.get(cell.getRowIndex() + cell.getRowSpan() - 1).setGrabExcessSpace(true);
         }
      }
   }
}
