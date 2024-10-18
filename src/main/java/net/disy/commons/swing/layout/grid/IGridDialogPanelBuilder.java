package net.disy.commons.swing.layout.grid;

import javax.swing.JPanel;

public interface IGridDialogPanelBuilder {
   void add(IDialogComponent var1);

   void addVerticalSpacing(int var1);

   JPanel createPanel();
}
