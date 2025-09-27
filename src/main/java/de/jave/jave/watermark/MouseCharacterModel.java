package de.jave.jave.plate;

import net.disy.commons.core.model.AbstractChangeableModel;

public class MouseCharacterModel extends AbstractChangeableModel {
   private char character1 = 'X';
   private char character2 = ' ';

   public void setCharacter1(char character1) {
      if (this.character1 != character1) {
         this.character1 = character1;
         this.fireChangeEvent();
      }
   }

   public void setCharacter2(char character2) {
      if (this.character2 != character2) {
         this.character2 = character2;
         this.fireChangeEvent();
      }
   }

   public char getCharacter1() {
      return this.character1;
   }

   public char getCharacter2() {
      return this.character2;
   }

   public char getCharacter(boolean right) {
      return right ? this.getCharacter2() : this.getCharacter1();
   }
}
