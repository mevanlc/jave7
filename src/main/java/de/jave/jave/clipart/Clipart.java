package de.jave.jave.clipart;

import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import net.dizzy.commons.core.util.Ensure;

public class Clipart {
   private final String name;
   private final String code;
   private final String author;
   private final int width;
   private final int height;

   public Clipart(String name, String code, String author) {
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(code);
      Ensure.ensureArgumentNotNull(author);
      this.name = name;
      this.code = code;
      this.author = author;
      int[][] content = AsciiPacker.decode(code);
      this.width = content[0].length;
      this.height = content.length;
   }

   public String getCode() {
      return this.code;
   }

   public CharacterPlate getContent() {
      return new CharacterPlate(AsciiPacker.decode(this.code));
   }

   public Dimension getSize() {
      return new Dimension(this.width, this.height);
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public String getName() {
      return this.name;
   }

   public String getAuthor() {
      return this.author;
   }

   @Override
   public String toString() {
      return this.name + " (by " + this.author + "): " + this.code;
   }
}
