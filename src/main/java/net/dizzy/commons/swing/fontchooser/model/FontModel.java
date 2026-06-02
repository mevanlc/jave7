package net.dizzy.commons.swing.fontchooser.model;

import java.awt.Font;

import net.dizzy.commons.core.model.AbstractChangeableModel;

public class FontModel extends AbstractChangeableModel {
   private String fontName;
   private int fontStyle;
   private int fontSize;

   public FontModel(Font font) {
      this(font.getName(), font.getStyle(), font.getSize());
   }

   public FontModel() {
      this(new Font(Font.MONOSPACED, Font.PLAIN, 12));
   }

   public FontModel(FontDescription description) {
      this(description.name, description.style, description.size);
   }

   public FontModel(String fontName, int fontStyle, int fontSize) {
      this.fontName = fontName;
      this.fontStyle = fontStyle;
      this.fontSize = fontSize;
   }

   public Font getFont() {
      return new Font(fontName, fontStyle, fontSize);
   }

   public void setFont(Font font) {
      setFontName(font.getName());
      setFontStyle(font.getStyle());
      setFontSize(font.getSize());
   }

   public String getFontName() {
      return fontName;
   }

   public String getFontFamilyName() {
      return fontName;
   }

   public void setFontName(String fontName) {
      this.fontName = fontName;
      fireChangeEvent();
   }

   public int getFontStyle() {
      return fontStyle;
   }

   public void setFontStyle(int fontStyle) {
      this.fontStyle = fontStyle;
      fireChangeEvent();
   }

   public int getFontSize() {
      return fontSize;
   }

   public void setFontSize(int fontSize) {
      this.fontSize = fontSize;
      fireChangeEvent();
   }

   public FontDescription getFontDescription() {
      return new FontDescription(fontName, fontStyle, fontSize);
   }

   public static class FontDescription {
      public final String name;
      public final int style;
      public final int size;

      public FontDescription(String name, int style, int size) {
         this.name = name;
         this.style = style;
         this.size = size;
      }
   }
}
