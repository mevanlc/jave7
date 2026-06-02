package net.dizzy.commons.swing.layout.grid;

public class GridDialogLayoutData {
   public static final int FILL_HORIZONTAL = 1;
   public static final int RIGHT = 2;
   public static final int FILL_BOTH = 3;
   public static final int FILL_VERTICAL = 4;

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
      switch (constant) {
         case FILL_HORIZONTAL:
            append(builder, "growx, pushx");
            break;
         case RIGHT:
            append(builder, "align right");
            break;
         case FILL_BOTH:
            append(builder, "grow, push");
            break;
         case FILL_VERTICAL:
            append(builder, "growy, pushy");
            break;
         default:
            break;
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
