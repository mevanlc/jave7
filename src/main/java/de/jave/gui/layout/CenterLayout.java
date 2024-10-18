package de.jave.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

public class CenterLayout implements LayoutManager, Serializable {
   @Override
   public void addLayoutComponent(String name, Component comp) {
   }

   @Override
   public void removeLayoutComponent(Component comp) {
   }

   @Override
   public Dimension preferredLayoutSize(Container container) {
      Component c = container.getComponent(0);
      if (c != null) {
         Dimension size = c.getPreferredSize();
         Insets insets = container.getInsets();
         size.width = size.width + insets.left + insets.right;
         size.height = size.height + insets.top + insets.bottom;
         return size;
      } else {
         return new Dimension(0, 0);
      }
   }

   @Override
   public Dimension minimumLayoutSize(Container container) {
      return this.preferredLayoutSize(container);
   }

   @Override
   public void layoutContainer(Container container) {
      if (container.getComponentCount() > 1) {
         throw new IllegalStateException("CenterLayout does not support more than one component, is:" + container.getComponentCount());
      } else {
         try {
            Component c = container.getComponent(0);
            c.setSize(c.getPreferredSize());
            Dimension size = c.getSize();
            Dimension containerSize = container.getSize();
            Insets containerInsets = container.getInsets();
            containerSize.width = containerSize.width - (containerInsets.left + containerInsets.right);
            containerSize.height = containerSize.height - (containerInsets.top + containerInsets.bottom);
            if (containerSize.width < size.width) {
               size.width = containerSize.width;
            }

            if (containerSize.height < size.height) {
               size.height = containerSize.height;
            }

            int componentLeft = containerSize.width / 2 - size.width / 2;
            int componentTop = containerSize.height / 2 - size.height / 2;
            componentLeft += containerInsets.left;
            componentTop += containerInsets.top;
            c.setBounds(componentLeft, componentTop, size.width, size.height);
         } catch (Exception var8) {
         }
      }
   }
}
