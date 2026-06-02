package de.jave.lib;

import de.jave.jave.ICharacterDrawable;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import net.dizzy.commons.core.util.Ensure;

public class CharacterPlate implements ICharacterDrawable {
   private char[][] chars;
   private boolean mix;
   private Dimension size;

   public CharacterPlate(Dimension size) {
      this(size.width, size.height);
   }

   public CharacterPlate(int width, int height) {
      this.chars = new char[height][width];
      this.size = new Dimension(width, height);
      this.clear();
      this.mix = false;
   }

   public CharacterPlate(char[][] ch) {
      this.chars = ch;
      int height = this.chars.length;
      int width = height == 0 ? 0 : this.chars[0].length;
      this.size = new Dimension(width, height);
      this.mix = false;
   }

   public CharacterPlate(String text) {
      if (text.length() == 0) {
         this.size = new Dimension(0, 0);
         this.chars = new char[0][0];
      } else {
         this.chars = TextTools.toCharField(text);
         this.size = new Dimension(this.chars[0].length, this.chars.length);
      }

      this.mix = false;
   }

   public CharacterPlate(String[] text) {
      if (text != null && text.length != 0) {
         this.chars = TextTools.toCharField(text);
         this.size = new Dimension(this.chars[0].length, this.chars.length);
      } else {
         this.size = new Dimension(0, 0);
         this.chars = new char[0][0];
      }

      this.mix = false;
   }

   public char[][] getContent() {
      return this.chars;
   }

   public char[][] getContentClone() {
      return getClone(this.chars);
   }

   public boolean contains(int x, int y) {
      return x >= 0 && y >= 0 && x < this.size.width && y < this.size.height;
   }

   public boolean equals(String pattern, int x, int y) {
      for (int i = 0; i < pattern.length(); i++) {
         if (!this.contains(x + i, y)) {
            return false;
         }

         if (this.get(x + i, y) != pattern.charAt(i)) {
            return false;
         }
      }

      return true;
   }

   public static final char[][] getClone(char[][] original) {
      int h = original.length;
      int w = h == 0 ? 0 : original[0].length;
      char[][] result = new char[h][w];

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
      char[][] sel = new char[height][width];

      for (int x = 0; x < width; x++) {
         int xx = x + x0;

         for (int y = 0; y < height; y++) {
            int yy = y + y0;
            if (xx >= 0 && xx < this.size.width && yy >= 0 && yy < this.size.height) {
               sel[y][x] = this.get(xx, yy);
            } else {
               sel[y][x] = ' ';
            }
         }
      }

      return new CharacterPlate(sel);
   }

   public CharacterPlate getClone() {
      char[][] c = getClone(this.chars);
      return new CharacterPlate(c);
   }

   public static CharacterPlate tabelize(String s) {
      String[] lines = TextTools.toStringArray(s);
      int columnCount = 1;

      for (int i = 0; i < lines.length; i++) {
         int currentColumnCount = 1 + TextTools.count(lines[i], '\t');
         if (currentColumnCount > columnCount) {
            columnCount = currentColumnCount;
         }
      }

      int[] columnWidths = new int[columnCount];

      for (int row = 0; row < lines.length; row++) {
         int col = 0;
         int oldIndex = 0;

         for (int index = lines[row].indexOf(9); index != -1; index = lines[row].indexOf(9, index + 1)) {
            if (columnWidths[col] < index - oldIndex) {
               columnWidths[col] = index - oldIndex;
            }

            col++;
            oldIndex = index;
         }

         if (columnWidths[col] < lines[row].length() - oldIndex) {
            columnWidths[col] = lines[row].length() - oldIndex;
         }
      }

      int resultWidth = 0;

      for (int col = 0; col < columnCount; col++) {
         resultWidth += columnWidths[col] + 1;
      }

      CharacterPlate cp = new CharacterPlate(--resultWidth, lines.length);

      for (int row = 0; row < lines.length; row++) {
         int col = 0;
         int x = 0;
         int oldIndex = 0;

         for (int index = lines[row].indexOf(9); index != -1; index = lines[row].indexOf(9, oldIndex)) {
            String cell = lines[row].substring(oldIndex, index);
            cp.paste(cell, x, row);
            x += columnWidths[col] + 1;
            col++;
            oldIndex = index + 1;
         }

         String cell = lines[row].substring(oldIndex);
         cp.paste(cell, x, row);
      }

      return cp;
   }

   public boolean isEmpty(char emptyChar) {
      return this.isEmpty(0, 0, this.size.width - 1, this.size.height - 1, emptyChar);
   }

   public boolean isEmpty() {
      return this.isEmpty(0, 0, this.size.width - 1, this.size.height - 1, ' ');
   }

   public boolean isEmpty(int x0, int y0, int x1, int y1) {
      return this.isEmpty(x0, y0, x1, y1, ' ');
   }

