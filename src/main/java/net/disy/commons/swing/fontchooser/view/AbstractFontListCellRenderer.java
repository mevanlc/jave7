package net.disy.commons.swing.fontchooser.view;

import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import net.disy.commons.swing.fontchooser.util.FontUtilities;

public abstract class AbstractFontListCellRenderer extends DefaultListCellRenderer {
   private final IFontDialogProperties properties;

   public AbstractFontListCellRenderer(IFontDialogProperties properties) {
      this.properties = properties;
   }

   protected Font getDisplayFont(Font font) {
      return this.properties.isDisplaySelectedFontInControlsEnabled() && !FontUtilities.isSymbolFont(font)
         ? font
         : FontUtilities.getDefaultFont().deriveFont(font.getStyle(), (float)font.getSize());
   }
}
