package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.gfx.GfxTools;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

public class Selection {
   private Rectangle region;
   private CharacterPlate content;
   private BooleanArea mask;
   private CharacterPlate originalContent;
   private int originalContentX;
   private int originalContentY;
   private Plate plate;
   private RectangleStyle textboxStyle;
   private int layer;
   private boolean d3View = true;
   private static final int LAYER_BACKGROUND = 0;
   private static final int LAYER_NORMAL = 1;
   public static final int LAYER_FOREGROUND = 2;
   private static final int LAYER_DIFFERENCE = 3;
   public static final int LAYER_DEFAULT = 1;
   public static final String[] STR_LAYER = new String[]{"Background", "Normal", "Foreground", "Difference"};
   public static final int OUTSIDE = 0;
   public static final int INSIDE = 1;
   public static final int NORTH = 2;
   public static final int WEST = 3;
   public static final int SOUTH = 4;
   public static final int EAST = 5;
   public static final int NORTHEAST = 6;
   public static final int NORTHWEST = 7;
   public static final int SOUTHEAST = 8;
   public static final int SOUTHWEST = 9;

   public Selection() {
      this.layer = 1;
   }

   public void setPlate(Plate plate) {
      this.plate = plate;
   }

   public boolean isTextbox() {
      return this.getTextboxStyle() != null;
   }

   public RectangleStyle getTextboxStyle() {
      if (this.textboxStyle == null) {
         this.textboxStyle = TextboxDialog.getTextboxStyle(this.content.getContent());
      }

      return this.textboxStyle;
   }

   public boolean hasSelection() {
      return this.region != null && this.content != null;
   }

   public Point getLocation() {
      return this.region.getLocation();
   }

   public boolean contains(Point location) {
      if (this.region == null || location == null) {
         return false;
      } else {
         return this.mask == null
            ? this.region.contains(location)
            : this.region.contains(location) && this.mask.isSet(location.x - this.region.x, location.y - this.region.y);
      }
   }

   public int getPlace(Point point) {
      Point p0 = this.plate.getScreenPointFor(this.region.x, this.region.y);
      Point p1 = this.plate.getScreenPointFor(this.region.x + this.region.width, this.region.y + this.region.height);
      if (p0.x > point.x || p0.y > point.y || p1.x < point.x || p1.y < point.y) {
         return 0;
      } else if (!this.isTextbox()) {
         return 1;
      } else {
         int x0 = p0.x;
         int y0 = p0.y;
         int x1 = p1.x + 2;
         int y1 = p1.y + 2;
         int b = 7;
         if (contains((x0 + x1 - 7) / 2, y0, 7, 7, point.x, point.y)) {
            return 2;
         } else if (contains((x0 + x1 - 7) / 2, y1 - 7, 7, 7, point.x, point.y)) {
            return 4;
         } else if (contains(x0, (y0 + y1 - 7) / 2, 7, 7, point.x, point.y)) {
            return 3;
         } else if (contains(x1 - 7, (y0 + y1 - 7) / 2, 7, 7, point.x, point.y)) {
            return 5;
         } else if (contains(x0, y1 - 7, 7, 7, point.x, point.y)) {
            return 9;
         } else if (contains(x1 - 7, y1 - 7, 7, 7, point.x, point.y)) {
            return 8;
         } else if (contains(x0, y0, 7, 7, point.x, point.y)) {
            return 7;
         } else {
            return contains(x1 - 7, y0, 7, 7, point.x, point.y) ? 6 : 1;
         }
      }
   }

   private static final boolean contains(int x0, int y0, int w, int h, int x, int y) {
      return x0 <= x && y0 <= y && x0 + w >= x && y0 + h >= y;
   }

   public void delete() {
      this.region = null;
      this.content = null;
      this.mask = null;
   }

   public CharacterPlate getContent() {
      return this.content;
   }

   public BooleanArea getMask() {
      return this.mask;
   }

   public JaveSelection getJaveSelection() {
      return new JaveSelection(this.content, this.mask);
   }

   private BooleanArea getEffectiveSelectionMask(Rectangle region, BooleanArea selectionMask) {
      BooleanArea layerMask = this.plate.getActiveLayerCoverageMask(region);
      if (selectionMask == null) {
         return layerMask;
      }
      if (layerMask == null) {
         return selectionMask;
      }

      BooleanArea effectiveMask = new BooleanArea(region.width, region.height);
      for (int y = 0; y < region.height; y++) {
         for (int x = 0; x < region.width; x++) {
            effectiveMask.set(x, y, selectionMask.isSet(x, y) && layerMask.isSet(x, y));
         }
      }
      return effectiveMask;
   }

   public Rectangle getRegion() {
      return this.region;
   }

   public int getWidth() {
      return this.region.width;
   }

   public int getHeight() {
      return this.region.height;
   }

   public void setLocation(int x, int y) {
      if (this.region != null) {
         this.region.x = x;
         this.region.y = y;
      }
   }

   public void resizeByCursorN_UP() {
      this.content.addLinesTop(1);
      this.region.y--;
      this.region.height++;
      CharacterPlate line = this.plate.cut(new Rectangle(this.region.x, this.region.y, this.region.width, 1));
      this.content.paste(line, 0, 0, this.region.width, 1);
      if (this.mask != null) {
         this.mask.addLinesTop(1, true);
      }
   }

