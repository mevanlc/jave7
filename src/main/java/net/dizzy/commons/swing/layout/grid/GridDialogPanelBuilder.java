package net.dizzy.commons.swing.layout.grid;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.dizzy.commons.swing.component.IComponentContainer;

public class GridDialogPanelBuilder {
   private final int columns;
   private final JPanel panel;

   public GridDialogPanelBuilder() {
      this(false);
   }

   public GridDialogPanelBuilder(boolean equalColumns) {
      this.columns = 2;
      this.panel = new JPanel(new GridDialogLayout(columns, equalColumns));
   }

   public GridDialogPanelBuilder add(Component component) {
      panel.add(component);
      return this;
   }

   public GridDialogPanelBuilder add(JComponent component) {
      panel.add(component);
      return this;
   }

   public GridDialogPanelBuilder add(IComponentContainer component) {
      panel.add(component.getContent());
      return this;
   }

   public GridDialogPanelBuilder add(IDialogComponent component) {
      component.fillInto(panel, columns);
      return this;
   }

   public JPanel createPanel() {
      return panel;
   }
}
