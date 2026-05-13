package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.Rectangle;
import net.disy.commons.core.util.Ensure;

public final class SecondaryLayer implements Layer {
   private final String id;
   private String name;
   private boolean visible = true;
   private boolean opaque = true;
   private Point position;
   private CharacterPlate content;

   public SecondaryLayer(String id, String name) {
      this(id, name, new Point(0, 0), new CharacterPlate(0, 0));
   }

   public SecondaryLayer(String id, String name, Point position, CharacterPlate content) {
      Ensure.ensureArgumentNotNull(id);
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(position);
      Ensure.ensureArgumentNotNull(content);
      this.id = id;
      this.name = name;
      this.position = new Point(position);
      this.content = content;
      this.trimToNonSpaceBounds();
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setName(String name) {
      Ensure.ensureArgumentNotNull(name);
      this.name = name;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public boolean isOpaque() {
      return this.opaque;
   }

   public void setOpaque(boolean opaque) {
      this.opaque = opaque;
   }

   public Point getPosition() {
      return new Point(this.position);
   }

   public void setPosition(Point position) {
      Ensure.ensureArgumentNotNull(position);
      this.position = new Point(position);
   }

   @Override
   public CharacterPlate getContent() {
      return this.content;
   }

   public Rectangle getBounds() {
      return new Rectangle(this.position.x, this.position.y, this.content.getWidth(), this.content.getHeight());
   }

   public void replaceContent(Point position, CharacterPlate content) {
      Ensure.ensureArgumentNotNull(position);
      Ensure.ensureArgumentNotNull(content);
      this.position = new Point(position);
      this.content = content;
      this.trimToNonSpaceBounds();
   }

   public void clipToDocument(java.awt.Dimension documentSize) {
      Rectangle bounds = this.getBounds();
      Rectangle documentBounds = new Rectangle(0, 0, documentSize.width, documentSize.height);
      Rectangle clippedBounds = bounds.intersection(documentBounds);
      if (clippedBounds.width <= 0 || clippedBounds.height <= 0) {
         this.content = new CharacterPlate(0, 0);
         this.position = new Point(Math.max(0, this.position.x), Math.max(0, this.position.y));
         return;
      }
      CharacterPlate clippedContent = this.content.getCopy(
         clippedBounds.x - this.position.x,
         clippedBounds.y - this.position.y,
         clippedBounds.width,
         clippedBounds.height
      );
      this.position = new Point(clippedBounds.x, clippedBounds.y);
      this.content = clippedContent;
      this.trimToNonSpaceBounds();
   }

   public void setCharAtDocument(int x, int y, char ch) {
      if (this.content.getWidth() == 0 || this.content.getHeight() == 0) {
         if (ch == ' ') {
            return;
         }
         this.position = new Point(x, y);
         this.content = new CharacterPlate(1, 1);
         this.content.set(0, 0, ch);
         return;
      }

      Rectangle bounds = this.getBounds();
      if (!bounds.contains(x, y)) {
         if (ch == ' ') {
            return;
         }
         this.expandToInclude(x, y);
      }

      this.content.set(x - this.position.x, y - this.position.y, ch);
      this.trimToNonSpaceBounds();
   }

   public char getCharAtDocument(int x, int y) {
      Rectangle bounds = this.getBounds();
      if (!bounds.contains(x, y)) {
         return ' ';
      }
      return this.content.get(x - this.position.x, y - this.position.y);
   }

   public SecondaryLayer duplicate(String newId, String newName) {
      SecondaryLayer copy = new SecondaryLayer(newId, newName, this.position, this.content.getClone());
      copy.setVisible(this.visible);
      copy.setOpaque(this.opaque);
      return copy;
   }

   private void expandToInclude(int documentX, int documentY) {
      int oldLeft = this.position.x;
      int oldTop = this.position.y;
      int oldRight = oldLeft + this.content.getWidth() - 1;
      int oldBottom = oldTop + this.content.getHeight() - 1;
      int newLeft = Math.min(oldLeft, documentX);
      int newTop = Math.min(oldTop, documentY);
      int newRight = Math.max(oldRight, documentX);
      int newBottom = Math.max(oldBottom, documentY);
      CharacterPlate expanded = new CharacterPlate(newRight - newLeft + 1, newBottom - newTop + 1);
      expanded.paste(this.content, oldLeft - newLeft, oldTop - newTop);
      this.position = new Point(newLeft, newTop);
      this.content = expanded;
   }

   private void trimToNonSpaceBounds() {
      Rectangle nonSpaceBounds = findNonSpaceBounds(this.content);
      if (nonSpaceBounds == null) {
         this.content = new CharacterPlate(0, 0);
         return;
      }

      if (nonSpaceBounds.x == 0
         && nonSpaceBounds.y == 0
         && nonSpaceBounds.width == this.content.getWidth()
         && nonSpaceBounds.height == this.content.getHeight()) {
         return;
      }

      CharacterPlate trimmed = this.content.getCopy(nonSpaceBounds);
      this.position.translate(nonSpaceBounds.x, nonSpaceBounds.y);
      this.content = trimmed;
   }

   private static Rectangle findNonSpaceBounds(CharacterPlate plate) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      for (int y = 0; y < plate.getHeight(); y++) {
         for (int x = 0; x < plate.getWidth(); x++) {
            if (plate.get(x, y) != ' ') {
               minX = Math.min(minX, x);
               minY = Math.min(minY, y);
               maxX = Math.max(maxX, x);
               maxY = Math.max(maxY, y);
            }
         }
      }
      return minX == Integer.MAX_VALUE ? null : new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
   }
}
