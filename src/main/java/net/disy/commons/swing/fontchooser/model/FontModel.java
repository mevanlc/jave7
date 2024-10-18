package net.disy.commons.swing.fontchooser.model;

import java.awt.Font;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.text.font.FontDescription;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.util.FontUtilities;

public class FontModel extends AbstractChangeableModel {
   public static final int MAX_FONT_SIZE = 200;
   public static final int MIN_FONT_SIZE = 2;
   private FontDescription fontDescription;

   public FontModel() {
      this(FontUtilities.getDefaultFont());
   }

   public FontModel(Font font) {
      this.setFont(font);
   }

   public FontModel(FontDescription fontDescription) {
      this.setFont(fontDescription);
   }

   public void setFont(Font font) {
      this.setFont(FontFactory.createFontDescription(font));
   }

   public void setFont(FontDescription fontDescription) {
      Ensure.ensureArgumentNotNull(fontDescription);
      this.fontDescription = fontDescription;
      this.fireChangeEvent();
   }

   public String getFontFamilyName() {
      return this.fontDescription.getFontFamilyName();
   }

   public int getFontSize() {
      return this.fontDescription.getFontSize();
   }

   public FontStyle getFontStyle() {
      return this.fontDescription.getFontStyle();
   }

   public void setFontFamilyName(String fontFamilyName) {
      Ensure.ensureArgumentNotNull(fontFamilyName);
      if (!ObjectUtilities.equals(this.fontDescription.getFontFamilyName(), fontFamilyName)) {
         this.fontDescription = this.fontDescription.deriveWithFontFamilyName(fontFamilyName);
         this.fireChangeEvent();
      }
   }

   public void setFontSize(int fontSize) {
      if (!FontUtilities.isValidFontSize(fontSize)) {
         throw new IllegalArgumentException("Illegal value '" + fontSize + "' for font size. Valid values are " + 2 + ".." + 200);
      } else if (this.fontDescription.getFontSize() != fontSize) {
         this.fontDescription = this.fontDescription.deriveWithFontSize(fontSize);
         this.fireChangeEvent();
      }
   }

   public void setFontStyle(FontStyle fontStyle) {
      Ensure.ensureArgumentNotNull(fontStyle);
      if (!ObjectUtilities.equals(this.fontDescription.getFontStyle(), fontStyle)) {
         this.fontDescription = this.fontDescription.deriveWithFontStyle(fontStyle);
         this.fireChangeEvent();
      }
   }

   public Font getFont() {
      return createFont(this.fontDescription);
   }

   public FontDescription getFontDescription() {
      return this.fontDescription;
   }

   public static Font createFont(FontDescription fontDescription) {
      return FontFactory.createFont(fontDescription.getFontFamilyName(), fontDescription.getFontStyle(), fontDescription.getFontSize());
   }
}
