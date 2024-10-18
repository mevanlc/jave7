package net.disy.commons.swing.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;

public class RightMouseClickTreeSelectionMouseListener extends MouseAdapter {
   @Override
   public void mousePressed(MouseEvent event) {
      if (event.isMetaDown()) {
         JTree tree = (JTree)event.getComponent();
         int selectedRow = tree.getRowForLocation(event.getX(), event.getY());
         if (selectedRow != -1 && !tree.isRowSelected(selectedRow)) {
            tree.setSelectionRow(selectedRow);
         }
      }
   }
}