   public void resizeByCursorS_DOWN() {
      this.content.addLinesBottom(1);
      this.region.height++;
      CharacterPlate line = this.plate.cut(new Rectangle(this.region.x, this.region.y + this.region.height - 1, this.region.width, 1));
      this.content.paste(line, 0, this.region.height - 1, this.region.width, 1);
      if (this.mask != null) {
         this.mask.addLinesBottom(1, true);
      }
   }

   public void resizeByCursorS_UP() {
      char[][] line = this.content.getCopy(0, this.region.height - 1, this.region.width, 1).getContent();

      try {
         this.plate.getContent().paste(line, this.region.x, this.region.y + this.region.height - 1, this.region.width, 1);
      } catch (Exception var3) {
      }

      this.content.setSize(this.content.getWidth(), this.content.getHeight() - 1);
      this.region.height--;
      if (this.mask != null) {
         this.mask.removeLinesBottom(1);
      }
   }

   public void resizeByCursorN_DOWN() {
      char[][] line = this.content.getCopy(0, 0, this.region.width, 1).getContent();

      try {
         this.plate.getContent().paste(line, this.region.x, this.region.y, this.region.width, 1);
      } catch (Exception var3) {
      }

      this.content.removeLinesTop(1);
      this.region.height--;
      this.region.y++;
      if (this.mask != null) {
         this.mask.removeLinesTop(1);
      }
   }

   public void resizeByCursorW_LEFT() {
      this.content.addColumnsLeft(1);
      this.region.x--;
      this.region.width++;
      CharacterPlate line = this.plate.cut(new Rectangle(this.region.x, this.region.y, 1, this.region.height));
      this.content.paste(line, 0, 0, 1, this.region.height);
      if (this.mask != null) {
         this.mask.addColumnsLeft(1, true);
      }
   }

   public void resizeByCursorE_RIGHT() {
      this.content.addColumnsRight(1);
      this.region.width++;
      CharacterPlate line = this.plate.cut(new Rectangle(this.region.x + this.region.width - 1, this.region.y, 1, this.region.height));
      this.content.paste(line, this.region.width - 1, 0, 1, this.region.height);
      if (this.mask != null) {
         this.mask.addColumnsRight(1, true);
      }
   }

   public void resizeByCursorE_LEFT() {
      char[][] line = this.content.getCopy(this.region.width - 1, 0, 1, this.region.height).getContent();

      try {
         this.plate.getContent().paste(line, this.region.x + this.region.width - 1, this.region.y, 1, this.region.width);
      } catch (Exception var3) {
      }

      this.content.removeColumnsRight(1);
      this.region.width--;
      if (this.mask != null) {
         this.mask.removeColumnsRight(1);
      }
   }

   public void resizeByCursorW_RIGHT() {
      char[][] line = this.content.getCopy(0, 0, 1, this.region.height).getContent();

      try {
         this.plate.getContent().paste(line, this.region.x, this.region.y, 1, this.region.height);
      } catch (Exception var3) {
      }

      this.content.removeColumnsLeft(1);
      this.region.width--;
      this.region.x++;
      if (this.mask != null) {
         this.mask.removeColumnsLeft(1);
      }
   }

   public void set(Rectangle region, CharacterPlate content) {
      this.set(region, content, null);
   }

   public void set(Rectangle region, BooleanArea mask) {
      CharacterPlate content1 = new CharacterPlate(region.width, region.height);
      BooleanArea effectiveMask = this.getEffectiveSelectionMask(region, mask);

      for (int y = 0; y < region.height; y++) {
         for (int x = 0; x < region.width; x++) {
            if ((effectiveMask == null || effectiveMask.isSet(x, y)) && this.plate.getContent().contains(region.x + x, region.y + y)) {
               content1.set(x, y, this.plate.getChar(region.x + x, region.y + y));
               this.plate.setCharForce(region.x + x, region.y + y, ' ');
            }
         }
      }

      this.set(region, content1, effectiveMask);
      this.optimizeSelectionRegion();
   }

   public void set(Rectangle region, JaveSelection sel) {
      this.set(region, sel.getContent(), sel.getMask());
   }

   private void set(Rectangle region, CharacterPlate content, BooleanArea mask) {
      this.mask = mask;
      this.region = region;
      this.content = content;
      this.textboxStyle = null;
      this.originalContentX = 0;
      this.originalContentY = 0;
      this.originalContent = content.getClone();
   }

   public void set(Point origin, char[][] content, BooleanArea mask) {
      if (origin != null && !isEmptySelectionContent(content)) {
         this.set(new Rectangle(origin.x, origin.y, content[0].length, content.length), new CharacterPlate(content), mask);
      } else {
         this.delete();
         this.textboxStyle = null;
      }
   }

   private static boolean isEmptySelectionContent(char[][] content) {
      return content == null || content.length == 0 || content[0] == null || content[0].length == 0;
   }

   public void set(Point origin, CharacterPlate content) {
      if (origin != null && content != null) {
         this.set(new Rectangle(origin.x, origin.y, content.getWidth(), content.getHeight()), content);
      } else {
         this.region = null;
         this.mask = null;
         this.content = null;
         this.textboxStyle = null;
      }
   }

   public void set(Point origin, JaveClipboardSelection sel) {
      if (origin != null && sel != null) {
         this.set(new Rectangle(origin.x, origin.y, sel.getContent().getWidth(), sel.getContent().getHeight()), sel.getContent(), sel.getMask());
      } else {
         this.region = null;
         this.mask = null;
         this.content = null;
         this.textboxStyle = null;
      }
   }

   public boolean add(Rectangle addRegion) {
      return this.add(addRegion, null);
   }

