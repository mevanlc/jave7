package de.jave.figlet.engine.primitives;

import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.layout.PrintDirection;
import de.jave.figlet.engine.layout.VerticalAlignment;
import de.jave.text.QuickString;
import net.disy.commons.core.util.Ensure;

public class FigFragment implements Cloneable {
   private QuickString[] lines;
   private FigLayout[] layoutTop;
   private FigLayout[] layoutBottom;
   private FigLayout[] layoutLeft;
   private FigLayout[] layoutRight;
   private int height;
   private int width;
   private int underLength;
   private VerticalAlignment verticalAlignment = VerticalAlignment.BOTTOM;
   private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
   private final int maxLineLength;
   private final PrintDirection printDirection;

   public FigFragment(
      QuickString[] lines,
      FigLayout[] layoutLeft,
      FigLayout[] layoutRight,
      int height,
      int width,
      FigLayout[] layoutTop,
      FigLayout[] layoutBottom,
      int underLength,
      int maxLineLength,
      PrintDirection printDirection
   ) {
      this.lines = lines;
      this.layoutLeft = layoutLeft;
      this.layoutRight = layoutRight;
      this.height = height;
      this.width = width;
      this.layoutTop = layoutTop;
      this.layoutBottom = layoutBottom;
      this.underLength = underLength;
      this.maxLineLength = maxLineLength;
      this.printDirection = printDirection;
      this.checkIntegrity();
   }

   public FigFragment(FigCharacter ch, FigLayout layout, int underLength, int maxLineLength) {
      Ensure.ensureArgumentNotNull(ch);
      this.printDirection = layout.getPrintDirection();
      this.maxLineLength = maxLineLength;
      this.underLength = underLength;
      this.height = ch.getHeight();
      this.lines = new QuickString[this.height];

      for (int i = 0; i < this.height; i++) {
         this.lines[i] = new QuickString(ch.getLine(i));
      }

      this.width = this.lines[0].length();
      this.layoutLeft = new FigLayout[this.height];
      this.layoutRight = new FigLayout[this.height];

      for (int i = 0; i < this.height; i++) {
         this.layoutLeft[i] = layout;
         this.layoutRight[i] = layout;
      }

      this.layoutTop = new FigLayout[this.width];
      this.layoutBottom = new FigLayout[this.width];

      for (int i = 0; i < this.width; i++) {
         this.layoutTop[i] = layout;
         this.layoutBottom[i] = layout;
      }

      this.verticalAlignment = VerticalAlignment.BOTTOM;
      this.checkIntegrity();
   }

   @Override
   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public void ensureUnsmushability() {
      if (this.lines[0].firstChar() == ' ') {
         this.lines[0].setCharAt(0, '\u007f');
      }
   }

