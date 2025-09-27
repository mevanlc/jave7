package net.disy.commons.swing.component;

import javax.swing.JPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public class ComponentContainerDialogComponent implements IDialogComponent {
   private final IComponentContainer componentContainer;

   public ComponentContainerDialogComponent(IComponentContainer componentContainer) {
      this.componentContainer = componentContainer;
   }

   @Override
   public int getColumnCount() {
      return 1;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      GridDialogLayoutData gridLayoutData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      gridLayoutData.setHorizontalSpan(columnCount);
      panel.add(this.componentContainer.getContent(), gridLayoutData);
   }
}