   public boolean add(Rectangle addRegion, BooleanArea addMask) {
      CharacterPlate addContent = new CharacterPlate(addRegion.width, addRegion.height);
      BooleanArea effectiveAddMask = this.getEffectiveSelectionMask(addRegion, addMask);

      for (int y = 0; y < addRegion.height; y++) {
         for (int x = 0; x < addRegion.width; x++) {
            int xx = addRegion.x + x;
            int yy = addRegion.y + y;
            if ((effectiveAddMask == null || effectiveAddMask.isSet(x, y)) && this.plate.getContent().contains(xx, yy)) {
               addContent.set(x, y, this.plate.getChar(xx, yy));
               this.plate.setCharForce(xx, yy, ' ');
            }
         }
      }

      int top = this.region.y - addRegion.y;
      if (top < 0) {
         top = 0;
      }

      int left = this.region.x - addRegion.x;
      if (left < 0) {
         left = 0;
      }

      int right = addRegion.x + addRegion.width - (this.region.x + this.region.width);
      if (right < 0) {
         right = 0;
      }

      int bottom = addRegion.y + addRegion.height - (this.region.y + this.region.height);
      if (bottom < 0) {
         bottom = 0;
      }

      if (top > 0 || bottom > 0 || right > 0 || left > 0) {
         if (this.mask == null) {
            this.mask = new BooleanArea(this.region.width, this.region.height);
            this.mask.setAll(true);
         }

         if (top > 0) {
            this.content.addLinesTop(top);
            this.mask.addLinesTop(top, false);
            this.region.y -= top;
            this.region.height += top;
         }

         if (bottom > 0) {
            this.content.addLinesBottom(bottom);
            this.mask.addLinesBottom(bottom, false);
            this.region.height += bottom;
         }

         if (left > 0) {
            this.content.addColumnsLeft(left);
            this.mask.addColumnsLeft(left, false);
            this.region.x -= left;
            this.region.width += left;
         }

         if (right > 0) {
            this.content.addColumnsRight(right);
            this.mask.addColumnsRight(right, false);
            this.region.width += right;
         }
      }

      if (this.mask == null) {
         return false;
      } else {
         boolean success = false;

         for (int y = 0; y < addRegion.height; y++) {
            for (int xx = 0; xx < addRegion.width; xx++) {
               int maskX = addRegion.x - this.region.x + xx;
               int maskY = addRegion.y - this.region.y + y;
               if (!this.mask.isSet(maskX, maskY) && (effectiveAddMask == null || effectiveAddMask.isSet(xx, y))) {
                  this.mask.set(maskX, maskY, true);
                  this.content.setForce(maskX, maskY, addContent.get(xx, y));
                  success = true;
               }
            }
         }

         if (success && this.mask.isAllSet()) {
            this.mask = null;
         }

         return success;
      }
   }

   public boolean remove(Rectangle removeRegion) {
      return this.remove(removeRegion, null);
   }

   public boolean remove(Rectangle removeRegion, BooleanArea removeMask) {
      if (removeRegion.y <= this.region.y + this.region.height - 1
         && removeRegion.x <= this.region.x + this.region.width - 1
         && removeRegion.y + removeRegion.height - 1 >= this.region.y
         && removeRegion.x + removeRegion.width - 1 >= this.region.x) {
         if (this.mask == null) {
            this.mask = new BooleanArea(this.region.width, this.region.height);
            this.mask.setAll(true);
         }

         int rdx = 0;
         int rdy = 0;
         int d = removeRegion.x + removeRegion.width - (this.region.x + this.region.width);
         if (d > 0) {
            removeRegion.width -= d;
         }

         d = removeRegion.y + removeRegion.height - (this.region.y + this.region.height);
         if (d > 0) {
            removeRegion.height -= d;
         }

         d = this.region.x - removeRegion.x;
         if (d > 0) {
            removeRegion.x += d;
            removeRegion.width -= d;
            rdx = d;
         }

         d = this.region.y - removeRegion.y;
         if (d > 0) {
            removeRegion.y += d;
            removeRegion.height -= d;
            rdy = d;
         }

         boolean success = false;

         for (int y = 0; y < removeRegion.height; y++) {
            for (int x = 0; x < removeRegion.width; x++) {
               int maskX = removeRegion.x - this.region.x + x;
               int maskY = removeRegion.y - this.region.y + y;
               if (this.mask.isSet(maskX, maskY) && (removeMask == null || removeMask.isSet(x + rdx, y + rdy))) {
                  this.mask.set(maskX, maskY, false);
                  char ch = this.content.get(maskX, maskY);
                  this.content.setForce(maskX, maskY, ' ');
                  this.plate.setCharForce(removeRegion.x + x, removeRegion.y + y, ch);
                  success = true;
               }
            }
         }

         this.optimizeSelectionRegion();
         return success;
      } else {
         return false;
      }
   }

   private void optimizeSelectionRegion() {
      if (this.mask != null) {
         if (this.mask.isEmpty()) {
            this.mask = null;
            this.content = null;
            this.region = null;
         } else {
            Insets in = this.mask.getEmptyInsets(false);
            if (in.top > 0) {
               this.content.removeLinesTop(in.top);
               this.mask.removeLinesTop(in.top);
               this.region.y = this.region.y + in.top;
               this.region.height = this.region.height - in.top;
            }

            if (in.left > 0) {
               this.content.removeColumnsLeft(in.left);
               this.mask.removeColumnsLeft(in.left);
               this.region.x = this.region.x + in.left;
               this.region.width = this.region.width - in.left;
            }

            if (in.right > 0) {
               this.content.removeColumnsRight(in.right);
               this.mask.removeColumnsRight(in.right);
               this.region.width = this.region.width - in.right;
            }

            if (in.bottom > 0) {
               this.content.removeLinesBottom(in.bottom);
               this.mask.removeLinesBottom(in.bottom);
               this.region.height = this.region.height - in.bottom;
            }
         }
      }
   }

