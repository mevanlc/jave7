package net.dizzy.commons.swing.layout.grid;

public final class GridDialogLayoutDataFactory {
   private GridDialogLayoutDataFactory() {
   }

   public static GridDialogLayoutData createHorizontalSpanData(int span) {
      return new GridDialogLayoutData().setHorizontalSpan(span);
   }

   public static GridDialogLayoutData createHorizontalSpanData(int span, int constant) {
      return new GridDialogLayoutData(constant).setHorizontalSpan(span);
   }
}
