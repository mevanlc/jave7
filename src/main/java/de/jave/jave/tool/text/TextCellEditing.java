package de.jave.jave.tool.text;

import de.jave.lib.CharacterPlate;
import de.jave.lib.cell.Cell;
import de.jave.lib.cell.GraphemeSplitter;
import java.awt.Point;
import java.text.Normalizer;

final class TextCellEditing {
   private TextCellEditing() {
   }

   /**
    * Enters one grapheme and reports whether it consumed the cursor cell.
    * Assimilators extend the preceding cell and therefore do not advance it.
    */
   static boolean enterGrapheme(CharacterPlate content, Point cursor, String text, boolean insert) {
      Cell incoming = Cell.fromText(text);
      if (cursor.x > 0 && content.glyphAt(cursor.x - 1, cursor.y) != ' ') {
         String combined = Normalizer.normalize(content.textAt(cursor.x - 1, cursor.y) + incoming.text(), Normalizer.Form.NFC);
         if (GraphemeSplitter.split(combined).size() == 1) {
            content.setText(cursor.x - 1, cursor.y, combined);
            return false;
         }
      }

      if (insert) {
         if (cursor.x == content.getWidth() - 1) {
            content.addColumnsRight(1);
         }
         TextRowEditing.insertRightward(content, cursor.x, cursor.y);
      }
      content.set(cursor.x, cursor.y, incoming.glyph());
      return true;
   }
}