   public void paste() {
      switch (this.getLayer()) {
         case 0:
            this.pasteBackground();
            break;
         case 1:
            this.pasteNormal();
            break;
         case 2:
            this.pasteForeground();
            break;
         case 3:
            this.pasteDifference();
      }
   }

   private void pasteNormal() {
      this.pasteIntoNormal(this.plate.getContent());
      this.plate.repaint();
   }

   public void pasteIntoNormal(CharacterPlate plate1) {
      for (int y = 0; y < this.region.height; y++) {
         for (int x = 0; x < this.region.width; x++) {
            if (this.mask == null || this.mask.isSet(x, y)) {
               plate1.set(this.region.x + x, this.region.y + y, this.content.get(x, y));
            }
         }
      }
   }

   private void pasteForeground() {
      CharacterPlate plateContent = this.plate.getContent();

      for (int x = 0; x < this.region.width; x++) {
         for (int y = 0; y < this.region.height; y++) {
            if ((this.mask == null || this.mask.isSet(x, y)) && this.content.get(x, y) != ' ') {
               plateContent.set(this.region.x + x, this.region.y + y, this.content.get(x, y));
            }
         }
      }

      this.plate.repaint();
   }

   private void pasteBackground() {
      CharacterPlate plateContent = this.plate.getContent();

      for (int y = 0; y < this.region.height; y++) {
         for (int x = 0; x < this.region.width; x++) {
            if ((this.mask == null || this.mask.isSet(x, y)) && this.content.get(x, y) != ' ' && plateContent.get(this.region.x + x, this.region.y + y) == ' ') {
               plateContent.set(this.region.x + x, this.region.y + y, this.content.get(x, y));
            }
         }
      }

      this.plate.repaint();
   }

   private void pasteDifference() {
      CharacterPlate plateContent = this.plate.getContent();

      for (int y = 0; y < this.region.height; y++) {
         for (int x = 0; x < this.region.width; x++) {
            if ((this.mask == null || this.mask.isSet(x, y)) && this.content.get(x, y) != ' ') {
               char ch = this.content.get(x, y);
               if (ch == plateContent.get(this.region.x + x, this.region.y + y)) {
                  plateContent.setForce(this.region.x + x, this.region.y + y, ' ');
               } else if (ch != ' ') {
                  plateContent.setForce(this.region.x + x, this.region.y + y, ch);
               }
            }
         }
      }

      this.plate.repaint();
   }

   public Point move(int dx, int dy) {
      return this.move(dx, dy, false);
   }

   public Point move(int dx, int dy, boolean collisionDetection) {
      if (!collisionDetection) {
         this.region.x += dx;
         this.region.y += dy;
         return new Point(dx, dy);
      } else {
         int rdx = 0;
         int rdy = 0;
         boolean ok = true;

         while (dx > 0 && ok) {
            int x = this.region.x + this.region.width;
            int y = this.region.y;

            while (true) {
               if (y < this.region.y + this.region.height) {
                  if (!this.plate.isInside(x, y) || this.plate.getChar(x, y) == ' ') {
                     y++;
                     continue;
                  }

                  ok = false;
               }

               if (ok) {
                  this.region.x++;
                  dx--;
                  rdx++;
               }
               break;
            }
         }

         ok = true;

         while (dx < 0 && ok) {
            int x = this.region.x - 1;
            int y = this.region.y;

            while (true) {
               if (y < this.region.y + this.region.height) {
                  if (!this.plate.isInside(x, y) || this.plate.getChar(x, y) == ' ') {
                     y++;
                     continue;
                  }

                  ok = false;
               }

               if (ok) {
                  this.region.x--;
                  dx++;
                  rdx--;
               }
               break;
            }
         }

         ok = true;

         while (dy < 0 && ok) {
            int y = this.region.y - 1;
            int x = this.region.x;

            while (true) {
               if (x < this.region.x + this.region.width) {
                  if (!this.plate.isInside(x, y) || this.plate.getChar(x, y) == ' ') {
                     x++;
                     continue;
                  }

                  ok = false;
               }

               if (ok) {
                  this.region.y--;
                  dy++;
                  rdy--;
               }
               break;
            }
         }

         ok = true;

         while (dy > 0 && ok) {
            int y = this.region.y + this.region.height;
            int x = this.region.x;

            while (true) {
               if (x < this.region.x + this.region.width) {
                  if (!this.plate.isInside(x, y) || this.plate.getChar(x, y) == ' ') {
                     x++;
                     continue;
                  }

                  ok = false;
               }

               if (ok) {
                  this.region.y++;
                  dy--;
                  rdy++;
               }
               break;
            }
         }

         return new Point(rdx, rdy);
      }
   }

