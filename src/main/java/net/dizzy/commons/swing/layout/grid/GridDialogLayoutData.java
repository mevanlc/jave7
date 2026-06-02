package net.dizzy.commons.swing.layout.grid;

public class GridDialogLayoutData {
   private static final int FILL_HORIZONTAL_VALUE = 1;
   private static final int RIGHT_VALUE = 2;
   private static final int FILL_BOTH_VALUE = 3;
   private static final int FILL_VERTICAL_VALUE = 4;

   public static final Integer FILL_HORIZONTAL = Integer.valueOf(FILL_HORIZONTAL_VALUE);
   public static final Integer RIGHT = Integer.valueOf(RIGHT_VALUE);
   public static final Integer FILL_BOTH = Integer.valueOf(FILL_BOTH_VALUE);
   public static final Integer FILL_VERTICAL = Integer.valueOf(FILL_VERTICAL_VALUE);

   private int constant;
   private int horizontalSpan = 1;
   private int verticalSpan = 1;
   private boolean grabExcessVerticalSpace;
   private int horizontalIndent;
   private int widthHint;
   private GridAlignment horizontalAlignment;
   private GridAlignment verticalAlignment;

   public GridDialogLayoutData() {
   }

   public GridDialogLayoutData(int constant) {
      this.constant = constant;
   }

   public GridDialogLayoutData setHorizontalSpan(int horizontalSpan) {
      this.horizontalSpan = horizontalSpan;
      return this;
   }

   public GridDialogLayoutData setVerticalSpan(int verticalSpan) {
      this.verticalSpan = verticalSpan;
      return this;
   }

   public GridDialogLayoutData setGrabExcessVerticalSpace(boolean grabExcessVerticalSpace) {
      this.grabExcessVerticalSpace = grabExcessVerticalSpace;
      return this;
   }

   public GridDialogLayoutData setHorizontalIndent(int horizontalIndent) {
      this.horizontalIndent = horizontalIndent;
      return this;
   }

   public GridDialogLayoutData setHorizontalAlignment(GridAlignment horizontalAlignment) {
      this.horizontalAlignment = horizontalAlignment;
      return this;
   }

   public GridDialogLayoutData setWidthHint(int widthHint) {
      this.widthHint = widthHint;
      return this;
   }

   public GridDialogLayoutData setVerticalAlignment(GridAlignment verticalAlignment) {
      this.verticalAlignment = verticalAlignment;
      return this;
   }

   String toMigConstraint() {
      StringBuilder builder = new StringBuilder();
      appendConstant(builder);
      if (horizontalSpan > 1) {
         append(builder, "span " + horizontalSpan);
      }
      if (verticalSpan > 1) {
         append(builder, "spany " + verticalSpan);
      }
      if (grabExcessVerticalSpace) {
         append(builder, "pushy");
      }
      if (horizontalIndent > 0) {
         append(builder, "gapleft " + horizontalIndent);
      }
      if (widthHint > 0) {
         append(builder, "w " + widthHint + "!");
      }
      appendAlignment(builder, horizontalAlignment, true);
      appendAlignment(builder, verticalAlignment, false);
      return builder.toString();
   }

   private void appendConstant(StringBuilder builder) {
      if (constant == FILL_HORIZONTAL_VALUE) {
         append(builder, "growx, pushx");
      } else if (constant == RIGHT_VALUE) {
         append(builder, "align right");
      } else if (constant == FILL_BOTH_VALUE) {
         append(builder, "grow, push");
      } else if (constant == FILL_VERTICAL_VALUE) {
         append(builder, "growy, pushy");
      }
   }

   private static void appendAlignment(StringBuilder builder, GridAlignment alignment, boolean horizontal) {
      if (alignment == null) {
         return;
      }
      if (horizontal) {
         if (alignment == GridAlignment.BEGINNING) {
            append(builder, "align left");
         } else if (alignment == GridAlignment.FILL) {
            append(builder, "growx");
         } else if (alignment == GridAlignment.END) {
            append(builder, "align right");
         }
      } else {
         if (alignment == GridAlignment.BEGINNING) {
            append(builder, "aligny top");
         } else if (alignment == GridAlignment.FILL) {
            append(builder, "growy");
         } else if (alignment == GridAlignment.END) {
            append(builder, "aligny bottom");
         }
      }
   }

   private static void append(StringBuilder builder, String constraint) {
      if (builder.length() > 0) {
         builder.append(", ");
      }
      builder.append(constraint);
   }
}
