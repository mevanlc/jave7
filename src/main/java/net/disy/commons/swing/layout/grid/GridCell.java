package net.disy.commons.swing.layout.grid;

import java.awt.Component;
import java.awt.Dimension;

public class GridCell {
   private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);
   private final int columnIndex;
   private final int rowIndex;
   private final IGridDialogLayoutData layoutData;
   private final Component component;
   private final Dimension preferredSize;
   private final Dimension preferredComponentSize;
   private final Dimension minimumSize;

   public GridCell(Component component, IGridDialogLayoutData layoutData, int columnIndex, int rowIndex) {
      this.component = component;
      this.layoutData = layoutData;
      this.columnIndex = columnIndex;
      this.rowIndex = rowIndex;
      this.preferredComponentSize = component.getPreferredSize();
      this.minimumSize = this.computeMinimumSize();
      this.preferredSize = this.computePreferredSize();
   }

   private Dimension computeMinimumSize() {
      Dimension minimumComponentSize = this.component.getMinimumSize();
      int width = minimumComponentSize.width + this.layoutData.getHorizontalIndent();
      if (this.layoutData.getWidthHint() > width) {
         width = this.layoutData.getWidthHint();
      }

      int height = minimumComponentSize.height;
      if (this.layoutData.getHeightHint() > height) {
         height = this.layoutData.getHeightHint();
      }

      return new Dimension(width, height);
   }

   private Dimension computePreferredSize() {
      int width = this.preferredComponentSize.width + this.layoutData.getHorizontalIndent();
      if (this.layoutData.getWidthHint() > width) {
         width = this.layoutData.getWidthHint();
      }

      int height = this.preferredComponentSize.height;
      if (this.layoutData.getHeightHint() > height) {
         height = this.layoutData.getHeightHint();
      }

      return new Dimension(width, height);
   }

   public IGridDialogLayoutData getLayoutData() {
      return this.layoutData;
   }

   public boolean covers(int column, int row) {
      return this.columnIndex <= column
         && this.rowIndex <= row
         && this.columnIndex + this.layoutData.getHorizontalSpan() - 1 >= column
         && this.rowIndex + this.layoutData.getVerticalSpan() - 1 >= row;
   }

   public int getRowIndex() {
      return this.rowIndex;
   }

   public int getRowSpan() {
      return this.layoutData.getVerticalSpan();
   }

   public int getColumnIndex() {
      return this.columnIndex;
   }

   public int getColumnSpan() {
      return this.layoutData.getHorizontalSpan();
   }

   public Dimension getMinimumSize() {
      return this.component.isVisible() ? this.minimumSize : EMPTY_DIMENSION;
   }

   public Dimension getPreferredSize() {
      return this.component.isVisible() ? this.preferredSize : EMPTY_DIMENSION;
   }

   public Dimension getPreferredComponentSize() {
      return this.component.isVisible() ? this.preferredComponentSize : EMPTY_DIMENSION;
   }

   public Component getComponent() {
      return this.component;
   }
}
