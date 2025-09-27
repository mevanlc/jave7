package net.disy.commons.swing.geometry;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import net.disy.commons.core.util.Ensure;

public class SmartRectangle extends Rectangle {
   public SmartRectangle(Rectangle rect) {
      this(rect.x, rect.y, rect.width, rect.height);
   }

   public SmartRectangle(Point position, Dimension size) {
      this(position.x, position.y, size.width, size.height);
   }

   public SmartRectangle(int ulx, int uly, int width, int height) {
      super(ulx, uly, width, height);
      Ensure.ensureArgumentTrue("Width <0 : '" + width + "'", width >= 0);
      Ensure.ensureArgumentTrue("Height <0 : '" + height + "'", height >= 0);
   }

   public SmartRectangle(int width, int height) {
      super(width, height);
   }

   public int getUlx() {
      return this.x;
   }

   public int getUly() {
      return this.y;
   }

   public int getLrx() {
      return this.x + this.width;
   }

   public int getLry() {
      return this.y + this.height;
   }

   public void correctOrientation() {
      if (this.width < 0) {
         this.x = this.x + this.width;
         this.width = Math.abs(this.width);
      }

      if (this.height < 0) {
         this.y = this.y + this.height;
         this.height = Math.abs(this.height);
      }
   }

   @Override
   public void setLocation(int dx, int dy) {
      this.x += dx;
      this.y += dy;
   }

   public boolean intersects(int ulx, int uly, int lrx, int lry) {
      return super.intersects(new Rectangle(ulx, uly, lrx - ulx, lry - uly));
   }

   public boolean intersects(SmartRectangle b) {
      return super.intersects(b);
   }

   @Override
   public boolean contains(Rectangle r) {
      return this.x <= r.x && this.y <= r.y && this.x + this.width >= r.x + r.width && this.y + this.height >= r.y + r.height;
   }

   public boolean contains(int coordinateX, int coordinateY, int w) {
      return new Rectangle(this.x - w, this.y - w, this.width + 2 * w, this.height + 2 * w).contains(coordinateX, coordinateY);
   }

   public void clip(SmartRectangle clip) {
      if (this.intersects(clip)) {
         int ulx = this.getUlx();
         int uly = this.getUly();
         int lrx = this.getLrx();
         int lry = this.getLry();
         if (clip.getUlx() > ulx) {
            ulx = clip.getUlx();
         }

         if (clip.getUly() > uly) {
            uly = clip.getUly();
         }

         if (clip.getLrx() < lrx) {
            lrx = clip.getLrx();
         }

         if (clip.getLry() < lry) {
            lry = clip.getLry();
         }

         this.x = ulx;
         this.y = uly;
         this.width = lrx - ulx;
         this.height = lry - uly;
      }
   }

   public Point getCenter() {
      return new Point((this.getLrx() + this.getUlx()) / 2, (this.getLry() + this.getUly()) / 2);
   }

   public SmartRectangle minSize(int min) {
      int newWidth = Math.max(min, this.width);
      int newHeight = Math.max(min, this.height);
      int newX = this.x - (newWidth - this.width) / 2;
      int newY = this.y - (newHeight - this.height) / 2;
      return new SmartRectangle(newX, newY, newWidth, newHeight);
   }

   public SmartRectangle createEnlarged(int pixel) {
      return new SmartRectangle(this.x - pixel, this.y - pixel, this.width + 2 * pixel, this.height + 2 * pixel);
   }

   public SmartRectangle createTranslated(int xTranslation, int yTranslation) {
      return new SmartRectangle(this.x + xTranslation, this.y + yTranslation, this.width, this.height);
   }

   public SmartPoint getPosition() {
      return new SmartPoint(this.x, this.y);
   }

   public SmartRectangle createSmartUnion(SmartRectangle other) {
      Point position = new Point(Math.min(this.x, other.x), Math.min(this.y, other.y));
      int unionWidth = Math.max((int)this.getMaxX(), (int)other.getMaxX()) - position.x;
      int unionHeight = Math.max((int)this.getMaxY(), (int)other.getMaxY()) - position.y;
      return new SmartRectangle(position, new Dimension(unionWidth, unionHeight));
   }
}
