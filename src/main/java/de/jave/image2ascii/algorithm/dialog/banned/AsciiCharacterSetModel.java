package de.jave.image2ascii.algorithm.dialog.banned;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.disy.commons.core.model.AbstractChangeableModel;

public class AsciiCharacterSetModel extends AbstractChangeableModel {
   private String selectedCharacters = "";

   public AsciiCharacterSetModel() {
   }

   public AsciiCharacterSetModel(String characters) {
      this.setSelectedCharacters(characters);
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof AsciiCharacterSetModel)) {
         return false;
      } else {
         AsciiCharacterSetModel other = (AsciiCharacterSetModel)obj;
         return this.selectedCharacters.equals(other.selectedCharacters);
      }
   }

   public void setSelectedCharacters(String selectedCharacters) {
      if (!isEquivalent(this.selectedCharacters, selectedCharacters)) {
         this.selectedCharacters = selectedCharacters;
         this.fireChangeEvent();
      }
   }

   public static boolean isEquivalent(String characters1, String characters2) {
      Set set1 = new HashSet();

      for (int i = 0; i < characters1.length(); i++) {
         set1.add(characters1.charAt(i));
      }

      Set set2 = new HashSet();

      for (int i = 0; i < characters2.length(); i++) {
         set2.add(characters2.charAt(i));
      }

      return set1.equals(set2);
   }

   public String getSelectedCharacters() {
      return this.selectedCharacters;
   }

   public boolean isSelected(char character) {
      for (int i = 0; i < this.selectedCharacters.length(); i++) {
         if (character == this.selectedCharacters.charAt(i)) {
            return true;
         }
      }

      return false;
   }

   public String getNotSelectedCharacters() {
      StringBuffer sb = new StringBuffer();

      for (char character = ' '; character < 127; character++) {
         if (!this.isSelected(character)) {
            sb.append(character);
         }
      }

      return sb.toString();
   }

   public void setSelected(char character, boolean selected) {
      if (selected != this.isSelected(character)) {
         if (selected) {
            this.setSelectedCharacters(getSorted(this.selectedCharacters + character));
         } else {
            int index = this.selectedCharacters.indexOf(character);
            this.setSelectedCharacters(getSorted(this.selectedCharacters.substring(0, index) + this.selectedCharacters.substring(index + 1)));
         }
      }
   }

   private static String getSorted(String string) {
      char[] chars = string.toCharArray();
      Arrays.sort(chars);
      return new String(chars);
   }

   public void clear() {
      this.setSelectedCharacters("");
   }

   public void selectAll() {
      this.setUnselectedCharacters("");
   }

   public void setUnselectedCharacters(String unselected) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < 95; i++) {
         char character = (char)(i + 32);
         if (unselected.indexOf(character) == -1) {
            sb.append(character);
         }
      }

      this.setSelectedCharacters(sb.toString());
   }
}
