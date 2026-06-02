package de.jave.jave.figlet.export;

import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.watermark.IWatermarkPainter;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import net.dizzy.commons.core.util.Ensure;

public class FigletExportWatermarkPainter implements IWatermarkPainter {
   private final FigletExportModel model;
   private boolean enabled = true;

   public FigletExportWatermarkPainter(FigletExportModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   public void paint(Graphics g, Point plateOrigin, ColorScheme colorScheme, int charWidth, int charHeight) {
      g.setFont(JaveGlobalRessources.FONT_SMALL);
      FontMetrics fm = g.getFontMetrics(JaveGlobalRessources.FONT_SMALL);
      int fontHeight = fm.getAscent();
      char[][] ch = this.model.getRaster();
      int figCharWidth = this.model.getCharacterWidth();
      int figCharHeight = this.model.getCharacterHeight();
      int figCharDescent = this.model.getCharacterDescent();
      int figCharVSpacing = this.model.getVerticalSpacing();
      int figCharHSpacing = this.model.getHorizontalSpacing();

      for (int y = 0; y < ch.length; y++) {
         for (int x = 0; x < ch[0].length; x++) {
            if (ch[y][x] != 0) {
               int xx = plateOrigin.x + x * figCharWidth * charWidth + figCharHSpacing * x * charWidth + 1;
               int yy = plateOrigin.y + y * figCharHeight * charHeight + figCharVSpacing * y * charHeight + 1;
               int ww = figCharWidth * charWidth - 2;
               int hh = figCharHeight * charHeight - 2;
               int y2 = yy + hh - figCharDescent * charHeight;
               g.setColor(colorScheme.getColorWatermarkFill());
               g.fillRect(xx, yy, ww, hh);
               g.setColor(colorScheme.getColorWatermark());
               g.drawRect(xx, yy, ww, hh);
               g.drawLine(xx, yy + 1, xx + ww, yy + 1);
               g.drawLine(xx, y2, xx + charWidth, y2);
               g.drawLine(xx + ww, y2, xx + ww - charWidth, y2);
               g.drawLine(xx + 1, y2, xx + 1, yy);
               g.drawLine(xx + ww - 1, y2, xx + ww - 1, yy);
               String text = String.valueOf(ch[y][x]);
               if (ch[y][x] == ' ') {
                  text = "[space]";
               }

               g.drawString(text, xx + 3, yy + fontHeight);
            }
         }
      }
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }
}
