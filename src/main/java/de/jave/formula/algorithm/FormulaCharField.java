package de.jave.formula.algorithm;

import de.jave.text.TextTools;

public class FormulaCharField {
   private final char[][] ch;
   private int ascent;

   public FormulaCharField(int width, int height) {
      this(width, height, 0);
   }

   public FormulaCharField(String line) {
      this.ch = new char[1][line.length()];
      System.arraycopy(line.toCharArray(), 0, this.ch[0], 0, line.length());
   }

   public FormulaCharField(int width, int height, int ascent) {
      this.ch = new char[height][width];
      this.ascent = ascent;
      this.setAll(' ');
   }

   public FormulaCharField(String[] lines, int ascent) {
      this(getMaxLenght(lines), lines.length, ascent);

      for (int i = 0; i < lines.length; i++) {
         this.insert(lines[i], 0, i);
      }
   }

   private static int getMaxLenght(String[] lines) {
      int height = lines.length;
      if (height == 0) {
         return 0;
      } else {
         int width = lines[0].length();

         for (int i = 1; i < height; i++) {
            if (lines[i].length() > width) {
               width = lines[i].length();
            }
         }

         return width;
      }
   }

   private void setAll(char character) {
      for (int x = 0; x < this.getWidth(); x++) {
         this.ch[0][x] = character;
      }

      for (int y = 1; y < this.getHeight(); y++) {
         System.arraycopy(this.ch[0], 0, this.ch[y], 0, this.getWidth());
      }
   }

   public void set(int x, int y, char c) {
      this.ch[y][x] = c;
   }

   public char get(int x, int y) {
      return this.ch[y][x];
   }

   public void insert(String s, int x, int y) {
      this.insert(s.toCharArray(), x, y);
   }

   public void insert(char[] c, int x, int y) {
      System.arraycopy(c, 0, this.ch[y], x, c.length);
   }

   public void pasteInto(FormulaCharField f, int x0, int y0) {
      for (int y = 0; y < this.getHeight(); y++) {
         System.arraycopy(this.ch[y], 0, f.ch[y0 + y], x0, this.getWidth());
      }
   }

   public boolean isTopWeak() {
      for (int x = 0; x < this.getWidth(); x++) {
         if (this.ch[0][x] != ' ' && this.ch[0][x] != '_') {
            return false;
         }
      }

      return true;
   }

   @Override
   public String toString() {
      return TextTools.toString(this.ch);
   }

   public void setAscent(int ascent) {
      this.ascent = ascent;
   }

   public int getAscent() {
      return this.ascent;
   }

   public int getHeight() {
      return this.ch.length;
   }

   public int getWidth() {
      return this.getHeight() == 0 ? 0 : this.ch[0].length;
   }
}
