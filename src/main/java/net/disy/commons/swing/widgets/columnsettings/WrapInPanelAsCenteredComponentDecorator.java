package net.disy.commons.swing.smarttable.columnsettings;

import java.awt.Component;
import javax.swing.JPanel;
import net.disy.commons.core.util.IDecorator;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class WrapInPanelAsCenteredComponentDecorator implements IDecorator<Component> {
   public Component decorate(Component input) {
      return wrapInPanelAsCentered(input);
   }

   public static JPanel wrapInPanelAsCentered(Component component) {
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      GridDialogLayoutData layoutData = new GridDialogLayoutData(GridDialogLayoutData.CENTER);
      layoutData.setGrabExcessHorizontalSpace(true);
      layoutData.setVerticalAlignment(GridAlignment.CENTER);
      layoutData.setGrabExcessVerticalSpace(true);
      panel.add(component, layoutData);
      return panel;
   }
}