   public void paintBorder(Graphics2D g, ColorScheme colorScheme) {
      if (this.region != null) {
         Point p0 = this.plate.getScreenPointFor(this.region.x, this.region.y);
         int w = this.region.width * this.plate.getCharWidth();
         int h = this.region.height * this.plate.getCharHeight();
         int blinkOffset = (int)(System.currentTimeMillis() / 600L % 6L);
         if (this.mask == null) {
            GfxTools.drawBrokenRectangle(g, Color.white, Color.black, p0.x, p0.y, w, h, blinkOffset);
            if (this.d3View) {
               if (this.layer == 0) {
                  g.setColor(colorScheme.getColorPlateBackground().darker());
                  g.drawLine(p0.x + 1, p0.y + 1, p0.x + w - 1, p0.y + 1);
                  g.drawLine(p0.x + 1, p0.y + 1, p0.x + 1, p0.y + h - 1);
               } else if (this.layer == 2) {
                  g.setColor(colorScheme.getColorPlateBackground().darker());
                  g.drawLine(p0.x + 1, p0.y + h + 1, p0.x + w + 1, p0.y + h + 1);
                  g.drawLine(p0.x + w + 1, p0.y + 1, p0.x + w + 1, p0.y + h + 1);
               }
            }
         } else {
            for (int x = 0; x < this.region.width; x++) {
               for (int y = 0; y < this.region.height; y++) {
                  if (this.mask.isSet(x, y)) {
                     if (y == 0 || !this.mask.isSet(x, y - 1)) {
                        int x0 = p0.x + x * this.plate.getCharWidth();
                        int y0 = p0.y + y * this.plate.getCharHeight();
                        int x1 = x0 + this.plate.getCharWidth();
                        g.setColor(Color.black);
                        g.drawLine(x0, y0, x1, y0);
                        g.setColor(Color.white);
                        GfxTools.drawBrokenLine(g, x0, y0, x1, y0, blinkOffset);
                     }

                     if (y == this.region.height - 1 || !this.mask.isSet(x, y + 1)) {
                        int x0 = p0.x + x * this.plate.getCharWidth();
                        int y0 = p0.y + (y + 1) * this.plate.getCharHeight();
                        int x1 = x0 + this.plate.getCharWidth();
                        g.setColor(Color.black);
                        g.drawLine(x0, y0, x1, y0);
                        g.setColor(Color.white);
                        GfxTools.drawBrokenLine(g, x0, y0, x1, y0, blinkOffset);
                     }

                     if (x == this.region.width - 1 || !this.mask.isSet(x + 1, y)) {
                        int x0 = p0.x + (x + 1) * this.plate.getCharWidth();
                        int y0 = p0.y + y * this.plate.getCharHeight();
                        int y1 = y0 + this.plate.getCharHeight();
                        g.setColor(Color.black);
                        g.drawLine(x0, y0, x0, y1);
                        g.setColor(Color.white);
                        GfxTools.drawBrokenLine(g, x0, y0, x0, y1, blinkOffset);
                     }

                     if (x == 0 || !this.mask.isSet(x - 1, y)) {
                        int x0 = p0.x + x * this.plate.getCharWidth();
                        int y0 = p0.y + y * this.plate.getCharHeight();
                        int y1 = y0 + this.plate.getCharHeight();
                        g.setColor(Color.black);
                        g.drawLine(x0, y0, x0, y1);
                        g.setColor(Color.white);
                        GfxTools.drawBrokenLine(g, x0, y0, x0, y1, blinkOffset);
                     }
                  }
               }
            }

            if (this.d3View) {
               if (this.layer == 0) {
                  g.setColor(colorScheme.getColorPlateBackground().darker());

                  for (int x = 0; x < this.region.width; x++) {
                     for (int yx = 0; yx < this.region.height; yx++) {
                        if (this.mask.isSet(x, yx)) {
                           if (yx == 0 || !this.mask.isSet(x, yx - 1)) {
                              int x0 = p0.x + x * this.plate.getCharWidth() + 1;
                              int y0 = p0.y + yx * this.plate.getCharHeight() + 1;
                              int x1 = x0 + this.plate.getCharWidth() - 1;
                              g.drawLine(x0, y0, x1, y0);
                           }

                           if (x == 0 || !this.mask.isSet(x - 1, yx)) {
                              int x0 = p0.x + x * this.plate.getCharWidth() + 1;
                              int y0 = p0.y + yx * this.plate.getCharHeight() + 1;
                              int y1 = y0 + this.plate.getCharHeight() - 1;
                              g.drawLine(x0, y0, x0, y1);
                           }
                        }
                     }
                  }
               } else if (this.layer == 2) {
                  g.setColor(colorScheme.getColorPlateBackground().darker());

                  for (int x = 0; x < this.region.width; x++) {
                     for (int yxx = 0; yxx < this.region.height; yxx++) {
                        if (this.mask.isSet(x, yxx)) {
                           if (yxx == this.region.height - 1 || !this.mask.isSet(x, yxx + 1)) {
                              int x0 = p0.x + x * this.plate.getCharWidth() + 1;
                              int y0 = p0.y + (yxx + 1) * this.plate.getCharHeight() + 1;
                              int x1 = x0 + this.plate.getCharWidth() - 1;
                              g.drawLine(x0, y0, x1, y0);
                           }

                           if (x == this.region.width - 1 || !this.mask.isSet(x + 1, yxx)) {
                              int x0 = p0.x + (x + 1) * this.plate.getCharWidth() + 1;
                              int y0 = p0.y + yxx * this.plate.getCharHeight() + 1;
                              int y1 = y0 + this.plate.getCharHeight() - 1;
                              g.drawLine(x0, y0, x0, y1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void paint(Graphics g, ColorScheme colorScheme) {
      if (this.region != null && this.content != null) {
         CharacterMetrics characterMetrics = this.plate.getCharacterMetrics();
         int charWidth = characterMetrics.getWidth();
         int charHeight = characterMetrics.getHeight();
         int charAscent = characterMetrics.getAscent();
         Point p0 = this.plate.getScreenPointFor(this.region.x, this.region.y);
         int w = this.region.width * charWidth;
         int h = this.region.height * charHeight;
         if (this.layer == 3) {
            int width = this.content.getWidth();
            int height = this.content.getHeight();
            int plateWidth = this.plate.getDocumentWidth();
            int plateHeight = this.plate.getDocumentHeight();
            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionBackground());
            } else {
               g.setColor(colorScheme.getColorPlateBackground());
            }

            for (int x = 0; x < width; x++) {
               for (int y = 0; y < height; y++) {
                  int ix = this.region.x + x;
                  int iy = this.region.y + y;
                  if (ix < 0 || ix >= plateWidth || iy < 0 || iy >= plateHeight || this.content.get(x, y) != ' ') {
                     g.fillRect(p0.x + x * charWidth, p0.y + y * charHeight, charWidth + 1, charHeight + 1);
                  }
               }
            }

            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionText());
            } else {
               g.setColor(colorScheme.getColorText());
            }

            for (int x = 0; x < width; x++) {
               for (int yx = 0; yx < height; yx++) {
                  char ch = this.content.get(x, yx);
                  if (ch != ' ') {
                     int ix = this.region.x + x;
                     int iy = this.region.y + yx;
                     if (ix < 0 || ix >= plateWidth || iy < 0 || iy >= plateHeight || this.plate.getChar(ix, iy) != ch) {
                        int xx = p0.x + x * charWidth;
                        int yy = p0.y + yx * charHeight;
                        g.drawString(String.valueOf(ch), xx, yy + charAscent);
                     }
                  }
               }
            }
         } else if (this.layer == 2) {
            int widthx = this.content.getWidth();
            int heightx = this.content.getHeight();
            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionBackground());
            } else {
               g.setColor(colorScheme.getColorPlateBackground());
            }

            for (int x = 0; x < widthx; x++) {
               for (int yxx = 0; yxx < heightx; yxx++) {
                  if (this.content.get(x, yxx) > ' ') {
                     g.fillRect(p0.x + x * charWidth, p0.y + yxx * charHeight, charWidth + 1, charHeight + 1);
                  }
               }
            }

            if (this.d3View) {
               Color shade = colorScheme.getColorPlateBackground().darker();
               Color light = colorScheme.getColorPlateBackground().brighter();

               for (int x = 0; x < widthx; x++) {
                  for (int yxxx = 0; yxxx < heightx; yxxx++) {
                     if (this.content.get(x, yxxx) > ' ') {
                        int xx = p0.x + x * charWidth;
                        int yy = p0.y + yxxx * charHeight;
                        if (x >= widthx - 1 || this.content.get(x + 1, yxxx) == ' ') {
                           g.setColor(shade);
                           g.drawLine(xx + charWidth, yy + 1, xx + charWidth, yy + charHeight);
                        }

                        if (yxxx >= heightx - 1 || this.content.get(x, yxxx + 1) == ' ') {
                           g.setColor(shade);
                           g.drawLine(xx + 1, yy + charHeight, xx + charWidth, yy + charHeight);
                        }

                        if (x == 0 || this.content.get(x - 1, yxxx) == ' ') {
                           g.setColor(light);
                           g.drawLine(xx, yy + 1, xx, yy + charHeight);
                        }

                        if (yxxx == 0 || this.content.get(x, yxxx - 1) == ' ') {
                           g.setColor(light);
                           g.drawLine(xx, yy, xx + charWidth, yy);
                        }
                     }
                  }
               }
            }

            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionText());
            } else {
               g.setColor(colorScheme.getColorText());
            }

            for (int yxxxx = 0; yxxxx < heightx; yxxxx++) {
               g.drawString(String.valueOf(this.content.getContent()[yxxxx]), p0.x, p0.y + yxxxx * charHeight + charAscent);
            }
         } else if (this.layer == 1) {
            int widthxx = this.content.getWidth();
            int heightxx = this.content.getHeight();
            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionBackground());
            } else {
               g.setColor(colorScheme.getColorPlateBackground());
            }

            if (this.mask == null) {
               g.fillRect(p0.x, p0.y, w, h);
            } else {
               for (int x = 0; x < widthxx; x++) {
                  for (int yxxxx = 0; yxxxx < heightxx; yxxxx++) {
                     if (this.mask.isSet(x, yxxxx)) {
                        g.fillRect(p0.x + x * charWidth, p0.y + yxxxx * charHeight, charWidth + 1, charHeight + 1);
                     }
                  }
               }
            }

            if (this.d3View) {
               g.setColor(colorScheme.getColorSelectionText());
            } else {
               g.setColor(colorScheme.getColorText());
            }

            if (this.mask == null) {
               for (int yxxxxx = 0; yxxxxx < heightxx; yxxxxx++) {
                  g.drawString(String.valueOf(this.content.getContent()[yxxxxx]), p0.x, p0.y + yxxxxx * charHeight + charAscent);
               }
            } else {
               for (int yxxxxx = 0; yxxxxx < heightxx; yxxxxx++) {
                  for (int x = 0; x < widthxx; x++) {
                     if (this.mask.isSet(x, yxxxxx)) {
                        char ch = this.content.get(x, yxxxxx);
                        if (ch != ' ') {
                           g.drawString(String.valueOf(ch), p0.x + x * charWidth, p0.y + yxxxxx * charHeight + charAscent);
                        }
                     }
                  }
               }
            }
         } else if (this.layer == 0) {
            int widthxxx = this.content.getWidth();
            int heightxxx = this.content.getHeight();
            int plateWidthx = this.plate.getDocumentWidth();
            int plateHeightx = this.plate.getDocumentHeight();

            for (int x = 0; x < widthxxx; x++) {
               for (int yxxxxx = 0; yxxxxx < heightxxx; yxxxxx++) {
                  int ix = this.region.x + x;
                  int iy = this.region.y + yxxxxx;
                  if (ix < 0 || ix >= plateWidthx || iy < 0 || iy >= plateHeightx || this.plate.getChar(ix, iy) == ' ') {
                     int xxx = p0.x + x * charWidth;
                     int yyx = p0.y + yxxxxx * charHeight;
                     if (this.mask == null || this.mask.isSet(x, yxxxxx)) {
                        if (this.d3View) {
                           g.setColor(colorScheme.getColorSelectionBackground());
                        } else {
                           g.setColor(colorScheme.getColorPlateBackground());
                        }

                        g.fillRect(xxx, yyx, charWidth + 1, charHeight + 1);
                     }

                     if (this.content.get(x, yxxxxx) > ' ') {
                        if (this.d3View) {
                           g.setColor(colorScheme.getColorSelectionText());
                        } else {
                           g.setColor(colorScheme.getColorText());
                        }

                        g.drawString(String.valueOf(this.content.get(x, yxxxxx)), xxx, yyx + charAscent);
                     }
                  }
               }
            }

            if (this.d3View) {
               Color shade = colorScheme.getColorPlateBackground().darker();
               Color light = colorScheme.getColorPlateBackground().brighter();

               for (int x = 0; x < widthxxx; x++) {
                  for (int yxxxxxx = 0; yxxxxxx < heightxxx; yxxxxxx++) {
                     int ix = this.region.x + x;
                     int iy = this.region.y + yxxxxxx;
                     if (ix >= 0
                        && ix < plateWidthx
                        && iy >= 0
                        && iy < plateHeightx
                        && (this.mask == null || this.mask.isSet(x, yxxxxxx))
                        && this.plate.getChar(ix, iy) != ' ') {
                        int xxxx = p0.x + x * charWidth;
                        int yyxx = p0.y + yxxxxxx * charHeight;
                        if (ix + 1 >= plateWidthx || this.plate.getChar(ix + 1, iy) == ' ') {
                           g.setColor(shade);
                           g.drawLine(xxxx + charWidth, yyxx + 1, xxxx + charWidth, yyxx + charHeight);
                        }

                        if (iy + 1 >= plateHeightx || this.plate.getChar(ix, iy + 1) == ' ') {
                           g.setColor(shade);
                           g.drawLine(xxxx + 1, yyxx + charHeight, xxxx + charWidth, yyxx + charHeight);
                        }

                        if (ix == 0 || this.plate.getChar(ix - 1, iy) == ' ') {
                           g.setColor(light);
                           g.drawLine(xxxx, yyxx, xxxx, yyxx + charHeight);
                        }

                        if (iy == 0 || this.plate.getChar(ix, iy - 1) == ' ') {
                           g.setColor(light);
                           g.drawLine(xxxx, yyxx, xxxx + charWidth, yyxx);
                        }
                     }
                  }
               }
            }
         }

         if (this.isTextbox()) {
            g.setColor(colorScheme.getColorToolRegion());
            int x0 = p0.x + 1;
            int y0 = p0.y + 1;
            int x1 = p0.x + w;
            int y1 = p0.y + h;
            int b = 5;
            g.drawRect(x0, y0, w - 2, h - 2);
            g.fillRect(x0, y0, 5, 5);
            g.fillRect(x0, y1 - 5, 5, 5);
            g.fillRect(x1 - 5, y0, 5, 5);
            g.fillRect(x1 - 5, y1 - 5, 5, 5);
            g.fillRect((x0 + x1 - 5) / 2, y0, 5, 5);
            g.fillRect((x0 + x1 - 5) / 2, y1 - 5, 5, 5);
            g.fillRect(x0, (y0 + y1 - 5) / 2, 5, 5);
            g.fillRect(x1 - 5, (y0 + y1 - 5) / 2, 5, 5);
         }
      }
   }

