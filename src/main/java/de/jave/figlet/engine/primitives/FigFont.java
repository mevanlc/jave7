package de.jave.figlet.engine.primitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FigFont {
   public static final char HARDBLANK = '\u007f';
   private final Map<Integer, FigCharacter> characterMapping = new HashMap<>();
   private final List<FigCharacter> characters = new ArrayList<>();
   private final String name;
   private final FigFontOptions options;

   public FigFont(String name, FigFontOptions options) {
      this.options = options;
      this.name = name;
   }

   public void addCharacter(FigCharacter character) {
      this.characters.add(character);
      this.characterMapping.put(character.getCharacterCode(), character);
   }

   public String getComments() {
      return this.options.getComments();
   }

   public int getHeight() {
      return this.options.getHeight();
   }

   public int getMaxlength() {
      return this.options.getMaxLength();
   }

   public int getUnderLength() {
      return this.options.getHeight() - this.options.getBaseline();
   }

   public FigCharacter getFIGCharacter(int ch) {
      return this.characterMapping.containsKey(ch) ? this.characterMapping.get(ch) : this.characterMapping.get(32);
   }

   public FigLayout getLayout() {
      return this.options.getLayout();
   }

   public String getName() {
      return this.name;
   }

   public FigFontOptions getOptions() {
      return this.options;
   }
}
