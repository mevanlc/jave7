package net.disy.commons.swing.layout.grid;

public class GridDialogLayoutDataBuilder {
   private final GridDialogLayoutData data = new GridDialogLayoutData();

   public GridDialogLayoutDataBuilder rightAligned() {
      this.data.setHorizontalAlignment(GridAlignment.END);
      return this;
   }

   public GridDialogLayoutDataBuilder leftAligned() {
      this.data.setHorizontalAlignment(GridAlignment.BEGINNING);
      return this;
   }

   public GridDialogLayoutDataBuilder topAligned() {
      this.data.setVerticalAlignment(GridAlignment.BEGINNING);
      return this;
   }

   public GridDialogLayoutDataBuilder horizontalSpan(int columns) {
      this.data.setHorizontalSpan(columns);
      return this;
   }

   public GridDialogLayoutDataBuilder grabExcessHorizontalSpace() {
      this.data.setGrabExcessHorizontalSpace(true);
      return this;
   }

   public GridDialogLayoutDataBuilder grabExcessVerticalSpace() {
      this.data.setGrabExcessVerticalSpace(true);
      return this;
   }

   public GridDialogLayoutDataBuilder filledHorizontal() {
      this.data.setHorizontalAlignment(GridAlignment.FILL);
      return this;
   }

   public GridDialogLayoutDataBuilder filledVertical() {
      this.data.setVerticalAlignment(GridAlignment.FILL);
      return this;
   }

   public GridDialogLayoutData get() {
      return this.data;
   }
}
