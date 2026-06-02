package de.jave.jave.pattern;

import de.jave.jave.algorithm.compress.AsciiPacker;
import net.dizzy.commons.core.util.Ensure;

public class Pattern {
   private String name;
   private final String code;
   private String author;

   public Pattern(String code) {
      this("", code, "");
   }

   public Pattern(String name, String code, String author) {
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(author);
      Ensure.ensureArgumentNotNull(code);
      this.name = name;
      this.code = code;
      this.author = author;
   }

   public String getCode() {
      return this.code;
   }

   public char[][] getContent() {
      return AsciiPacker.decode(this.code);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   @Override
   public String toString() {
      return this.name + " (by " + this.author + "): " + this.code;
   }
}
