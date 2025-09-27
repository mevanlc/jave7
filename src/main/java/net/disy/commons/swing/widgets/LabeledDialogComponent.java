package net.disy.commons.swing.component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.IDialogComponent;

public class LabeledDialogComponent implements IDialogComponent {
   private final String label;
   private final JComponent component;

   public LabeledDialogComponent(String label, JComponent component) {
      this.label = label;
      this.component = component;
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(new JLabel(this.label), -1);
      GridDialogLayoutData layoutData = new GridDialogLayoutData();
      layoutData.setHorizontalSpan(columnCount - 1);
      layoutData.setGrabExcessHorizontalSpace(true);
      layoutData.setHorizontalAlignment(GridAlignment.FILL);
      panel.add(this.component);
   }
}
