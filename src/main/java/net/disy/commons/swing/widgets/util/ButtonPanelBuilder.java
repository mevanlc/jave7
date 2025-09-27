package net.disy.commons.swing.layout.util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class ButtonPanelBuilder {
   private static final int MINIMUM_BUTTON_WIDTH = 70;
   private final List<Component> components = new ArrayList<>();
   private final LayoutDirection direction;

   public ButtonPanelBuilder() {
      this(LayoutDirection.HORIZONTAL);
   }

   public ButtonPanelBuilder(LayoutDirection direction) {
      this.direction = direction;
   }

   public ButtonPanelBuilder add(Component... newComponents) {
       Collections.addAll(this.components, newComponents);

      return this;
   }

   public ButtonPanelBuilder add(Action... actions) {
      for (Action action : actions) {
         this.add(new JButton(action));
      }

      return this;
   }

   public JPanel createPanel() {
      return this.direction == LayoutDirection.HORIZONTAL ? this.createHorizontalButtonPanel() : this.createVerticalButtonPanel();
   }

   private JPanel createVerticalButtonPanel() {
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setWidthHint(70);
      layoutData.setHorizontalAlignment(GridAlignment.FILL);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.setBorder(
         new EmptyBorder(
            LayoutUtilities.getDpiAdjusted(4), LayoutUtilities.getDpiAdjusted(8), LayoutUtilities.getDpiAdjusted(10), LayoutUtilities.getDpiAdjusted(4)
         )
      );

      for (int i = 0; i < this.components.size(); i++) {
         panel.add(this.components.get(i), layoutData);
      }

      return panel;
   }

   private JPanel createHorizontalButtonPanel() {
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setWidthHint(70);
      layoutData.setHorizontalAlignment(GridAlignment.FILL);
      JPanel panel = new JPanel(new GridDialogLayout(this.components.size() + 1, false));
      panel.setBorder(
         new EmptyBorder(
            LayoutUtilities.getDpiAdjusted(10), LayoutUtilities.getDpiAdjusted(20), LayoutUtilities.getDpiAdjusted(4), LayoutUtilities.getDpiAdjusted(4)
         )
      );
      panel.add(Box.createGlue(), GridDialogLayoutData.FILL_HORIZONTAL);

      for (int i = 0; i < this.components.size(); i++) {
         panel.add(this.components.get(i), layoutData);
      }

      return panel;
   }
}