   private int getLayer() {
      return this.layer;
   }

   public void setLayer(int layer) {
      this.layer = layer;
   }

   public void set3dView(boolean d3View) {
      this.d3View = d3View;
   }

   public synchronized void resizeTextboxE(int dx) {
      if (dx != 0 && this.region.width + dx >= 3) {
         int w = this.region.width + dx;
         int h = this.region.height;
         CharacterPlate newContent = new CharacterPlate(w, h);
         if (dx > 0) {
            for (int x = 0; x < w - dx - 1; x++) {
               for (int y = 0; y < h; y++) {
                  newContent.setForce(x, y, this.content.get(x, y));
               }
            }

            for (int y = 0; y < h; y++) {
               newContent.setForce(w - 1, y, this.content.get(w - dx - 1, y));
            }

            for (int x = w - dx - 1; x < w - 1; x++) {
               for (int y = 1; y < h - 1; y++) {
                  char chOld = this.getOriginalCharAt(x, y);
                  if (chOld == 0) {
                     chOld = ' ';
                  }

                  newContent.setForce(x, y, chOld);
               }

               newContent.setForce(x, 0, this.content.get(w - dx - 2, 0));
               newContent.setForce(x, h - 1, this.content.get(w - dx - 2, h - 1));
            }
         } else {
            for (int x = 0; x < w - 1; x++) {
               for (int y = 0; y < h; y++) {
                  newContent.setForce(x, y, this.content.get(x, y));
               }
            }

            for (int y = 0; y < h; y++) {
               newContent.setForce(w - 1, y, this.content.get(w - dx - 1, y));
            }
         }

         this.content = newContent;
         this.region.width = w;
         this.plate.repaint();
      }
   }

