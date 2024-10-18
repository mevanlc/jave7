package net.disy.commons.swing.fontchooser.view.combos;

import java.util.Set;
import javax.swing.JComponent;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.button.FontStyleButton;

public class FontChooserComboComponents {
   private final FontFamilyCombo fontFamilyCombo;
   private final FontSizeCombo fontSizeCombo;
   private final FontStyleButton boldButton;
   private final FontStyleButton italicsButton;

   public FontChooserComboComponents(FontModel fontModel) {
      this(fontModel, new FontFamilyCombo(fontModel));
   }

   public FontChooserComboComponents(FontModel fontModel, Set<String> availableFontFamilies) {
      this(fontModel, new FontFamilyCombo(fontModel, availableFontFamilies));
   }

   public FontChooserComboComponents(FontModel fontModel, FontFamilyCombo fontFamilyCombo) {
      this.fontFamilyCombo = fontFamilyCombo;
      this.fontSizeCombo = new FontSizeCombo(fontModel);
      this.boldButton = FontStyleButton.createBoldButton(fontModel);
      this.italicsButton = FontStyleButton.createItalicsButton(fontModel);
   }

   public FontModel getFontModel() {
      return this.fontFamilyCombo.getFontModel();
   }

   public void setEnabled(boolean enabled) {
      this.fontFamilyCombo.setEnabled(enabled);
      this.fontSizeCombo.setEnabled(enabled);
      this.boldButton.setEnabled(enabled);
      this.italicsButton.setEnabled(enabled);
   }

   public boolean isEnabled() {
      return this.fontFamilyCombo.isEnabled();
   }

   public JComponent[] getComponents() {
      return new JComponent[]{
         this.fontFamilyCombo.getContent(), this.fontSizeCombo.getContent(), this.boldButton.getContent(), this.italicsButton.getContent()
      };
   }
}
