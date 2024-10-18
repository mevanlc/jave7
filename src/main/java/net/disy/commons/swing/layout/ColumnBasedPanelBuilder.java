package net.disy.commons.swing.layout;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IGridDialogLayoutData;

public class ColumnBasedPanelBuilder implements IColumnContainer {
   private final List<JPanel> allPanels = new ArrayList<>();
   private JPanel currentPanel;

   public JComponent create() {
      JPanel container = new JPanel(new GridDialogLayout(Math.max(1, this.allPanels.size()), true));
      container.setBorder(BorderFactory.createEmptyBorder());

      for (JPanel panel : this.allPanels) {
         container.add(panel, GridDialogLayoutData.FILL_BOTH);
      }

      return container;
   }

   @Override
   public void add(JComponent component, IGridDialogLayoutData layoutData) {
      if (this.currentPanel == null) {
         this.currentPanel = new JPanel(new GridDialogLayout(1, false));
         this.allPanels.add(this.currentPanel);
      }

      this.currentPanel.add(component, layoutData);
   }

   public void breakColumn() {
      this.currentPanel = null;
   }
}