   public synchronized void resizeTextboxW(int dx) {
      if (dx != 0 && this.region.width - dx >= 3) {
         int w = this.region.width - dx;
         int h = this.region.height;
         CharacterPlate newContent = new CharacterPlate(w, h);
         if (dx > 0) {
            for (int y = 0; y < h; y++) {
               newContent.setForce(0, y, this.content.get(0, y));
            }

            for (int x = 1; x < w; x++) {
               for (int y = 0; y < h; y++) {
                  newContent.setForce(x, y, this.content.get(x + dx, y));
               }
            }
         } else {
            for (int y = 0; y < h; y++) {
               newContent.setForce(0, y, this.content.get(0, y));
            }

            for (int x = 1; x < -dx + 1; x++) {
               for (int y = 1; y < h - 1; y++) {
                  char chOld = this.getOriginalCharAt(x + dx, y);
                  if (chOld == 0) {
                     chOld = ' ';
                  }

                  newContent.setForce(x, y, chOld);
               }

               newContent.setForce(x, 0, this.content.get(1, 0));
               newContent.setForce(x, h - 1, this.content.get(1, h - 1));
            }

            for (int x = -dx + 1; x < w; x++) {
               for (int y = 0; y < h; y++) {
                  newContent.setForce(x, y, this.content.get(x + dx, y));
               }
            }
         }

         this.originalContentX += dx;
         this.content = newContent;
         this.region.width = w;
         this.region.x += dx;
         this.plate.repaint();
      }
   }

