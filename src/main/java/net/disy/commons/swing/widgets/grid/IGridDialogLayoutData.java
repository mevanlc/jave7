package net.disy.commons.swing.layout.grid;

public interface IGridDialogLayoutData {
   int DEFAULT = -1;

   int getHorizontalSpan();

   int getVerticalSpan();

   int getHorizontalIndent();

   int getWidthHint();

   int getHeightHint();

   GridAlignment getHorizontalAlignment();

   GridAlignment getVerticalAlignment();

   boolean isGrabExcessHorizontalSpace();

   boolean isGrabExcessVerticalSpace();
}
