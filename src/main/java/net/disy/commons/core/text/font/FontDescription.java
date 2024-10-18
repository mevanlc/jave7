package net.disy.commons.core.text.font;

import net.disy.commons.core.util.Ensure;

public class FontDescription {
   private String fontFamilyName;
   private FontStyle fontStyle;
   private int fontSize;

   public FontDescription(String fontFamilyName, FontStyle fontStyle, int fontSize) {
      Ensure.ensureArgumentNotNull(fontFamilyName);
      Ensure.ensureArgumentNotNull(fontStyle);
      this.fontFamilyName = fontFamilyName;
      this.fontStyle = fontStyle;
      this.fontSize = fontSize;
   }

   public final String getFontFamilyName() {
      return this.fontFamilyName;
   }

   public final int getFontSize() {
      return this.fontSize;
   }

   public final FontStyle getFontStyle() {
      return this.fontStyle;
   }

   @Deprecated
   public void setFontFamilyName(String fontFamilyName) {
      this.fontFamilyName = fontFamilyName;
   }

   public FontDescription deriveWithFontFamilyName(String newFontFamilyName) {
      return new FontDescription(newFontFamilyName, this.fontStyle, this.fontSize);
   }

   @Deprecated
   public final void setFontSize(int fontSize) {
      this.fontSize = fontSize;
   }

   public FontDescription deriveWithFontSize(int newFontSize) {
      return new FontDescription(this.fontFamilyName, this.fontStyle, newFontSize);
   }

   @Deprecated
   public final void setFontStyle(FontStyle fontStyle) {
      this.fontStyle = fontStyle;
   }

   public FontDescription deriveWithFontStyle(FontStyle newFontStyle) {
      return new FontDescription(this.fontFamilyName, newFontStyle, this.fontSize);
   }

   @Override
   public boolean equals(Object object) {
      if (!(object instanceof FontDescription)) {
         return false;
      } else {
         FontDescription other = (FontDescription)object;
         return other.fontFamilyName.equals(this.fontFamilyName) && other.fontSize == this.fontSize && other.fontStyle == this.fontStyle;
      }
   }

   @Override
   public int hashCode() {
      return 31 * (this.fontFamilyName.hashCode() + 31 * this.fontSize) + this.fontStyle.hashCode();
   }

   @Override
   public String toString() {
      return this.getClass().getName() + "{" + this.fontFamilyName + ", " + this.fontSize + ", " + this.fontStyle + "}";
   }
}
