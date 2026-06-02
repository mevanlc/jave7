package de.jave.jave.tool.text;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.gui.xor.IXorPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.util.EventDispatchThreadUtilities;

public class JaveTextCursor implements IXorPainter {
   private static long counter = 0L;
   private final Point location;
   private final CharacterMetrics characterMetrics;
   private final TextCursorStyle style;
   private final long id;

   public JaveTextCursor(Point location, TextCursorStyle style, CharacterMetrics characterMetrics) {
      Ensure.ensureArgumentNotNull(location);
      Ensure.ensureArgumentNotNull(style);
      Ensure.ensureArgumentNotNull(characterMetrics);
      this.style = style;
      this.location = location;
      this.characterMetrics = characterMetrics;
      this.id = ++counter;
   }

   @Override
   public void paintXor(Graphics g) {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
      g.setColor(Color.black);
      g.setXORMode(Color.white);
      int charWidth = this.characterMetrics.getWidth();
      int charHeight = this.characterMetrics.getHeight();
      switch (this.style) {
         case BLOCK:
            g.fillRect(this.location.x, this.location.y, charWidth + 1, charHeight + 1);
            break;
         case HORIZONTAL_LINE:
            int y = this.location.y + charHeight - 2;
            g.drawLine(this.location.x + 1, y, this.location.x + 1 + charWidth - 2, y);
            break;
         case VERTICAL_LINE:
            g.drawLine(this.location.x + 2, this.location.y + charHeight - 2, this.location.x + 2, this.location.y + 2);
      }

      g.setPaintMode();
   }

   @Override
   public String toString() {
      return "TextCursor{" + this.id + "}";
   }
}
