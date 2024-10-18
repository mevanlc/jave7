package de.jave.lib.area;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

public class BooleanArea {
   private boolean[][] plate;

   public BooleanArea(Dimension size) {
      this(size.width, size.height);
   }

   public BooleanArea(int width, int height) {
      this.plate = new boolean[height][width];
   }

   private BooleanArea(boolean[][] plate) {
      this.plate = plate;
   }

   public int getWidth() {
      return this.plate[0].length;
   }

   public int getHeight() {
      return this.plate.length;
   }

   public Dimension getSize() {
      return new Dimension(this.getWidth(), this.getHeight());
   }

   public BooleanArea(String s) {
      char[] ch = s.toCharArray();
      int index = 0;
      int width = 0;

      while (ch[index] != ' ') {
         width *= 10;
         width += ch[index++] - '0';
      }

      index++;
      int height = 0;

      while (ch[index] != ' ') {
         height *= 10;
         height += ch[index++] - '0';
      }

      this.plate = new boolean[height][width];
      int i = 0;

      while (index < ch.length) {
         int count = ch[index++] - ' ';
         boolean state = count > 46;

         for (int var14 = count % 47; var14 > 0; var14--) {
            int x = i % width;
            int y = i / width;
            i++;
            this.plate[y][x] = state;
         }
      }
   }

   public BooleanArea getClone() {
      int height = this.getHeight();
      int width = this.getWidth();
      boolean[][] bytes = new boolean[height][width];

      for (int y = 0; y < height; y++) {
         System.arraycopy(this.plate[y], 0, bytes[y], 0, width);
      }

      return new BooleanArea(bytes);
   }

   public BooleanArea getCopy(Rectangle region) {
      return this.getCopy(region.x, region.y, region.width, region.height);
   }

   public BooleanArea getCopy(int x0, int y0, int w0, int h0) {
      boolean[][] sel = new boolean[h0][w0];

      for (int x = 0; x < w0; x++) {
         int xx = x + x0;

         for (int y = 0; y < h0; y++) {
            int yy = y + y0;
            if (xx >= 0 && xx < this.getWidth() && yy >= 0 && yy < this.getHeight()) {
               sel[y][x] = this.isSet(xx, yy);
            } else {
               sel[y][x] = false;
            }
         }
      }

      return new BooleanArea(sel);
   }

   public boolean[][] getContent() {
      return this.plate;
   }

