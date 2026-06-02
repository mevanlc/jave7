package de.jave.jave.clipart;

import de.jave.lib.CharacterPlate;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;

public class ClipartNewEntryModel extends AbstractChangeableModel {
   private final CharacterPlate code;
   private String name = "";
   private String author = "";

   public ClipartNewEntryModel(CharacterPlate code) {
      Ensure.ensureArgumentNotNull(code);
      this.code = code;
   }

   public CharacterPlate getCode() {
      return this.code;
   }

   public void setAuthor(String author) {
      Ensure.ensureArgumentNotNull(author);
      if (!this.author.equals(author)) {
         this.author = author;
         this.fireChangeEvent();
      }
   }

   public String getAuthor() {
      return this.author;
   }

   public void setName(String name) {
      Ensure.ensureArgumentNotNull(name);
      if (!this.name.equals(name)) {
         this.name = name;
         this.fireChangeEvent();
      }
   }

   public String getName() {
      return this.name;
   }
}
