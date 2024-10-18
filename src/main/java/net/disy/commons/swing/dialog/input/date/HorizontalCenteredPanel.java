package net.disy.commons.swing.dialog.input.date;

import javax.swing.JPanel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class HorizontalCenteredPanel extends JPanel {
   public HorizontalCenteredPanel(JPanel content) {
      this.setLayout(new GridDialogLayout(1, false));
      GridDialogLayoutData layoutData = new GridDialogLayoutData(GridDialogLayoutData.CENTER);
      layoutData.setGrabExcessHorizontalSpace(true);
      this.add(content, layoutData);
   }
}
