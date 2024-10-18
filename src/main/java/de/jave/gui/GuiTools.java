package de.jave.gui;

import java.awt.Graphics;
import net.disy.commons.swing.color.SwingColors;

public class GuiTools {
   private GuiTools() {
   }

   public static void drawSmall3dRectangleUp(Graphics g, int x, int y, int w, int h) {
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(x, y, x + w - 2, y);
      g.drawLine(x, y + 1, x, y + h - 2);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      g.drawLine(x + w - 1, y + h - 1, x, y + h - 1);
   }

   public static void drawBorderDown(Graphics g, int x, int y, int w, int h) {
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
      g.drawLine(x + 1, y + 2, x + 1, y + h - 3);
      g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawRect(x, y, w - 2, h - 2);
   }

   public static void drawSmall3dRectangleDown(Graphics g, int x, int y, int w, int h) {
      g.setColor(SwingColors.getControlLtHighlightColor());
      g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
      g.drawLine(x + w - 2, y + h - 1, x, y + h - 1);
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(x, y, x + w - 2, y);
      g.drawLine(x, y + 1, x, y + h - 2);
   }
}
