package net.disy.commons.swing.layout.grid;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import net.disy.commons.swing.layout.util.GridCellSizeList;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class GridDialogLayout implements LayoutManager2 {
   private final Map<Component, IGridDialogLayoutData> constraints = new HashMap<>();
   private final int columnCount;
   private int horizontalSpacing = LayoutUtilities.getComponentSpacing();
   private int verticalSpacing = LayoutUtilities.getComponentSpacing();
   private final boolean equalWidthColumns;
   private Grid grid;

   public GridDialogLayout(int columnCount, boolean equalWidthColumns) {
      if (columnCount < 1) {
         throw new IllegalArgumentException("ColumnCount must be >=1, was " + columnCount);
      } else {
         this.columnCount = columnCount;
         this.equalWidthColumns = equalWidthColumns;
      }
   }

   public GridDialogLayout(int columnCount, boolean equalWidthColumns, int horizontalSpacing, int verticalSpacing) {
      this(columnCount, equalWidthColumns);
      this.setHorizontalSpacing(horizontalSpacing);
      this.setVerticalSpacing(verticalSpacing);
   }

   public void setHorizontalSpacing(int horizontalSpacing) {
      this.horizontalSpacing = horizontalSpacing;
   }

   public int getHorizontalSpacing() {
      return this.horizontalSpacing;
   }

   public void setVerticalSpacing(int verticalSpacing) {
      this.verticalSpacing = verticalSpacing;
   }

   public int getVerticalSpacing() {
      return this.verticalSpacing;
   }

   @Override
   public float getLayoutAlignmentX(Container target) {
      return 0.5F;
   }

   @Override
   public float getLayoutAlignmentY(Container target) {
      return 0.5F;
   }

   @Override
   public void invalidateLayout(Container target) {
      this.grid = null;
   }

   @Override
   public void addLayoutComponent(Component comp, Object constraint) {
      Object finalConstraint = constraint;
      if (constraint == null) {
         finalConstraint = new GridDialogLayoutData();
      } else if (!(constraint instanceof IGridDialogLayoutData)) {
         throw new IllegalArgumentException("GridDialogLayout expectes layout constraints to be instance of IGridDialogLayoutData, was " + constraint);
      }

      this.constraints.put(comp, (IGridDialogLayoutData)finalConstraint);
   }

   @Override
   public void removeLayoutComponent(Component comp) {
      this.constraints.remove(comp);
   }

   @Override
   public void layoutContainer(Container parent) {
      synchronized (parent.getTreeLock()) {
         if (this.grid == null) {
            this.grid = this.createGrid(parent);
         }

         Insets insets = parent.getInsets();
         this.adjustColumnWidths(parent.getWidth(), insets);
         this.adjustRowHeights(parent.getHeight(), insets);
         int x = insets.left;

         for (int i = 0; i < this.grid.getColumnCount(); i++) {
            if (i > 0) {
               x += this.horizontalSpacing;
            }

            this.grid.getColumn(i).setStart(x);
            int size = this.grid.getColumn(i).getSize();
            x += size;
         }

         int y = insets.top;

         for (int i = 0; i < this.grid.getRowCount(); i++) {
            if (i > 0) {
               y += this.verticalSpacing;
            }

            this.grid.getRow(i).setStart(y);
            int size = this.grid.getRow(i).getSize();
            y += size;
         }

         for (int i = 0; i < this.grid.getCellCount(); i++) {
            GridCell cell = this.grid.getCell(i);
            int x0 = this.grid.getColumn(cell.getColumnIndex()).getStart();
            int y0 = this.grid.getRow(cell.getRowIndex()).getStart();
            int width = getActualSize(this.grid.getColumns(), cell.getColumnIndex(), cell.getColumnSpan(), this.horizontalSpacing);
            int height = getActualSize(this.grid.getRows(), cell.getRowIndex(), cell.getRowSpan(), this.verticalSpacing);
            this.layoutCell(cell, new Rectangle(x0, y0, width, height));
         }
      }
   }

   private void adjustColumnWidths(int availableWidth, Insets insets) {
      if (this.equalWidthColumns) {
         this.adjustColumnWidthsForEqualWidthColumns(availableWidth, insets);
      } else {
         this.adjustColumnWidthsForNonEqualWidthColumns(availableWidth, insets);
      }
   }

   private void adjustColumnWidthsForNonEqualWidthColumns(int availableWidth, Insets insets) {
      this.grid.getColumns().adjustToPreferredSizes();
      int remainder = availableWidth - this.grid.getTotalPreferredWidth(this.horizontalSpacing, insets);
      if (remainder < 0) {
         GridCellSizeList grabbingColumns = this.grid.getGrabbingColumnSizes();
         if (grabbingColumns.size() > 0) {
            grabbingColumns.increaseSizes(remainder);
            remainder = availableWidth - this.grid.getTotalWidth(this.horizontalSpacing, insets);
            if (remainder < 0 && this.grid.getNonGrabbingColumnSizes().size() > 0) {
               this.grid.getNonGrabbingColumnSizes().increaseSizes(remainder);
            }
         } else {
            this.grid.getColumns().increaseSizes(remainder);
         }
      } else if (remainder > 0) {
         GridCellSizeList grabbingColumns = this.grid.getGrabbingColumnSizes();
         if (grabbingColumns.size() > 0) {
            grabbingColumns.increaseSizes(remainder);
         }
      }
   }

   private void adjustColumnWidthsForEqualWidthColumns(int availableWidth, Insets insets) {
      this.grid.makeColumnsEqualWidth();
      this.grid.getColumns().adjustToPreferredSizes();
      int remainder = availableWidth - this.grid.getTotalPreferredWidth(this.horizontalSpacing, insets);
      if (remainder < 0) {
         this.grid.getColumns().increaseSizes(remainder);
      } else if (remainder > 0) {
         GridCellSizeList grabbingColumns = this.grid.getGrabbingColumnSizes();
         if (grabbingColumns.size() > 0) {
            this.grid.getColumns().increaseSizes(remainder);
         }
      }
   }

   private void adjustRowHeights(int availableHeight, Insets insets) {
      GridCellSizeList rows = this.grid.getRows();
      if (rows.size() != 0) {
         rows.adjustToPreferredSizes();
         int verticalRemainder = availableHeight - this.grid.getTotalPreferredHeight(this.verticalSpacing, insets);
         if (verticalRemainder < 0) {
            GridCellSizeList grabbingRows = this.grid.getGrabbingRowSizes();
            if (grabbingRows.size() > 0) {
               grabbingRows.increaseSizes(verticalRemainder);
               verticalRemainder = availableHeight - this.grid.getTotalHeight(this.verticalSpacing, insets);
               if (verticalRemainder < 0 && this.grid.getNonGrabbingRowSizes().size() > 0) {
                  this.grid.getNonGrabbingRowSizes().increaseSizes(verticalRemainder);
               }
            } else {
               rows.increaseSizes(verticalRemainder);
            }
         } else if (verticalRemainder > 0) {
            GridCellSizeList grabbingRows = this.grid.getGrabbingRowSizes();
            if (grabbingRows.size() > 0) {
               grabbingRows.increaseSizes(verticalRemainder);
            }
         }
      }
   }

   private void layoutCell(GridCell cell, Rectangle rectangle) {
      if (cell.getComponent().isVisible()) {
         Dimension preferredSize = cell.getPreferredComponentSize();
         int preferredHeight = preferredSize.height;
         int preferredWidth = preferredSize.width;
         IGridDialogLayoutData layoutData = cell.getLayoutData();
         if (layoutData.getHorizontalIndent() < rectangle.width) {
            rectangle.x = rectangle.x + layoutData.getHorizontalIndent();
            rectangle.width = rectangle.width - layoutData.getHorizontalIndent();
         }

         int width;
         int x;
         if (layoutData.getHorizontalAlignment() == GridAlignment.BEGINNING) {
            x = rectangle.x;
            width = this.min(preferredWidth, rectangle.width);
         } else if (layoutData.getHorizontalAlignment() == GridAlignment.CENTER) {
            width = this.min(preferredWidth, rectangle.width);
            x = rectangle.x + (rectangle.width - width) / 2;
         } else if (layoutData.getHorizontalAlignment() == GridAlignment.END) {
            width = this.min(preferredWidth, rectangle.width);
            x = rectangle.x + rectangle.width - width;
         } else {
            width = rectangle.width;
            x = rectangle.x;
         }

         int height;
         int y;
         if (layoutData.getVerticalAlignment() == GridAlignment.BEGINNING) {
            y = rectangle.y;
            height = this.min(preferredHeight, rectangle.height);
         } else if (layoutData.getVerticalAlignment() == GridAlignment.CENTER) {
            height = this.min(preferredHeight, rectangle.height);
            y = rectangle.y + (rectangle.height - height) / 2;
         } else if (layoutData.getVerticalAlignment() == GridAlignment.END) {
            height = this.min(preferredHeight, rectangle.height);
            y = rectangle.y + rectangle.height - height;
         } else {
            height = rectangle.height;
            y = rectangle.y;
         }

         cell.getComponent().setBounds(x, y, width, height);
      }
   }

   private int min(int a, int b) {
      return a < b ? a : b;
   }

   private static int getActualSize(GridCellSizeList sizes, int startIndex, int span, int spacing) {
      int size = 0;

      for (int i = startIndex; i < startIndex + span; i++) {
         size += sizes.get(i).getSize();
      }

      return size + (span - 1) * spacing;
   }

   @Override
   public void addLayoutComponent(String name, Component comp) {
      throw new UnsupportedOperationException();
   }

   @Override
   public Dimension minimumLayoutSize(Container parent) {
      return this.getLayoutSize(parent, true);
   }

   @Override
   public Dimension preferredLayoutSize(Container parent) {
      return this.getLayoutSize(parent, false);
   }

   @Override
   public Dimension maximumLayoutSize(Container target) {
      return new Dimension(32767, 32767);
   }

   private Dimension getLayoutSize(Container parent, boolean minimumSize) {
      synchronized (parent.getTreeLock()) {
         if (this.grid == null) {
            this.grid = this.createGrid(parent);
         }

         Insets insets = parent.getInsets();
         int totalWidth = minimumSize
            ? this.grid.getTotalMinimumWidth(this.horizontalSpacing, insets)
            : this.grid.getTotalPreferredWidth(this.horizontalSpacing, insets);
         int totalHeight = minimumSize
            ? this.grid.getTotalMinimumHeight(this.verticalSpacing, insets)
            : this.grid.getTotalPreferredHeight(this.verticalSpacing, insets);
         return new Dimension(totalWidth, totalHeight);
      }
   }

   private Grid createGrid(Container parent) {
      GridBuilder builder = new GridBuilder(this.columnCount);

      for (int i = 0; i < parent.getComponentCount(); i++) {
         Component component = parent.getComponent(i);
         IGridDialogLayoutData layoutData = this.constraints.get(component);
         builder.add(component, layoutData);
      }

      Grid myGrid = builder.createGrid(this.horizontalSpacing, this.verticalSpacing);
      if (this.equalWidthColumns) {
         myGrid.makeColumnsEqualWidth();
      }

      return myGrid;
   }
}
