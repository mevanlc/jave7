package net.dizzy.commons.swing.list;

import javax.swing.ListSelectionModel;

public enum ListSelectionMode {
   SINGLE_SELECTION(ListSelectionModel.SINGLE_SELECTION),
   SINGLE_INTERVAL_SELECTION(ListSelectionModel.SINGLE_INTERVAL_SELECTION),
   MULTIPLE_INTERVAL_SELECTION(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

   private final int listSelectionMode;

   ListSelectionMode(int listSelectionMode) {
      this.listSelectionMode = listSelectionMode;
   }

   public int getListSelectionMode() {
      return listSelectionMode;
   }
}
