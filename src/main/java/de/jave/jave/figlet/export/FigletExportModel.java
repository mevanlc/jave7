package de.jave.jave.figlet.export;

import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterPlate;
import de.jave.lib.Toolbox;
import de.jave.text.TextTools;
import java.text.MessageFormat;
import java.util.HashSet;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.core.util.StringUtilities;

public class FigletExportModel extends AbstractChangeableModel {
   private final CharacterPlate characterPlate;
   private int characterWidth = 8;
   private int characterHeight = 5;
   private int characterDescent = 1;
   private int horizontalSpacing = 0;
   private int verticalSpacing = 0;
   private String characterArrangement = CharacterArrangement.ALL[0].getField();
   private int fullLayout;
   private int oldLayout;
   private static final String COMMENT_PATTERN = "Author : {0}\nDate   : {1}\nVersion: 1.0\n-------------------------------------------------\n\n-------------------------------------------------\nThis font has been created using JavE's FIGlet font export assistant.\nHave a look at: http://www.jave.de\n\nPermission is hereby given to modify this font, as long as the\nmodifier's name is placed on a comment line.";
   private String name = "";
   private String comment;

   public FigletExportModel(Plate plate, JaveApplicationPreferences preferences) {
      PlateDocument doc = plate.getDocument();
      this.characterPlate = doc.getContent();
      String authorText = this.getAuthorText(preferences);
      String dateText = Toolbox.getDateString();
      this.comment = MessageFormat.format(
         "Author : {0}\nDate   : {1}\nVersion: 1.0\n-------------------------------------------------\n\n-------------------------------------------------\nThis font has been created using JavE's FIGlet font export assistant.\nHave a look at: http://www.jave.de\n\nPermission is hereby given to modify this font, as long as the\nmodifier's name is placed on a comment line.",
         authorText,
         dateText
      );
   }

   private String getAuthorText(JaveApplicationPreferences preferences) {
      String authorName = preferences.getAuthorName();
      String authorEmail = preferences.getAuthorMail();
      if (StringUtilities.isNullOrTrimmedEmpty(authorName)) {
         return StringUtilities.isNullOrTrimmedEmpty(authorEmail) ? "" : authorEmail;
      } else {
         return StringUtilities.isNullOrTrimmedEmpty(authorEmail) ? authorName : authorName + " (" + authorEmail + ")";
      }
   }

   public CharacterPlate getCharacterPlate() {
      return this.characterPlate;
   }

   public int getCharacterDescent() {
      return this.characterDescent;
   }

   public void setCharacterDescent(int characterDescent) {
      if (this.characterDescent != characterDescent) {
         this.characterDescent = characterDescent;
         this.fireChangeEvent();
      }
   }

   public int getCharacterHeight() {
      return this.characterHeight;
   }

   public void setCharacterHeight(int characterHeight) {
      if (this.characterHeight != characterHeight) {
         this.characterHeight = characterHeight;
         this.fireChangeEvent();
      }
   }

   public int getCharacterWidth() {
      return this.characterWidth;
   }

   public void setCharacterWidth(int characterWidth) {
      if (this.characterWidth != characterWidth) {
         this.characterWidth = characterWidth;
         this.fireChangeEvent();
      }
   }

   public int getHorizontalSpacing() {
      return this.horizontalSpacing;
   }

   public void setHorizontalSpacing(int horizontalSpacing) {
      if (this.horizontalSpacing != horizontalSpacing) {
         this.horizontalSpacing = horizontalSpacing;
         this.fireChangeEvent();
      }
   }

   public int getVerticalSpacing() {
      return this.verticalSpacing;
   }

   public void setVerticalSpacing(int verticalSpacing) {
      if (this.verticalSpacing != verticalSpacing) {
         this.verticalSpacing = verticalSpacing;
         this.fireChangeEvent();
      }
   }

   public void setCharacterArrangement(String characterArrangement) {
      Ensure.ensureArgumentNotNull(characterArrangement);
      if (!this.characterArrangement.equals(characterArrangement)) {
         this.characterArrangement = characterArrangement;
         this.fireChangeEvent();
      }
   }

   public String getCharacterArrangement() {
      return this.characterArrangement;
   }

   public char[][] getRaster() {
      char[][] ch = TextTools.toCharField(this.characterArrangement);
      HashSet table = new HashSet();

      for (int y = 0; y < ch.length; y++) {
         for (int x = 0; x < ch[0].length; x++) {
            Character c = ch[y][x];
            if (table.contains(c)) {
               ch[y][x] = 0;
            } else {
               table.add(c);
            }
         }
      }

      return ch;
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

   public boolean canFinish() {
      return this.name.length() > 0;
   }

   public void setLayout(int oldLayout, int fullLayout) {
      if (this.oldLayout != oldLayout || this.fullLayout != fullLayout) {
         this.oldLayout = oldLayout;
         this.fullLayout = fullLayout;
         this.fireChangeEvent();
      }
   }

   public int getOldLayout() {
      return this.oldLayout;
   }

   public int getFullLayout() {
      return this.fullLayout;
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String comment) {
      Ensure.ensureArgumentNotNull(comment);
      if (!this.comment.equals(comment)) {
         this.comment = comment;
         this.fireChangeEvent();
      }
   }
}
