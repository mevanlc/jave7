package net.disy.commons.swing.dialog.animation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;

public class AnimatedCompositeLayout implements LayoutManager2 {
   private final JComponent overlaidComponent;
   private final JComponent baseComponent;

   public AnimatedCompositeLayout(JComponent baseComponent, JComponent overlaidComponent) {
      Ensure.ensureArgumentNotNull(baseComponent);
      Ensure.ensureArgumentNotNull(overlaidComponent);
      this.baseComponent = baseComponent;
      this.overlaidComponent = overlaidComponent;
   }

   @Override
   public void addLayoutComponent(String name, Component comp) {
      if (comp != this.baseComponent && comp != this.overlaidComponent) {
         throw new IllegalArgumentException("No other components may be added to an animated composite panel, tried to add " + comp);
      }
   }

   @Override
   public void addLayoutComponent(Component comp, Object constraints) {
      if (comp != this.baseComponent && comp != this.overlaidComponent) {
         throw new IllegalArgumentException("No other components may be added to an animated composite panel, tried to add " + comp);
      }
   }

   @Override
   public void layoutContainer(Container parent) {
      synchronized (parent.getTreeLock()) {
         AnimatedCompositeComponent component = (AnimatedCompositeComponent)parent;
         Insets insets = parent.getInsets();
         Dimension size = parent.getSize();
         this.baseComponent.setBounds(insets.left, insets.top, size.width - (insets.left + insets.right), size.height - (insets.top + insets.bottom));
         int overlayPositionFromBottom = component.isOverlayVisible()
            ? component.getSize().height - component.getOverlayPosition()
            : component.getOverlayPosition();
         this.overlaidComponent
            .setBounds(
               insets.left,
               insets.top + size.height - overlayPositionFromBottom,
               size.width - (insets.left + insets.right),
               size.height - (insets.top + insets.bottom)
            );
         this.overlaidComponent.setVisible(overlayPositionFromBottom > 0);
         this.baseComponent.setVisible(overlayPositionFromBottom < size.height);
      }
   }

   @Override
   public Dimension minimumLayoutSize(Container parent) {
      synchronized (parent.getTreeLock()) {
         Insets insets = parent.getInsets();
         Dimension size1 = this.baseComponent.getMinimumSize();
         Dimension size2 = this.overlaidComponent.getMinimumSize();
         return new Dimension(
            insets.left + insets.right + Math.max(size1.width, size2.width), insets.top + insets.bottom + Math.max(size1.height, size2.height)
         );
      }
   }

   @Override
   public Dimension preferredLayoutSize(Container parent) {
      synchronized (parent.getTreeLock()) {
         Insets insets = parent.getInsets();
         Dimension size1 = this.baseComponent.getPreferredSize();
         Dimension size2 = this.overlaidComponent.getPreferredSize();
         return new Dimension(
            insets.left + insets.right + Math.max(size1.width, size2.width), insets.top + insets.bottom + Math.max(size1.height, size2.height)
         );
      }
   }

   @Override
   public Dimension maximumLayoutSize(Container target) {
      return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   }

   @Override
   public float getLayoutAlignmentX(Container parent) {
      return 0.5F;
   }

   @Override
   public float getLayoutAlignmentY(Container parent) {
      return 0.5F;
   }

   @Override
   public void invalidateLayout(Container target) {
   }

   @Override
   public void removeLayoutComponent(Component comp) {
   }
}