   public void pasteInto(BooleanArea plate, int x0, int y0) {
      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            plate.set(x + x0, y + y0, this.isSet(x, y));
         }
      }
   }

   public void paste(BooleanArea insert, Rectangle region) {
      for (int y = 0; y < region.height; y++) {
         System.arraycopy(insert.plate[y], 0, this.plate[region.y + y], region.x, region.width);
      }
   }

   public void fill(int x, int y, int w, int h, boolean what) {
      for (int yy = y; yy < y + h; yy++) {
         for (int xx = x; xx < x + w; xx++) {
            this.set(xx, yy, what);
         }
      }
   }

   @Override
   public String toString() {
      StringBuffer s = new StringBuffer();
      s.append(this.getWidth()).append(" ").append(this.getHeight()).append(" ");
      boolean state = this.plate[0][0];
      int count = 0;

      for (int i = 0; i < this.getWidth() * this.getHeight(); i++) {
         int x = i % this.getWidth();
         int y = i / this.getWidth();
         if (this.plate[y][x] == state && count < 46) {
            count++;
         } else {
            if (state) {
               s.append((char)(count + 79));
            } else {
               s.append((char)(count + 32));
            }

            state = this.plate[y][x];
            count = 1;
         }
      }

      if (state) {
         s.append((char)(count + 79));
      } else {
         s.append((char)(count + 32));
      }

      return s.toString();
   }

   public void setAll(boolean set) {
      if (this.getHeight() != 0) {
         for (int x = 0; x < this.getWidth(); x++) {
            this.plate[0][x] = set;
         }

         for (int y = 1; y < this.getHeight(); y++) {
            System.arraycopy(this.plate[0], 0, this.plate[y], 0, this.getWidth());
         }
      }
   }

   public boolean isSet(int x, int y) {
      if (x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight()) {
         return this.plate[y][x];
      } else {
         throw new IllegalArgumentException("coordinates out of plate in BooleanPlate.isSet(" + x + "," + y + ")");
      }
   }

   public boolean isAllSet() {
      for (int y = 0; y < this.getHeight(); y++) {
         for (int x = 0; x < this.getWidth(); x++) {
            if (!this.plate[y][x]) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isEmpty() {
      for (int x = 0; x < this.getWidth(); x++) {
         for (int y = 0; y < this.getHeight(); y++) {
            if (this.plate[y][x]) {
               return false;
            }
         }
      }

      return true;
   }

   public Insets getEmptyInsets(boolean empty) {
      int top = 0;

      while (top < this.getHeight() && this.isEmpty(0, top, this.getWidth() - 1, top, empty)) {
         top++;
      }

      int bottom = 0;

      while (bottom < this.getHeight() && this.isEmpty(0, this.getHeight() - bottom - 1, this.getWidth() - 1, this.getHeight() - bottom - 1, empty)) {
         bottom++;
      }

      int left = 0;

      while (left < this.getWidth() && this.isEmpty(left, 0, left, this.getHeight() - 1, empty)) {
         left++;
      }

      int right = 0;

      while (right < this.getWidth() && this.isEmpty(this.getWidth() - right - 1, 0, this.getWidth() - right - 1, this.getHeight() - 1, empty)) {
         right++;
      }

      return new Insets(top, left, bottom, right);
   }

   public boolean isEmpty(int x0, int y0, int x1, int y1, boolean empty) {
      for (int x = x0; x <= x1; x++) {
         for (int y = y0; y <= y1; y++) {
            if (this.plate[y][x] != empty) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isOutside(int x, int y) {
      return x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight();
   }

   public void set(int x, int y, boolean set) {
      if (!this.isOutside(x, y)) {
         this.plate[y][x] = set;
      }
   }

   public void setSize(int newWidth, int newHeight) {
      int oldHeight = this.getHeight();
      int oldWidth = this.getWidth();
      if (newWidth != oldWidth || newHeight != oldHeight) {
         boolean[][] oldPlate = this.plate;
         this.plate = new boolean[newHeight][newWidth];

         for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
               if (y < oldHeight && x < oldWidth) {
                  this.plate[y][x] = oldPlate[y][x];
               } else {
                  this.plate[y][x] = false;
               }
            }
         }
      }
   }

   public void expand(Insets outsets, boolean fill) {
      boolean[][] oldPlate = this.plate;
      int newHeight = this.getHeight() + outsets.top + outsets.bottom;
      int newWidth = this.getWidth() + outsets.left + outsets.right;
      int oldHeight = this.getHeight();
      int oldWidth = this.getWidth();
      this.plate = new boolean[newHeight][newWidth];

      for (int y = 0; y < oldHeight; y++) {
         System.arraycopy(oldPlate[y], 0, this.plate[y + outsets.top], outsets.left, oldWidth);
      }

      for (int y = 0; y < outsets.top; y++) {
         for (int x = 0; x < newWidth; x++) {
            this.plate[y][x] = fill;
         }
      }

      for (int y = 0; y < oldHeight; y++) {
         for (int x = 0; x < outsets.left; x++) {
            this.plate[outsets.top + y][x] = fill;
         }

         for (int x = 0; x < outsets.right; x++) {
            this.plate[outsets.top + y][outsets.left + oldWidth + x] = fill;
         }
      }

      for (int y = oldHeight + outsets.top; y < newHeight; y++) {
         for (int x = 0; x < newWidth; x++) {
            this.plate[y][x] = fill;
         }
      }
   }

   public void shrink(Insets insets) {
      int newHeight = this.getHeight() - insets.top - insets.bottom;
      int newWidth = this.getWidth() - insets.left - insets.right;
      boolean[][] oldPlate = this.plate;
      this.plate = new boolean[newHeight][newWidth];

      for (int y = 0; y < newHeight; y++) {
         System.arraycopy(oldPlate[y + insets.top], insets.left, this.plate[y], 0, newWidth);
      }
   }

   public boolean isSet(Point point) {
      return this.isSet(point.x, point.y);
   }

   public void removeLinesBottom(int count) {
      boolean[][] oldPlate = this.plate;
      int height = this.getHeight() - count;
      int width = this.getWidth();
      this.plate = new boolean[height][width];

       System.arraycopy(oldPlate, 0, this.plate, 0, height);
   }

   public void removeLinesTop(int count) {
      boolean[][] oldPlate = this.plate;
      int height = this.getHeight() - count;
      int width = this.getWidth();
      this.plate = new boolean[height][width];

       System.arraycopy(oldPlate, 0 + count, this.plate, 0, height);
   }

   public void removeColumnsRight(int count) {
      boolean[][] oldPlate = this.plate;
      int width = this.getWidth();
      int height = this.getHeight();
      this.plate = new boolean[height][width - count];

      for (int y = 0; y < height; y++) {
          if (width - count >= 0) System.arraycopy(oldPlate[y], 0, this.plate[y], 0, width - count);
      }
   }

   public void removeColumnsLeft(int count) {
      boolean[][] oldPlate = this.plate;
      int width = this.getWidth() - count;
      int height = this.getHeight();
      this.plate = new boolean[height][width];

      for (int y = 0; y < height; y++) {
          System.arraycopy(oldPlate[y], 0 + count, this.plate[y], 0, width);
      }
   }

   public void addLinesBottom(int count, boolean set) {
      boolean[][] oldPlate = this.plate;
      int height = this.getHeight();
      int width = this.getWidth();
      this.plate = new boolean[height + count][width];

      for (int y = 0; y < height; y++) {
          System.arraycopy(oldPlate[y], 0, this.plate[y], 0, width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < width; x++) {
            this.plate[y + height][x] = set;
         }
      }
   }

   public void addLinesTop(int count, boolean set) {
      boolean[][] oldPlate = this.plate;
      int height = this.getHeight();
      int width = this.getWidth();
      this.plate = new boolean[height + count][width];

      for (int y = 0; y < height; y++) {
          System.arraycopy(oldPlate[y], 0, this.plate[y + count], 0, width);
      }

      for (int y = 0; y < count; y++) {
         for (int x = 0; x < width; x++) {
            this.plate[y][x] = set;
         }
      }
   }

   public void addColumnsRight(int count, boolean set) {
      boolean[][] oldPlate = this.plate;
      int height = this.getHeight();
      int width = this.getWidth();
      this.plate = new boolean[height][width + count];

      for (int y = 0; y < height; y++) {
          if (width >= 0) System.arraycopy(oldPlate[y], 0, this.plate[y], 0, width);
      }

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < count; x++) {
            this.plate[y][x + width] = set;
         }
      }
   }

   public void addColumnsLeft(int count, boolean set) {
      boolean[][] oldPlate = this.plate;
      int width = this.getWidth();
      int height = this.getHeight();
      this.plate = new boolean[height][width + count];

      for (int y = 0; y < height; y++) {
          if (width >= 0) System.arraycopy(oldPlate[y], 0, this.plate[y], 0 + count, width);
      }

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < count; x++) {
            this.plate[y][x] = set;
         }
      }
   }
}