   public synchronized void resizeTextboxN(int dy) {
      if (dy != 0 && this.region.height - dy >= 3) {
         int w = this.region.width;
         int h = this.region.height - dy;
         CharacterPlate newContent = new CharacterPlate(w, h);
         if (dy > 0) {
            for (int x = 0; x < w; x++) {
               newContent.setForce(x, 0, this.content.get(x, 0));
            }

            for (int y = 1; y < h; y++) {
               for (int x = 0; x < w; x++) {
                  newContent.setForce(x, y, this.content.get(x, y + dy));
               }
            }
         } else {
            for (int x = 0; x < w; x++) {
               newContent.setForce(x, 0, this.content.get(x, 0));
            }

            for (int y = 1; y < -dy + 1; y++) {
               for (int x = 1; x < w - 1; x++) {
                  char chOld = this.getOriginalCharAt(x, y + dy);
                  if (chOld == 0) {
                     chOld = ' ';
                  }

                  newContent.setForce(x, y, chOld);
               }

               newContent.setForce(0, y, this.content.get(0, 1));
               newContent.setForce(w - 1, y, this.content.get(w - 1, 1));
            }

            for (int y = -dy + 1; y < h; y++) {
               for (int x = 0; x < w; x++) {
                  newContent.setForce(x, y, this.content.get(x, y + dy));
               }
            }
         }

         this.originalContentY += dy;
         this.content = newContent;
         this.region.height = h;
         this.region.y += dy;
         this.plate.repaint();
      }
   }

   public synchronized void resizeTextboxS(int dy) {
      if (dy != 0 && this.region.height + dy >= 3) {
         int w = this.region.width;
         int h = this.region.height + dy;
         CharacterPlate newContent = new CharacterPlate(w, h);
         if (dy > 0) {
            for (int y = 0; y < h - dy - 1; y++) {
               for (int x = 0; x < w; x++) {
                  newContent.setForce(x, y, this.content.get(x, y));
               }
            }

            for (int x = 0; x < w; x++) {
               newContent.setForce(x, h - 1, this.content.get(x, h - dy - 1));
            }

            for (int y = h - dy - 1; y < h - 1; y++) {
               for (int x = 1; x < w - 1; x++) {
                  char chOld = this.getOriginalCharAt(x, y);
                  if (chOld == 0) {
                     chOld = ' ';
                  }

                  newContent.setForce(x, y, chOld);
               }

               newContent.setForce(0, y, this.content.get(0, h - dy - 2));
               newContent.setForce(w - 1, y, this.content.get(w - 1, h - dy - 2));
            }
         } else {
            for (int y = 0; y < h - 1; y++) {
               for (int x = 0; x < w; x++) {
                  newContent.setForce(x, y, this.content.get(x, y));
               }
            }

            for (int x = 0; x < w; x++) {
               newContent.setForce(x, h - 1, this.content.get(x, h - dy - 1));
            }
         }

         this.content = newContent;
         this.region.height = h;
         this.plate.repaint();
      }
   }

   private char getOriginalCharAt(int x, int y) {
      x += this.originalContentX;
      y += this.originalContentY;
      return x >= 1 && y >= 1 && x < this.originalContent.getWidth() - 1 && y < this.originalContent.getHeight() - 1
         ? this.originalContent.get(x, y)
         : '\u0000';
   }
}
