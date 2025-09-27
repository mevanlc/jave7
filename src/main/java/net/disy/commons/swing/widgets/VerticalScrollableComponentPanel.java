package net.disy.commons.swing.panel;

import java.awt.Component;
import java.awt.Rectangle;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class VerticalScrollableComponentPanel extends AbstractVerticalScrollablePanel {
   public VerticalScrollableComponentPanel(int preferredViewPortHeight) {
      super(new GridDialogLayout(1, false), preferredViewPortHeight);
   }

   @Override
   protected int getVerticalScrollabelUnitIncrement(Rectangle visibleRect, int direction) {
      if (direction > 0) {
         Component component = this.getNextComponentToShow(visibleRect);
         return component.getY() + component.getHeight() - (visibleRect.y + visibleRect.height);
      } else {
         Component component = this.getPreviousComponentToShow(visibleRect);
         return visibleRect.y - component.getY();
      }
   }

   private Component getNextComponentToShow(Rectangle visibleRect) {
      int componentIndex;
      for (componentIndex = 0; componentIndex < this.getComponentCount(); componentIndex++) {
         Component component = this.getComponent(componentIndex);
         if (component.getY() >= visibleRect.y) {
            break;
         }
      }

      while (componentIndex < this.getComponentCount()) {
         Component component = this.getComponent(componentIndex);
         if (component.getY() + component.getHeight() > visibleRect.y + visibleRect.height) {
            break;
         }

         componentIndex++;
      }

      if (componentIndex >= this.getComponentCount()) {
         componentIndex = this.getComponentCount() - 1;
      }

      return this.getComponent(componentIndex);
   }

   private Component getPreviousComponentToShow(Rectangle visibleRect) {
      int componentIndex;
      for (componentIndex = this.getComponentCount() - 1; componentIndex >= 0; componentIndex--) {
         Component component = this.getComponent(componentIndex);
         if (component.getY() + component.getHeight() <= visibleRect.y + visibleRect.height) {
            break;
         }
      }

      while (componentIndex >= 0) {
         Component component = this.getComponent(componentIndex);
         if (component.getY() < visibleRect.y) {
            break;
         }

         componentIndex--;
      }

      if (componentIndex < 0) {
         componentIndex = 0;
      }

      return this.getComponent(componentIndex);
   }
}
