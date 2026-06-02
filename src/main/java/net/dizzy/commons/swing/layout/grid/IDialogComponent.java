package net.dizzy.commons.swing.layout.grid;

import javax.swing.JPanel;

public interface IDialogComponent {
   void fillInto(JPanel panel, int columnCount);

   int getColumnCount();
}
