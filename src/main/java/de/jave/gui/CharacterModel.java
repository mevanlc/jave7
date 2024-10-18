package de.jave.gui;

import net.disy.commons.core.model.AbstractChangeableModel;

public class CharacterModel extends AbstractChangeableModel {
   private char character = ' ';

   public CharacterModel() {
   }

   public CharacterModel(char character) {
      this.setCharacter(character);
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
