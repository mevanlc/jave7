package de.jave.asciimation.editor;

import javax.swing.ListSelectionModel;

public class ListSelectionUtilities {
   public static int[] getSelectedIndices(ListSelectionModel selectionModel) {
      int iMin = selectionModel.getMinSelectionIndex();
      int iMax = selectionModel.getMaxSelectionIndex();
      if (iMin >= 0 && iMax >= 0) {
         int[] rvTmp = new int[1 + iMax - iMin];
         int n = 0;

         for (int i = iMin; i <= iMax; i++) {
            if (selectionModel.isSelectedIndex(i)) {
               rvTmp[n++] = i;
            }
         }

         int[] rv = new int[n];
         System.arraycopy(rvTmp, 0, rv, 0, n);
         return rv;
      } else {
         return new int[0];
      }
   }
}
