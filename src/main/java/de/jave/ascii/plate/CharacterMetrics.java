package de.jave.ascii.plate;

import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JOptionPane;

public class CharacterMetrics {
   private final int width;
   private final int height;
   private final int ascent;

   public CharacterMetrics(int width, int height, int ascent) {
      this.width = width;
      this.height = height;
      this.ascent = ascent;
   }

   public int getAscent() {
      return this.ascent;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public static CharacterMetrics createCharacterMetrics(Font font) {
      FontMetrics fontMetrics = JOptionPane.getRootFrame().getFontMetrics(font);
      int charWidth = fontMetrics.stringWidth("#");
      int charHeight = (int)Math.round(-0.14 * (double)font.getSize() * (double)font.getSize() + 4.56 * (double)font.getSize() - 19.3);
      return new CharacterMetrics(charWidth, charHeight, charHeight - fontMetrics.getDescent());
   }
}
