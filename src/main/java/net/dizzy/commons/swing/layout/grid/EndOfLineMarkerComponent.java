package net.dizzy.commons.swing.layout.grid;

import javax.swing.JPanel;

public class EndOfLineMarkerComponent extends JPanel implements IDialogComponent {
   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(javax.swing.Box.createHorizontalStrut(0), "wrap");
   }

   @Override
   public int getColumnCount() {
      return 0;
   }
}