   public boolean isEmpty(int x0, int y0, int x1, int y1, char emptyChar) {
      for (int x = x0; x <= x1; x++) {
         for (int y = y0; y <= y1; y++) {
            if (this.chars[y][x] != emptyChar) {
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
            if (this.chars[y][x] != ' ') {
               result++;
            }
         }
      }

      return result;
   }

   public Insets getEmptyInsets() {
      return this.getEmptyInsets(' ');
   }

   public Insets getEmptyInsets(char emptyChar) {
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

   public void setContent(char[][] ch) {
      this.chars = ch;
      this.size = new Dimension(this.chars[0].length, this.chars.length);
   }

   public void setSize(int width, int height) {
      if (width != this.size.width || height != this.size.height) {
         char[][] oldChars = this.chars;
         this.chars = new char[height][width];

         for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
               if (y < this.size.height && x < this.size.width) {
                  this.chars[y][x] = oldChars[y][x];
               } else {
                  this.chars[y][x] = ' ';
               }
            }
         }

         this.size = new Dimension(width, height);
      }
   }

   public void removeColumnsLeft(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height][this.size.width - count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width - count >= 0)
              System.arraycopy(oldChars[y], 0 + count, this.chars[y], 0, this.size.width - count);
      }

      this.size = new Dimension(this.size.width - count, this.size.height);
   }

   public void removeColumnsRight(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height][this.size.width - count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width - count >= 0) System.arraycopy(oldChars[y], 0, this.chars[y], 0, this.size.width - count);
      }

      this.size = new Dimension(this.size.width - count, this.size.height);
   }

   public void removeLinesBottom(int count) {
      char[][] oldChars = this.chars;
      this.size = new Dimension(this.size.width, this.size.height - count);
      this.chars = new char[this.size.height][this.size.width];

       System.arraycopy(oldChars, 0, this.chars, 0, this.size.height);
   }

   public void removeLinesTop(int count) {
      char[][] oldChars = this.chars;
      this.size = new Dimension(this.size.width, this.size.height - count);
      this.chars = new char[this.size.height][this.size.width];

       System.arraycopy(oldChars, 0 + count, this.chars, 0, this.size.height);
   }

   public void addLinesBottom(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height + count][this.size.width];

      for (int y = 0; y < this.size.height; y++) {
          System.arraycopy(oldChars[y], 0, this.chars[y], 0, this.size.width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < this.size.width; x++) {
            this.chars[y + this.size.height][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width, this.size.height + count);
   }

   public void addLinesTop(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height + count][this.size.width];

      for (int y = 0; y < this.size.height; y++) {
          System.arraycopy(oldChars[y], 0, this.chars[y + count], 0, this.size.width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < this.size.width; x++) {
            this.chars[y][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width, this.size.height + count);
   }

   public void addColumnsRight(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height][this.size.width + count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width >= 0) System.arraycopy(oldChars[y], 0, this.chars[y], 0, this.size.width);
      }

      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < count; x++) {
            this.chars[y][x + this.size.width] = ' ';
         }
      }

      this.size = new Dimension(this.size.width + count, this.size.height);
   }

   public void addColumnsLeft(int count) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height][this.size.width + count];

      for (int y = 0; y < this.size.height; y++) {
          if (this.size.width >= 0) System.arraycopy(oldChars[y], 0, this.chars[y], 0 + count, this.size.width);
      }

      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < count; x++) {
            this.chars[y][x] = ' ';
         }
      }

      this.size = new Dimension(this.size.width + count, this.size.height);
   }

   public void insertLine(int line) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height + 1][this.size.width];

       if (line >= 0) System.arraycopy(oldChars, 0, this.chars, 0, line);

      for (int x = 0; x < this.size.width; x++) {
         this.chars[line][x] = ' ';
      }

       if (this.size.height - line >= 0)
           System.arraycopy(oldChars, line, this.chars, line + 1, this.size.height - line);

      this.size = new Dimension(this.size.width, this.size.height + 1);
   }

   public void insertLine(int line, String text) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height + 1][this.size.width];

       if (line >= 0) System.arraycopy(oldChars, 0, this.chars, 0, line);

      for (int x = 0; x < this.size.width; x++) {
         if (x < text.length()) {
            this.chars[line][x] = text.charAt(x);
         } else {
            this.chars[line][x] = ' ';
         }
      }

       if (this.size.height - line >= 0)
           System.arraycopy(oldChars, line, this.chars, line + 1, this.size.height - line);

      this.size = new Dimension(this.size.width, this.size.height + 1);
   }

   public void removeLine(int line) {
      char[][] oldChars = this.chars;
      this.chars = new char[this.size.height - 1][this.size.width];

       if (line >= 0) System.arraycopy(oldChars, 0, this.chars, 0, line);

       if (this.size.height - (line + 1) >= 0)
           System.arraycopy(oldChars, line + 1, this.chars, line + 1 - 1, this.size.height - (line + 1));

      this.size = new Dimension(this.size.width, this.size.height - 1);
   }

   public String getLine(int lineNo) {
      return this.getLine(lineNo, 0);
   }

   public String getLine(int lineNo, int xStart) {
      return xStart == 0 ? new String(this.chars[lineNo]) : new String(this.chars[lineNo], xStart, this.size.width - xStart);
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
                  if (this.chars[y][x] != o.chars[y][x]) {
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
         this.chars[y0][x] = ' ';
      }

      for (int y = y0 + 1; y <= y1; y++) {
         System.arraycopy(this.chars[y0], x0, this.chars[y], x0, x1 - x0 + 1);
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
      return TextTools.toStringArray(this.chars);
   }

   public void pasteInto(CharacterPlate plate, int x0, int y0) {
      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < this.size.width; x++) {
            if (this.chars[y][x] != ' ') {
               plate.set(x + x0, y + y0, this.chars[y][x]);
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
         System.arraycopy(this.chars[y], xStart, plate.chars[y + y0], x0 + xStart, w - xStart);
      }
   }

   public void paste(char[][] ch, int x, int y) {
      this.paste(new CharacterPlate(ch), x, y);
   }

   public void paste(CharacterPlate block, int x, int y) {
      this.paste(block, x, y, block.getWidth(), block.getHeight());
   }

   public void paste(CharacterPlate ch, int x, int y, int width, int height) {
      this.paste(ch.getContent(), x, y, width, height);
   }

   public void paste(char[][] ch, int x, int y, int width, int height) {
      for (int currentY = 0; currentY < height; currentY++) {
         System.arraycopy(ch[currentY], 0, this.chars[y + currentY], x, width);
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

   public char get(int x, int y) {
      return this.chars[y][x];
   }

   public void setForce(int x, int y, char ch) {
      if (ch == 1) {
         this.chars[y][x] = (char)(this.chars[y][x] % 255);
         this.chars[y][x] = (char)(this.chars[y][x] + 255);
      } else {
         this.chars[y][x] = ch;
      }
   }

   public void fill(int x, int y, int w, int h, char ch) {
      for (int yy = y; yy < y + h; yy++) {
         for (int xx = x; xx < x + w; xx++) {
            this.setForce(xx, yy, ch);
         }
      }
   }

   @Override
   public void set(int x, int y, char ch) {
      if (x >= 0 && y >= 0 && x < this.size.width && y < this.size.height) {
         if (ch == 1) {
            this.chars[y][x] = (char)(this.chars[y][x] % 255);
            this.chars[y][x] = (char)(this.chars[y][x] + 255);
         } else if (this.chars[y][x] != ch) {
            if (!this.mix || ch != ' ') {
               if (this.mix && ch != ' ') {
                  CharacterMergeRulesConfiguration mergeRulesConfiguration = CharacterMergeRulesConfiguration.INSTANCE;
                  char mergeResult = mergeRulesConfiguration.getMergeResult(this.chars[y][x], ch);
                  this.chars[y][x] = mergeResult;
               } else {
                  this.chars[y][x] = ch;
               }
            }
         }
      }
   }

   public char getPasteResult(char ch, int x, int y) {
      if (this.mix && ch != ' ') {
         CharacterMergeRulesConfiguration mergeRulesConfiguration = CharacterMergeRulesConfiguration.INSTANCE;
         return mergeRulesConfiguration.getMergeResult(this.chars[y][x], ch);
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
            this.chars[0][x] = ' ';
         }

         for (int y = 1; y < this.size.height; y++) {
            System.arraycopy(this.chars[0], 0, this.chars[y], 0, this.size.width);
         }
      }
   }

   public void replace(char ch, char chNew) {
      for (int y = 0; y < this.size.height; y++) {
         for (int x = 0; x < this.size.width; x++) {
            if (this.chars[y][x] == ch) {
               this.chars[y][x] = chNew;
            }
         }
      }
   }

   public void panLeft() {
      for (int y = 0; y < this.size.height; y++) {
         char t = this.chars[y][0];
         System.arraycopy(this.chars[y], 1, this.chars[y], 0, this.size.width - 1);
         this.chars[y][this.size.width - 1] = t;
      }
   }

   public void panRight() {
      for (int y = 0; y < this.size.height; y++) {
         char t = this.chars[y][this.size.width - 1];
         System.arraycopy(this.chars[y], 0, this.chars[y], 1, this.size.width - 1);
         this.chars[y][0] = t;
      }
   }

   public void panUp() {
      char[] t = new char[this.size.width];
      System.arraycopy(this.chars[0], 0, t, 0, this.size.width);

      for (int y = 1; y < this.size.height; y++) {
         System.arraycopy(this.chars[y], 0, this.chars[y - 1], 0, this.size.width);
      }

      System.arraycopy(t, 0, this.chars[this.size.height - 1], 0, this.size.width);
   }

   public void panDown() {
      char[] t = new char[this.size.width];
      System.arraycopy(this.chars[this.size.height - 1], 0, t, 0, this.size.width);

      for (int y = this.size.height - 1; y > 0; y--) {
         System.arraycopy(this.chars[y - 1], 0, this.chars[y], 0, this.size.width);
      }

      System.arraycopy(t, 0, this.chars[0], 0, this.size.width);
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
      return TextTools.toString(this.chars);
   }
}
