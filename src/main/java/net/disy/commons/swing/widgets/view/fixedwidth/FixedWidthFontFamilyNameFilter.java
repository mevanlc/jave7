package net.disy.commons.swing.fontchooser.view.fixedwidth;

import java.awt.Font;
import net.disy.commons.swing.fontchooser.model.IFontFamilyNameFilter;
import net.disy.commons.swing.fontchooser.util.FontUtilities;

public class FixedWidthFontFamilyNameFilter implements IFontFamilyNameFilter {
   private boolean hideSymbolFonts;
   private boolean fixedWidthOnly;

   public FixedWidthFontFamilyNameFilter(boolean fixedWidthOnly, boolean hideSymbolFonts) {
      this.fixedWidthOnly = fixedWidthOnly;
      this.hideSymbolFonts = hideSymbolFonts;
   }

   @Override
   public boolean accept(String fontFamilyName) {
      if (!this.hideSymbolFonts && !this.fixedWidthOnly) {
         return true;
      } else {
         Font font = new Font(fontFamilyName, 0, 12);
         return this.fixedWidthOnly && !FontUtilities.isFixedWidth(font) ? false : !this.hideSymbolFonts || !FontUtilities.isSymbolFont(font);
      }
   }
}
