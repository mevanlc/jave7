package de.jave.lib;

import de.jave.jave.ICharacterDrawable;
import de.jave.lib.cell.Cell;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import net.dizzy.commons.core.util.Ensure;

public class CharacterPlate implements ICharacterDrawable {
   private int[][] glyphs;
   private boolean mix;
   private Dimension size;

   public CharacterPlate(Dimension size) {
      this(size.width, size.height);
   }

   public CharacterPlate(int width, int height) {
      this.glyphs = new int[height][width];
      this.size = new Dimension(width, height);
      this.clear();
      this.mix = false;
   }

   public CharacterPlate(int[][] ch) {
      this.glyphs = ch;
      int height = this.glyphs.length;
      int width = height == 0 ? 0 : this.glyphs[0].length;
      this.size = new Dimension(width, height);
      this.mix = false;
   }

   public CharacterPlate(String text) {
      if (text.length() == 0) {
         this.size = new Dimension(0, 0);
         this.glyphs = new int[0][0];
      } else {
         this.glyphs = TextTools.toCharField(text);
         this.size = new Dimension(this.glyphs[0].length, this.glyphs.length);
      }

      this.mix = false;
   }

   public CharacterPlate(String[] text) {
      if (text != null && text.length != 0) {
         this.glyphs = TextTools.toCharField(text);
         this.size = new Dimension(this.glyphs[0].length, this.glyphs.length);
      } else {
         this.size = new Dimension(0, 0);
         this.glyphs = new int[0][0];
      }

      this.mix = false;
   }

   public int[][] glyphPlane() {
      return this.glyphs;
   }

   public int[][] glyphPlaneClone() {
      return getClone(this.glyphs);
   }

   public boolean contains(int x, int y) {
      return x >= 0 && y >= 0 && x < this.size.width && y < this.size.height;
   }

   public boolean equals(String pattern, int x, int y) {
      int[] patternGlyphs = TextTools.toGlyphs(pattern);
      for (int i = 0; i < patternGlyphs.length; i++) {
         if (!this.contains(x + i, y)) {
            return false;
         }

         if (this.glyphAt(x + i, y) != patternGlyphs[i]) {
            return false;
         }
      }

      return true;
   }

   public static final int[][] getClone(int[][] original) {
      int h = original.length;
      int w = h == 0 ? 0 : original[0].length;
      int[][] result = new int[h][w];

      for (int y = 0; y < h; y++) {
         System.arraycopy(original[y], 0, result[y], 0, w);
      }

      return result;
   }

   public CharacterPlate getCopy(Rectangle region) {
      return this.getCopy(region.x, region.y, region.width, region.height);
   }

   public CharacterPlate getCopy(int x0, int y0, int width, int height) {
      Ensure.ensureArgumentTrue("height may not be less than 0, but was " + height, height >= 0);
      Ensure.ensureArgumentTrue("width may not be less than 0, but was " + width, width >= 0);
      int[][] sel = new int[height][width];

      for (int x = 0; x < width; x++) {
         int xx = x + x0;

         for (int y = 0; y < height; y++) {
            int yy = y + y0;
            if (xx >= 0 && xx < this.size.width && yy >= 0 && yy < this.size.height) {
               sel[y][x] = this.glyphAt(xx, yy);
            } else {
               sel[y][x] = ' ';
            }
         }
      }

      return new CharacterPlate(sel);
   }

   public CharacterPlate getClone() {
      int[][] c = getClone(this.glyphs);
      return new CharacterPlate(c);
   }

