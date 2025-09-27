package net.disy.commons.swing.layout.grid;

public class GridDialogLayoutData implements IGridDialogLayoutData {
   private GridAlignment verticalAlignment = GridAlignment.CENTER;
   private GridAlignment horizontalAlignment = GridAlignment.BEGINNING;
   private int widthHint = -1;
   private int heightHint = -1;
   private int horizontalIndent = 0;
   private int horizontalSpan = 1;
   private int verticalSpan = 1;
   private boolean grabExcessHorizontalSpace = false;
   private boolean grabExcessVerticalSpace = false;
   public static final IGridDialogLayoutData RIGHT = createRight();
   public static final IGridDialogLayoutData FILL_VERTICAL = createFillVertical();
   public static final IGridDialogLayoutData FILL_HORIZONTAL = createFillHorizontal();
   public static final IGridDialogLayoutData FILL_BOTH = createFillBoth();
   public static final IGridDialogLayoutData CENTER = createCenter();

   private static IGridDialogLayoutData createRight() {
      GridDialogLayoutData rightData = new GridDialogLayoutData();
      rightData.setHorizontalAlignment(GridAlignment.END);
      return rightData;
   }

   private static IGridDialogLayoutData createCenter() {
      GridDialogLayoutData centerData = new GridDialogLayoutData();
      centerData.setHorizontalAlignment(GridAlignment.CENTER);
      return centerData;
   }

   private static IGridDialogLayoutData createFillVertical() {
      GridDialogLayoutData fillVerticalData = new GridDialogLayoutData();
      fillVerticalData.setVerticalAlignment(GridAlignment.FILL);
      fillVerticalData.setGrabExcessVerticalSpace(true);
      return fillVerticalData;
   }

   private static IGridDialogLayoutData createFillHorizontal() {
      GridDialogLayoutData fillHorizontalData = new GridDialogLayoutData();
      fillHorizontalData.setHorizontalAlignment(GridAlignment.FILL);
      fillHorizontalData.setGrabExcessHorizontalSpace(true);
      return fillHorizontalData;
   }

   private static IGridDialogLayoutData createFillBoth() {
      GridDialogLayoutData fillBothData = new GridDialogLayoutData();
      fillBothData.setHorizontalAlignment(GridAlignment.FILL);
      fillBothData.setVerticalAlignment(GridAlignment.FILL);
      fillBothData.setGrabExcessHorizontalSpace(true);
      fillBothData.setGrabExcessVerticalSpace(true);
      return fillBothData;
   }

   public GridDialogLayoutData() {
   }

   public GridDialogLayoutData(IGridDialogLayoutData prototype) {
      this.heightHint = prototype.getHeightHint();
      this.horizontalAlignment = prototype.getHorizontalAlignment();
      this.horizontalIndent = prototype.getHorizontalIndent();
      this.horizontalSpan = prototype.getHorizontalSpan();
      this.verticalAlignment = prototype.getVerticalAlignment();
      this.verticalSpan = prototype.getVerticalSpan();
      this.widthHint = prototype.getWidthHint();
      this.grabExcessHorizontalSpace = prototype.isGrabExcessHorizontalSpace();
      this.grabExcessVerticalSpace = prototype.isGrabExcessVerticalSpace();
   }

   @Override
   public int getHorizontalSpan() {
      return this.horizontalSpan;
   }

   @Override
   public int getVerticalSpan() {
      return this.verticalSpan;
   }

   @Override
   public int getHorizontalIndent() {
      return this.horizontalIndent;
   }

   @Override
   public int getWidthHint() {
      return this.widthHint;
   }

   @Override
   public int getHeightHint() {
      return this.heightHint;
   }

   @Override
   public GridAlignment getHorizontalAlignment() {
      return this.horizontalAlignment;
   }

   @Override
   public GridAlignment getVerticalAlignment() {
      return this.verticalAlignment;
   }

   @Override
   public boolean isGrabExcessHorizontalSpace() {
      return this.grabExcessHorizontalSpace;
   }

   @Override
   public boolean isGrabExcessVerticalSpace() {
      return this.grabExcessVerticalSpace;
   }

   public GridDialogLayoutData setGrabExcessHorizontalSpace(boolean grabExcessHorizontalSpace) {
      this.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
      return this;
   }

   public GridDialogLayoutData setGrabExcessVerticalSpace(boolean grabExcessVerticalSpace) {
      this.grabExcessVerticalSpace = grabExcessVerticalSpace;
      return this;
   }

   public GridDialogLayoutData setHeightHint(int heightHint) {
      this.heightHint = heightHint;
      return this;
   }

   public GridDialogLayoutData setHorizontalAlignment(GridAlignment horizontalAlignment) {
      this.horizontalAlignment = horizontalAlignment;
      return this;
   }

   public GridDialogLayoutData setHorizontalIndent(int horizontalIndent) {
      this.horizontalIndent = horizontalIndent;
      return this;
   }

   public GridDialogLayoutData setHorizontalSpan(int horizontalSpan) {
      this.horizontalSpan = horizontalSpan;
      return this;
   }

   public GridDialogLayoutData setVerticalAlignment(GridAlignment verticalAlignment) {
      this.verticalAlignment = verticalAlignment;
      return this;
   }

   public GridDialogLayoutData setVerticalSpan(int verticalSpan) {
      this.verticalSpan = verticalSpan;
      return this;
   }

   public GridDialogLayoutData setWidthHint(int widthHint) {
      this.widthHint = widthHint;
      return this;
   }
}
