package net.disy.commons.swing.layout.grid;

import javax.swing.JPanel;

public interface IDialogComponent {
   int getColumnCount();

   void fillInto(JPanel var1, int var2);
}
