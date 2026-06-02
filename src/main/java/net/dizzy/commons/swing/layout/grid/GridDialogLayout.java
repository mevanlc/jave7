package net.dizzy.commons.swing.layout.grid;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;

import net.miginfocom.swing.MigLayout;

public class GridDialogLayout implements LayoutManager2 {
   private final MigLayout delegate;

   public GridDialogLayout(int columns, boolean equalColumns) {
      this(columns, equalColumns, -1, -1);
   }

   public GridDialogLayout(int columns, boolean equalColumns, int horizontalGap, int verticalGap) {
      StringBuilder layout = new StringBuilder("wrap " + columns);
      if (horizontalGap >= 0 || verticalGap >= 0) {
         layout.append(", gap ").append(Math.max(0, horizontalGap)).append("px ").append(Math.max(0, verticalGap)).append("px");
      }
      String columnConstraints = equalColumns ? ("[sg cols]" + repeat("[sg cols]", Math.max(0, columns - 1))) : "";
      delegate = new MigLayout(layout.toString(), columnConstraints, "");
   }

   @Override
   public void addLayoutComponent(Component component, Object constraints) {
      if (constraints instanceof Integer) {
         delegate.addLayoutComponent(component, new GridDialogLayoutData(((Integer) constraints).intValue()).toMigConstraint());
      } else if (constraints instanceof GridDialogLayoutData) {
         delegate.addLayoutComponent(component, ((GridDialogLayoutData) constraints).toMigConstraint());
      } else if (constraints instanceof String) {
         delegate.addLayoutComponent(component, constraints);
      } else {
         delegate.addLayoutComponent(component, "");
      }
   }

   @Override public Dimension maximumLayoutSize(Container target) { return delegate.maximumLayoutSize(target); }
   @Override public float getLayoutAlignmentX(Container target) { return delegate.getLayoutAlignmentX(target); }
   @Override public float getLayoutAlignmentY(Container target) { return delegate.getLayoutAlignmentY(target); }
   @Override public void invalidateLayout(Container target) { delegate.invalidateLayout(target); }
   @Override public void addLayoutComponent(String name, Component component) { delegate.addLayoutComponent(name, component); }
   @Override public void removeLayoutComponent(Component component) { delegate.removeLayoutComponent(component); }
   @Override public Dimension preferredLayoutSize(Container parent) { return delegate.preferredLayoutSize(parent); }
   @Override public Dimension minimumLayoutSize(Container parent) { return delegate.minimumLayoutSize(parent); }
   @Override public void layoutContainer(Container parent) { delegate.layoutContainer(parent); }

   private static String repeat(String value, int count) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < count; i++) {
         builder.append(value);
      }
      return builder.toString();
   }
}
