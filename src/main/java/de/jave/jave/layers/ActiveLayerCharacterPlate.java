package de.jave.jave.layers;

import de.jave.lib.CharacterMergeRulesConfiguration;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

public final class ActiveLayerCharacterPlate extends CharacterPlate {
   private final LayeredDocument document;

   public ActiveLayerCharacterPlate(LayeredDocument document) {
      super(document.getSize());
      this.document = document;
   }

   @Override
   public char get(int x, int y) {
      return this.document.getActiveChar(x, y);
   }

   @Override
   public void set(int x, int y, char ch) {
      if (this.isMix() && ch != ' ') {
         ch = CharacterMergeRulesConfiguration.INSTANCE.getMergeResult(this.document.getActiveChar(x, y), ch);
      }
      this.document.setActiveChar(x, y, ch);
   }

   @Override
   public void setForce(int x, int y, char ch) {
      this.document.setActiveCharForce(x, y, ch);
   }

   @Override
   public int getWidth() {
      return this.document.getSize().width;
   }

   @Override
   public int getHeight() {
      return this.document.getSize().height;
   }

   @Override
   public Dimension getSize() {
      return this.document.getSize();
   }

   @Override
   public char[][] getContent() {
      return this.document.getActiveContentProjection().getContent();
   }

   @Override
   public char[][] getContentClone() {
      return this.document.getActiveContentProjection().getContentClone();
   }

   @Override
   public CharacterPlate getClone() {
      return this.document.getActiveContentProjection().getClone();
   }

   @Override
   public CharacterPlate getCopy(Rectangle region) {
      return this.document.getActiveContentProjection().getCopy(region);
   }

   @Override
   public CharacterPlate getCopy(int x0, int y0, int width, int height) {
      return this.document.getActiveContentProjection().getCopy(x0, y0, width, height);
   }

   @Override
   public boolean contains(int x, int y) {
      return x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight();
   }

   @Override
   public boolean isEmpty() {
      return this.document.getActiveContentProjection().isEmpty();
   }

   @Override
   public int getNonEmptyCharCount() {
      return this.document.getActiveContentProjection().getNonEmptyCharCount();
   }

   @Override
   public Insets getEmptyInsets() {
      return this.document.getActiveContentProjection().getEmptyInsets();
   }

   @Override
   public String getLine(int lineNo) {
      return this.document.getActiveContentProjection().getLine(lineNo);
   }

   @Override
   public String getLine(int lineNo, int xStart) {
      return this.document.getActiveContentProjection().getLine(lineNo, xStart);
   }

   @Override
   public char getPasteResult(char ch, int x, int y) {
      if (this.isMix() && ch != ' ') {
         return CharacterMergeRulesConfiguration.INSTANCE.getMergeResult(this.document.getActiveChar(x, y), ch);
      }
      return ch;
   }

   @Override
   public String[] toStringArray() {
      return this.document.getActiveContentProjection().toStringArray();
   }

   @Override
   public String asString() {
      return this.document.getActiveContentProjection().asString();
   }

   @Override
   public void clear() {
      if (this.document == null) {
         super.clear();
         return;
      }
      CharacterPlate projection = this.document.getActiveContentProjection();
      for (int y = 0; y < projection.getHeight(); y++) {
         for (int x = 0; x < projection.getWidth(); x++) {
            this.document.setActiveCharForce(x, y, ' ');
         }
      }
   }

   @Override
   public void setSize(int width, int height) {
      CharacterPlate projection = this.document.getActiveContentProjection().getClone();
      projection.setSize(width, height);
      this.document.resizeDocument(width, height);
      this.document.replaceActiveContentProjection(projection);
   }

   @Override
   public void addColumnsRight(int count) {
      CharacterPlate projection = this.document.getActiveContentProjection().getClone();
      projection.addColumnsRight(count);
      this.document.resizeDocument(projection.getWidth(), projection.getHeight());
      this.document.replaceActiveContentProjection(projection);
   }

   @Override
   public void insertLine(int line) {
      CharacterPlate projection = this.document.getActiveContentProjection().getClone();
      projection.insertLine(line);
      this.document.resizeDocument(projection.getWidth(), projection.getHeight());
      this.document.replaceActiveContentProjection(projection);
   }

   @Override
   public void insertLine(int line, String text) {
      CharacterPlate projection = this.document.getActiveContentProjection().getClone();
      projection.insertLine(line, text);
      this.document.resizeDocument(projection.getWidth(), projection.getHeight());
      this.document.replaceActiveContentProjection(projection);
   }

   @Override
   public void removeLine(int line) {
      CharacterPlate projection = this.document.getActiveContentProjection().getClone();
      projection.removeLine(line);
      this.document.resizeDocument(projection.getWidth(), projection.getHeight());
      this.document.replaceActiveContentProjection(projection);
   }

   @Override
   public void paste(String lineContent, int x, int y) {
      this.paste(new CharacterPlate(lineContent), x, y);
   }

   @Override
   public void paste(char[][] ch, int x, int y) {
      this.paste(new CharacterPlate(ch), x, y);
   }

   @Override
   public void paste(CharacterPlate block, int x, int y) {
      this.paste(block, x, y, block.getWidth(), block.getHeight());
   }

   @Override
   public void paste(CharacterPlate ch, int x, int y, int width, int height) {
      this.paste(ch.getContent(), x, y, width, height);
   }

   @Override
   public void paste(char[][] ch, int x, int y, int width, int height) {
      for (int currentY = 0; currentY < height; currentY++) {
         for (int currentX = 0; currentX < width; currentX++) {
            if (this.contains(x + currentX, y + currentY)) {
               this.document.setActiveCharForce(x + currentX, y + currentY, ch[currentY][currentX]);
            }
         }
      }
   }

   @Override
   public void delete(int x0, int y0, int x1, int y1) {
      for (int x = x0; x <= x1; x++) {
         this.document.setActiveCharForce(x, y0, ' ');
      }
      for (int y = y0 + 1; y <= y1; y++) {
         for (int x = x0; x <= x1; x++) {
            this.document.setActiveCharForce(x, y, this.document.getActiveChar(x, y0));
         }
      }
   }

   @Override
   public void fill(int x, int y, int w, int h, char ch) {
      for (int yy = y; yy < y + h; yy++) {
         for (int xx = x; xx < x + w; xx++) {
            this.document.setActiveCharForce(xx, yy, ch);
         }
      }
   }
}
