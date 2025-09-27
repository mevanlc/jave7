package net.disy.commons.swing.layout.grid;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JPanel;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class GridDialogPanelBuilder implements IGridDialogPanelBuilder {
   private final List<IDialogComponent> components = new ArrayList<>();
   private final boolean equalWidthColumns;
   private JPanel panel;
   private final int horizontalSpacing;
   private final int verticalSpacing;

   public GridDialogPanelBuilder() {
      this(false);
   }

   public GridDialogPanelBuilder(boolean equalWidthColumns) {
      this(equalWidthColumns, LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing());
   }

   public GridDialogPanelBuilder(int horizontalSpacing, int verticalSpacing) {
      this(false, horizontalSpacing, verticalSpacing);
   }

   public GridDialogPanelBuilder(boolean equalWidthColumns, int horizontalSpacing, int verticalSpacing) {
      this.equalWidthColumns = equalWidthColumns;
      this.horizontalSpacing = horizontalSpacing;
      this.verticalSpacing = verticalSpacing;
   }

   @Override
   public void add(IDialogComponent component) {
      if (this.panel != null) {
         throw new IllegalStateException("Trying to add a dialog component after content has been created.");
      } else {
         this.components.add(component);
      }
   }

   @Override
   public void addVerticalSpacing(int height) {
      this.add(new GridDialogPanelBuilder.VerticalSpacingDialogComponent(height));
   }

   @Override
   public JPanel createPanel() {
      if (this.panel == null) {
         int columnCount = this.getMaximumColumnCount();
         if (columnCount == 0) {
            columnCount = 1;
         }

         GridDialogLayout gridDialogLayout = new GridDialogLayout(columnCount, this.equalWidthColumns);
         gridDialogLayout.setHorizontalSpacing(this.horizontalSpacing);
         gridDialogLayout.setVerticalSpacing(this.verticalSpacing);
         this.panel = new JPanel(gridDialogLayout);

         for (IDialogComponent component : this.components) {
            component.fillInto(this.panel, columnCount);
            this.panel.add(new EndOfLineMarkerComponent());
         }
      }

      return this.panel;
   }

   private int getMaximumColumnCount() {
      int maxColumnCount = 0;

      for (IDialogComponent component : this.components) {
         if (component.getColumnCount() > maxColumnCount) {
            maxColumnCount = component.getColumnCount();
         }
      }

      return maxColumnCount;
   }

   private static final class VerticalSpacingDialogComponent implements IDialogComponent {
      private final int height;

      private VerticalSpacingDialogComponent(int height) {
         this.height = height;
      }

      @Override
      public int getColumnCount() {
         return 1;
      }

      @Override
      public void fillInto(JPanel panel, int columnCount) {
         panel.add(Box.createVerticalStrut(this.height));
      }
   }
}
