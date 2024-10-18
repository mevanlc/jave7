package net.disy.commons.swing.layout.grid;

public class GridDialogLayoutDataFactory {
   public static GridDialogLayoutData createHorizontalSpanData(int columnCount, IGridDialogLayoutData prototype) {
      GridDialogLayoutData layoutData = new GridDialogLayoutData(prototype);
      layoutData.setHorizontalSpan(columnCount);
      return layoutData;
   }

   public static GridDialogLayoutData createHorizontalSpanData(int columnCount) {
      return createHorizontalSpanData(columnCount, new GridDialogLayoutData());
   }

   public static GridDialogLayoutData createHorizontalFillNoGrab() {
      GridDialogLayoutData horizontalFillNoGrab = new GridDialogLayoutData();
      horizontalFillNoGrab.setHorizontalAlignment(GridAlignment.FILL);
      return horizontalFillNoGrab;
   }

   public static GridDialogLayoutData createHorizontalGrabNoFill() {
      GridDialogLayoutData horizontalFillNoGrab = new GridDialogLayoutData();
      horizontalFillNoGrab.setGrabExcessHorizontalSpace(true);
      return horizontalFillNoGrab;
   }

   public static GridDialogLayoutData createFillNoGrab() {
      GridDialogLayoutData fillNoGrab = new GridDialogLayoutData();
      fillNoGrab.setHorizontalAlignment(GridAlignment.FILL);
      fillNoGrab.setVerticalAlignment(GridAlignment.FILL);
      return fillNoGrab;
   }

   public static GridDialogLayoutData createTopData() {
      return createTopData(new GridDialogLayoutData());
   }

   public static GridDialogLayoutData createTopData(IGridDialogLayoutData prototyp) {
      GridDialogLayoutData topData = new GridDialogLayoutData(prototyp);
      topData.setVerticalAlignment(GridAlignment.BEGINNING);
      return topData;
   }

   public static GridDialogLayoutData createRightData() {
      GridDialogLayoutData data = new GridDialogLayoutData();
      data.setHorizontalAlignment(GridAlignment.END);
      return data;
   }
}
