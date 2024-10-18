package de.jave.jave.rendering;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.Plate;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics;
import java.awt.Point;

public class PixelPlateRenderer {
   public static void paint(Graphics g, Plate target, ColorScheme colorScheme, LocatedCharacterPlate characterPlate, Point plateOrigin) {
      CharacterMetrics characterMetrics = target.getCharacterMetrics();
      int charWidth = characterMetrics.getWidth();
      int charHeight = characterMetrics.getHeight();
      int fontAscent = characterMetrics.getAscent();
      int x1 = characterPlate.getOriginX() * charWidth + plateOrigin.x;
      int y1 = characterPlate.getOriginY() * charHeight + plateOrigin.y;
      g.setColor(colorScheme.getColorToolPreview());
      CharacterPlate displayResult = characterPlate.getClone();
      displayResult.replace('\u0000', ' ');
      displayResult.replace(' ', ' ');
      target.paintPreview(g, displayResult, colorScheme, 0, 0, characterPlate.getOriginX(), characterPlate.getOriginY());
      int plateWidth = target.getDocumentWidth();
      int plateHeight = target.getDocumentHeight();
      g.setColor(colorScheme.getColorToolPreviewDelete());

      for (int y = 0; y < characterPlate.getHeight(); y++) {
         for (int x = 0; x < characterPlate.getWidth(); x++) {
            if (characterPlate.get(x, y) == ' ' || characterPlate.get(x, y) == 160) {
               int xx = characterPlate.getOriginX() + x;
               int yy = characterPlate.getOriginY() + y;
               if (xx >= 0 && yy >= 0 && xx < plateWidth && yy < plateHeight) {
                  char ch = target.getChar(xx, yy);
                  if (ch != ' ') {
                     g.drawString(String.valueOf(ch), x1 + x * charWidth, y1 + fontAscent + y * charHeight);
                  }
               }
            }
         }
      }
   }
}
