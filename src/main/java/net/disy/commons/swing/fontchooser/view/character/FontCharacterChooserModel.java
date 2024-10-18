package net.disy.commons.swing.fontchooser.view.character;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class FontCharacterChooserModel extends AbstractChangeableModel {
   private char character;
   private final FontModel fontModel;

   public FontCharacterChooserModel(FontModel fontModel, char character) {
      this.fontModel = fontModel;
      this.character = character;
   }

   public FontModel getFontModel() {
      return this.fontModel;
   }

   public char getCharacter() {
      return this.character;
   }

   public void setCharacter(char character) {
      if (this.character != character) {
         this.character = character;
         this.fireChangeEvent();
      }
   }
}
