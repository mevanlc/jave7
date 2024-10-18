package net.disy.commons.swing.list;

public class ListSelectionMode {
   private final int listSelectionMode;
   public static final ListSelectionMode SINGLE_SELECTION = new ListSelectionMode(0);
   public static final ListSelectionMode MULTIPLE_INTERVAL_SELECTION = new ListSelectionMode(2);
   public static final ListSelectionMode SINGLE_INTERVAL_SELECTION = new ListSelectionMode(1);

   private ListSelectionMode(int listSelectionMode) {
      this.listSelectionMode = listSelectionMode;
   }

   public int getListSelectionMode() {
      return this.listSelectionMode;
   }
}