   public static CharacterPlate tabelize(String s) {
      String[] lines = TextTools.toStringArray(s);
      int columnCount = 1;
      String[][] rows = new String[lines.length][];

      for (int row = 0; row < lines.length; row++) {
         rows[row] = lines[row].split("\\t", -1);
         columnCount = Math.max(columnCount, rows[row].length);
      }

      int[] columnWidths = new int[columnCount];

      for (String[] row : rows) {
         for (int col = 0; col < row.length; col++) {
            columnWidths[col] = Math.max(columnWidths[col], TextTools.toGlyphs(row[col]).length);
         }
      }

      int resultWidth = 0;

      for (int col = 0; col < columnCount; col++) {
         resultWidth += columnWidths[col] + 1;
      }

      CharacterPlate cp = new CharacterPlate(Math.max(0, --resultWidth), lines.length);

      for (int row = 0; row < rows.length; row++) {
         int x = 0;
         for (int col = 0; col < rows[row].length; col++) {
            cp.paste(rows[row][col], x, row);
            x += columnWidths[col] + 1;
         }
      }

      return cp;
   }

   public boolean isEmpty(int emptyChar) {
      return this.isEmpty(0, 0, this.size.width - 1, this.size.height - 1, emptyChar);
   }

   public boolean isEmpty() {
      return this.isEmpty(0, 0, this.size.width - 1, this.size.height - 1, ' ');
   }

   public boolean isEmpty(int x0, int y0, int x1, int y1) {
      return this.isEmpty(x0, y0, x1, y1, ' ');
   }

   public boolean isEmpty(int x0, int y0, int x1, int y1, int emptyChar) {
      for (int x = x0; x <= x1; x++) {
         for (int y = y0; y <= y1; y++) {
            if (this.glyphs[y][x] != emptyChar) {
               return false;
            }
         }
      }

      return true;
   }

   public int getNonEmptyCharCount() {
      int result = 0;

      for (int x = 0; x < this.size.width; x++) {
         for (int y = 0; y < this.size.height; y++) {
            if (this.glyphs[y][x] != ' ') {
               result++;
            }
         }
      }

      return result;
   }

   public Insets getEmptyInsets() {
      return this.getEmptyInsets(' ');
   }

   public Insets getEmptyInsets(int emptyChar) {
      int top = 0;

      while (top < this.size.height && this.isEmpty(0, top, this.size.width - 1, top, emptyChar)) {
         top++;
      }

      int bottom = 0;

      while (bottom < this.size.height && this.isEmpty(0, this.size.height - bottom - 1, this.size.width - 1, this.size.height - bottom - 1, emptyChar)) {
         bottom++;
      }

      int left = 0;

      while (left < this.size.width && this.isEmpty(left, 0, left, this.size.height - 1, emptyChar)) {
         left++;
      }

      int right = 0;

      while (right < this.size.width && this.isEmpty(this.size.width - right - 1, 0, this.size.width - right - 1, this.size.height - 1, emptyChar)) {
         right++;
      }

      return new Insets(top, left, bottom, right);
   }

   public void setContent(int[][] ch) {
      this.glyphs = ch;
      this.size = new Dimension(this.glyphs[0].length, this.glyphs.length);
   }

   public void setSize(int width, int height) {
      if (width != this.size.width || height != this.size.height) {
         int[][] oldChars = this.glyphs;
         this.glyphs = new int[height][width];

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               if (y < this.size.height && x < this.size.width) {
                  this.glyphs[y][x] = oldChars[y][x];
               } else {
                  this.glyphs[y][x] = ' ';
               }
            }
         }