   public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
      this.verticalAlignment = verticalAlignment;
   }

   public VerticalAlignment getVerticalAlignment() {
      return this.verticalAlignment;
   }

   public HorizontalAlignment getHorizontalAlignment() {
      return this.horizontalAlignment;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public QuickString[] getLines() {
      return this.lines;
   }

   public int getUnderLength() {
      return this.underLength;
   }

   public FigLayout[] getLayoutRight() {
      return this.layoutRight;
   }

   public FigLayout[] getLayoutLeft() {
      return this.layoutLeft;
   }

   public int getMaxLineLength() {
      return this.maxLineLength;
   }

   public FigLayout getTopLayout(int i) {
      return this.layoutTop[i];
   }

   public FigLayout getBottomLayout(int i) {
      return this.layoutBottom[i];
   }

   public FigLayout getRightLayout(int i) {
      return this.layoutRight[i];
   }

   public FigLayout getLeftLayout(int i) {
      return this.layoutLeft[i];
   }

   public void setTopLayout(int columnIndex, FigLayout layout) {
      Ensure.ensureArrayIndex(columnIndex, 0, this.width - 1);
      this.layoutTop[columnIndex] = layout;
   }

   public void setBottomLayout(int columnIndex, FigLayout layout) {
      Ensure.ensureArrayIndex(columnIndex, 0, this.width - 1);
      this.layoutBottom[columnIndex] = layout;
   }

   public void setRightLayout(int rowIndex, FigLayout layout) {
      Ensure.ensureArrayIndex(rowIndex, 0, this.height - 1);
      this.layoutRight[rowIndex] = layout;
   }

   public void setLeftLayout(int rowIndex, FigLayout layout) {
      Ensure.ensureArrayIndex(rowIndex, 0, this.height - 1);
      this.layoutLeft[rowIndex] = layout;
   }

   public void setLines(QuickString[] lines) {
      this.lines = lines;
   }

   public void setLayoutLeft(FigLayout[] ll) {
      this.layoutLeft = ll;
   }

   public void setLayoutRight(FigLayout[] ll) {
      this.layoutRight = ll;
   }

   public void setUnderLength(int underLenght) {
      this.underLength = underLenght;
   }

   public void setHeight(int i) {
      this.height = i;
   }

   public FigLayout[] getLayoutTop() {
      return this.layoutTop;
   }

   public FigLayout[] getLayoutBottom() {
      return this.layoutBottom;
   }

   public void setLayoutTop(FigLayout[] lt) {
      this.layoutTop = lt;
   }

   public void setLayoutBottom(FigLayout[] lt) {
      this.layoutBottom = lt;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void addColumnsRight(int columnCount) {
      QuickString gap = createEmptyString(columnCount);

      for (int i = 0; i < this.getHeight(); i++) {
         this.getLines()[i].append(gap);
      }

      FigLayout addedTopLayout = this.width == 0 ? this.layoutRight[0] : this.layoutTop[this.width - 1];
      this.layoutTop = append(this.layoutTop, columnCount, addedTopLayout);
      FigLayout addedBottomLayout = this.width == 0 ? this.layoutRight[0] : this.layoutBottom[this.width - 1];
      this.layoutBottom = append(this.layoutBottom, columnCount, addedBottomLayout);
      this.width += columnCount;
      this.checkIntegrity();
   }

   public void addColumnsLeft(int columnCount) {
      QuickString gap = createEmptyString(columnCount);

      for (int i = 0; i < this.getHeight(); i++) {
         this.getLines()[i].prepend(gap);
      }

      FigLayout addedTopLayout = this.width == 0 ? this.layoutLeft[0] : this.layoutTop[0];
      this.layoutTop = prepend(this.layoutTop, columnCount, addedTopLayout);
      FigLayout addedBottomLayout = this.width == 0 ? this.layoutLeft[0] : this.layoutBottom[0];
      this.layoutBottom = prepend(this.layoutBottom, columnCount, addedBottomLayout);
      this.width += columnCount;
      this.checkIntegrity();
   }

   public void addRowsBottom(int rowCount) {
      QuickString[] newLines = new QuickString[this.lines.length + rowCount];
      System.arraycopy(this.lines, 0, newLines, 0, this.lines.length);
      QuickString gap = createEmptyString(this.width);

      for (int i = 0; i < rowCount; i++) {
         newLines[this.lines.length + i] = new QuickString(gap);
      }

      FigLayout addedLeftLayout = this.height == 0 ? this.layoutBottom[0] : this.layoutLeft[this.height - 1];
      this.layoutLeft = append(this.layoutLeft, rowCount, addedLeftLayout);
      FigLayout addedRightLayout = this.height == 0 ? this.layoutBottom[0] : this.layoutRight[this.height - 1];
      this.layoutRight = append(this.layoutRight, rowCount, addedRightLayout);
      this.lines = newLines;
      this.height += rowCount;
      this.checkIntegrity();
   }

   public void addRowsTop(int rowCount) {
      QuickString[] newLines = new QuickString[this.lines.length + rowCount];
      System.arraycopy(this.lines, 0, newLines, rowCount, this.lines.length);
      QuickString gap = createEmptyString(this.width);

      for (int i = 0; i < rowCount; i++) {
         newLines[i] = new QuickString(gap);
      }

      FigLayout addedLeftLayout = this.height == 0 ? this.layoutTop[0] : this.layoutLeft[0];
      this.layoutLeft = prepend(this.layoutLeft, rowCount, addedLeftLayout);
      FigLayout addedRightLayout = this.height == 0 ? this.layoutTop[0] : this.layoutRight[0];
      this.layoutRight = prepend(this.layoutRight, rowCount, addedRightLayout);
      this.lines = newLines;
      this.height += rowCount;
      this.checkIntegrity();
   }

   private void checkIntegrity() {
      Ensure.ensureArgumentTrue(null, this.width == this.layoutTop.length);
      Ensure.ensureArgumentTrue(null, this.width == this.layoutBottom.length);
      Ensure.ensureArgumentTrue(null, this.height == this.layoutLeft.length);
      Ensure.ensureArgumentTrue(null, this.height == this.layoutRight.length);
      Ensure.ensureArgumentTrue(null, this.height == this.lines.length);

      for (int i = 0; i < this.lines.length; i++) {
         Ensure.ensureArgumentNotNull(this.lines[i]);
      }
   }

   private static FigLayout[] append(FigLayout[] oldLayouts, int length, FigLayout addedLayout) {
      FigLayout[] result = new FigLayout[oldLayouts.length + length];
      System.arraycopy(oldLayouts, 0, result, 0, oldLayouts.length);

      for (int i = 0; i < length; i++) {
         result[oldLayouts.length + i] = addedLayout;
      }

      return result;
   }

   private static FigLayout[] prepend(FigLayout[] oldLayouts, int length, FigLayout addedLayout) {
      FigLayout[] result = new FigLayout[oldLayouts.length + length];
      System.arraycopy(oldLayouts, 0, result, length, oldLayouts.length);

      for (int i = 0; i < length; i++) {
         result[i] = addedLayout;
      }

      return result;
   }

   private static QuickString createEmptyString(int length) {
      QuickString string = new QuickString(length);

      for (int i = 0; i < length; i++) {
         string.append(' ');
      }

      return string;
   }

   public void setHorizontalAlignment(HorizontalAlignment alignment) {
      this.horizontalAlignment = alignment;
   }

   public boolean isEmpty() {
      return this.getHeight() == 0 || this.getWidth() == 0;
   }

   public PrintDirection getPrintDirection() {
      return this.printDirection;
   }
}