         this.size = new Dimension(width, height);
      }
   }

   public void removeColumnsLeft(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height][this.size.width - count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width - count >= 0)
              System.arraycopy(oldChars[y], 0 + count, this.glyphs[y], 0, this.size.width - count);
      }

      this.size = new Dimension(this.size.width - count, this.size.height);
   }

   public void removeColumnsRight(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height][this.size.width - count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width - count >= 0) System.arraycopy(oldChars[y], 0, this.glyphs[y], 0, this.size.width - count);
      }

      this.size = new Dimension(this.size.width - count, this.size.height);
   }

   public void removeLinesBottom(int count) {
      int[][] oldChars = this.glyphs;
      this.size = new Dimension(this.size.width, this.size.height - count);
      this.glyphs = new int[this.size.height][this.size.width];

       System.arraycopy(oldChars, 0, this.glyphs, 0, this.size.height);
   }

   public void removeLinesTop(int count) {
      int[][] oldChars = this.glyphs;
      this.size = new Dimension(this.size.width, this.size.height - count);
      this.glyphs = new int[this.size.height][this.size.width];

       System.arraycopy(oldChars, 0 + count, this.glyphs, 0, this.size.height);
   }

   public void addLinesBottom(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height + count][this.size.width];

      for (int y = 0; y < this.size.height; y++) {
          System.arraycopy(oldChars[y], 0, this.glyphs[y], 0, this.size.width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < this.size.width; x++) {
            this.glyphs[y + this.size.height][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width, this.size.height + count);
   }

   public void addLinesTop(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height + count][this.size.width];

      for (int y = 0; y < this.size.height; y++) {
          System.arraycopy(oldChars[y], 0, this.glyphs[y + count], 0, this.size.width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < this.size.width; x++) {
            this.glyphs[y][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width, this.size.height + count);
   }

   public void addColumnsRight(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height][this.size.width + count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width >= 0) System.arraycopy(oldChars[y], 0, this.glyphs[y], 0, this.size.width);
      }

      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < count; x++) {
            this.glyphs[y][x + this.size.width] = ' ';
         }
      }

      this.size = new Dimension(this.size.width + count, this.size.height);
   }

   public void addColumnsLeft(int count) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height][this.size.width + count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width >= 0) System.arraycopy(oldChars[y], 0, this.glyphs[y], 0 + count, this.size.width);
      }

      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < count; x++) {
            this.glyphs[y][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width + count, this.size.height);
   }

   public void insertLine(int line) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height + 1][this.size.width];

       if (line >= 0) System.arraycopy(oldChars, 0, this.glyphs, 0, line);

      for (int x = 0; x < this.size.width; x++) {
         this.glyphs[line][x] = ' ';
      }

       if (this.size.height - line >= 0)
           System.arraycopy(oldChars, line, this.glyphs, line + 1, this.size.height - line);

      this.size = new Dimension(this.size.width, this.size.height + 1);
   }

   public void insertLine(int line, String text) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height + 1][this.size.width];
      int[] textGlyphs = TextTools.toGlyphs(text);

       if (line >= 0) System.arraycopy(oldChars, 0, this.glyphs, 0, line);

      for (int x = 0; x < this.size.width; x++) {
         if (x < textGlyphs.length) {
            this.glyphs[line][x] = textGlyphs[x];
         } else {
            this.glyphs[line][x] = ' ';
         }
      }

       if (this.size.height - line >= 0)
           System.arraycopy(oldChars, line, this.glyphs, line + 1, this.size.height - line);

      this.size = new Dimension(this.size.width, this.size.height + 1);
   }

   public void removeLine(int line) {
      int[][] oldChars = this.glyphs;
      this.glyphs = new int[this.size.height - 1][this.size.width];

       if (line >= 0) System.arraycopy(oldChars, 0, this.glyphs, 0, line);

       if (this.size.height - (line + 1) >= 0)
           System.arraycopy(oldChars, line + 1, this.glyphs, line + 1 - 1, this.size.height - (line + 1));

      this.size = new Dimension(this.size.width, this.size.height - 1);
   }

   public String getLine(int lineNo) {
      return this.getLine(lineNo, 0);
   }

   public String getLine(int lineNo, int xStart) {
      StringBuilder line = new StringBuilder(this.size.width - xStart);
      for (int x = xStart; x < this.size.width; x++) {
         line.append(this.textAt(x, lineNo));
      }
      return line.toString();
   }

   @Override
   public boolean equals(Object other) {
      if (!(other instanceof CharacterPlate)) {
         return false;
      } else {
         CharacterPlate o = (CharacterPlate)other;
         if (o.getWidth() != this.getWidth()) {
            return false;
         } else if (o.getHeight() != this.getHeight()) {
            return false;
         } else {
            for (int x = 0; x < this.size.width; x++) {
               for (int y = 0; y < this.size.height; y++) {
                  if (this.glyphs[y][x] != o.glyphs[y][x]) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public void delete(int x0, int y0, int x1, int y1) {
      for (int x = x0; x <= x1; x++) {
         this.glyphs[y0][x] = ' ';
      }

      for (int y = y0 + 1; y <= y1; y++) {
         System.arraycopy(this.glyphs[y0], x0, this.glyphs[y], x0, x1 - x0 + 1);
      }
   }

   public void paste(String lineContent, int x, int y) {
      CharacterPlate pasteContent = new CharacterPlate(lineContent);
      this.paste(pasteContent, x, y, pasteContent.getWidth(), pasteContent.getHeight());
   }

   @Override
   public String toString() {
      return this.asString();
   }

   public String[] toStringArray() {
      return TextTools.toStringArray(this.glyphs);
   }

   public void pasteInto(CharacterPlate plate, int x0, int y0) {
      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < this.size.width; x++) {
            if (this.glyphs[y][x] != ' ') {
               plate.set(x + x0, y + y0, this.glyphs[y][x]);
            }
         }
      }
   }

   public void pasteIntoForce(CharacterPlate plate, int x0, int y0) {
      int yStart = 0;
      int xStart = 0;
      if (x0 < 0) {
         xStart = -x0;
      }

      if (y0 < 0) {
         yStart = -y0;
      }

      int h = this.size.height;
      int w = this.size.width;
      if (x0 + w > plate.size.width) {
         w = plate.size.width - x0;
      }

      if (y0 + h > plate.size.height) {
         h = plate.size.height - y0;
      }

      for (int y = yStart; y < h; y++) {
         System.arraycopy(this.glyphs[y], xStart, plate.glyphs[y + y0], x0 + xStart, w - xStart);
      }
   }

   public void paste(int[][] ch, int x, int y) {
      this.paste(new CharacterPlate(ch), x, y);
   }

   public void paste(CharacterPlate block, int x, int y) {
      this.paste(block, x, y, block.getWidth(), block.getHeight());
   }

   public void paste(CharacterPlate ch, int x, int y, int width, int height) {
      this.paste(ch.glyphPlane(), x, y, width, height);
   }

   public void paste(int[][] ch, int x, int y, int width, int height) {
      for (int currentY = 0; currentY < height; currentY++) {
         System.arraycopy(ch[currentY], 0, this.glyphs[y + currentY], x, width);
      }
   }

   public boolean isOverwrite() {
      return !this.mix;
   }

   public boolean isMix() {
      return this.mix;
   }

   public void setMix(boolean what) {
      this.mix = what;
   }

   public int glyphAt(int x, int y) {
      return this.glyphs[y][x];
   }

   public String textAt(int x, int y) {
      return this.cellAt(x, y).text();
   }

   public Cell cellAt(int x, int y) {
      return new Cell(this.glyphAt(x, y));
   }

   public void setText(int x, int y, String text) {
      this.set(x, y, Cell.fromText(text).glyph());
   }

   public void setForce(int x, int y, int ch) {
      if (ch == 1) {
         this.glyphs[y][x] = (this.glyphs[y][x] % 255);
         this.glyphs[y][x] = (this.glyphs[y][x] + 255);
      } else {
         this.glyphs[y][x] = ch;
      }
   }

   public void fill(int x, int y, int w, int h, int ch) {
      for (int yy = y; yy < y + h; yy++) {
         for (int xx = x; xx < x + w; xx++) {
            this.setForce(xx, yy, ch);
         }
      }
   }

   @Override
   public void set(int x, int y, int ch) {
      if (x >= 0 && y >= 0 && x < this.size.width && y < this.size.height) {
         if (ch == 1) {
            this.glyphs[y][x] = (this.glyphs[y][x] % 255);
            this.glyphs[y][x] = (this.glyphs[y][x] + 255);
         } else if (this.glyphs[y][x] != ch) {
            if (!this.mix || ch != ' ') {
               if (this.mix && ch != ' ') {
                  CharacterMergeRulesConfiguration mergeRulesConfiguration = CharacterMergeRulesConfiguration.INSTANCE;
                  int mergeResult = mergeRulesConfiguration.getMergeResult(this.glyphs[y][x], ch);
                  this.glyphs[y][x] = mergeResult;
               } else {
                  this.glyphs[y][x] = ch;
               }
            }
         }
      }
   }

   public int getPasteResult(int ch, int x, int y) {
      if (this.mix && ch != ' ') {
         CharacterMergeRulesConfiguration mergeRulesConfiguration = CharacterMergeRulesConfiguration.INSTANCE;
         return mergeRulesConfiguration.getMergeResult(this.glyphs[y][x], ch);
      } else {
         return ch;
      }
   }

   public int getWidth() {
      return this.size.width;
   }

   public int getHeight() {
      return this.size.height;
   }

   public void clear() {
      if (this.size.height != 0) {
         for (int x = 0; x < this.size.width; x++) {
            this.glyphs[0][x] = ' ';
         }

         for (int y = 1; y < this.size.height; y++) {
            System.arraycopy(this.glyphs[0], 0, this.glyphs[y], 0, this.size.width);
         }
      }
   }

   public void replace(int ch, int chNew) {
      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < this.size.width; x++) {
            if (this.glyphs[y][x] == ch) {
               this.glyphs[y][x] = chNew;
            }
         }
      }
   }

   public void panLeft() {
      for (int y = 0; y < this.size.height; y++) {
         int t = this.glyphs[y][0];
         System.arraycopy(this.glyphs[y], 1, this.glyphs[y], 0, this.size.width - 1);
         this.glyphs[y][this.size.width - 1] = t;
      }
   }

   public void panRight() {
      for (int y = 0; y < this.size.height; y++) {
         int t = this.glyphs[y][this.size.width - 1];
         System.arraycopy(this.glyphs[y], 0, this.glyphs[y], 1, this.size.width - 1);
         this.glyphs[y][0] = t;
      }
   }

   public void panUp() {
      int[] t = new int[this.size.width];
      System.arraycopy(this.glyphs[0], 0, t, 0, this.size.width);

      for (int y = 1; y < this.size.height; y++) {
         System.arraycopy(this.glyphs[y], 0, this.glyphs[y - 1], 0, this.size.width);
      }

      System.arraycopy(t, 0, this.glyphs[this.size.height - 1], 0, this.size.width);
   }

   public void panDown() {
      int[] t = new int[this.size.width];
      System.arraycopy(this.glyphs[this.size.height - 1], 0, t, 0, this.size.width);

      for (int y = this.size.height - 1; y > 0; y--) {
         System.arraycopy(this.glyphs[y - 1], 0, this.glyphs[y], 0, this.size.width);
      }

      System.arraycopy(t, 0, this.glyphs[0], 0, this.size.width);
   }

   public Dimension getSize() {
      return this.size;
   }

   public void changeColumnsRight(int size) {
      if (size != 0) {
         if (size < 0) {
            this.removeColumnsRight(-size);
         } else {
            this.addColumnsRight(size);
         }
      }
   }

   public void changeLinesBottom(int size) {
      if (size != 0) {
         if (size < 0) {
            this.removeLinesBottom(-size);
         } else {
            this.addLinesBottom(size);
         }
      }
   }

   public void changeColumnsLeft(int size) {
      if (size != 0) {
         if (size < 0) {
            this.removeColumnsLeft(-size);
         } else {
            this.addColumnsLeft(size);
         }
      }
   }

   public void changeLinesTop(int size) {
      if (size != 0) {
         if (size < 0) {
            this.removeLinesTop(-size);
         } else {
            this.addLinesTop(size);
         }
      }
   }

   public void expand(Insets expansion) {
      this.addColumnsLeft(expansion.left);
      this.addLinesTop(expansion.top);
      this.addColumnsRight(expansion.right);
      this.addLinesBottom(expansion.bottom);
   }

   public String asString() {
      return TextTools.toString(this.glyphs);
   }
}
