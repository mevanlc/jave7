package de.jave.gui;

import de.jave.lib.gui.IStatusDisplay;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import net.dizzy.commons.swing.color.SwingColors;

public class StatusBar extends JComponent implements IStatusDisplay {
   private String status = "";

   @Override
   public void showStatus(String statusText) {
      this.status = statusText;
      this.repaint();
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(200, 18);
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(100, 18);
   }

   @Override
   protected void paintComponent(Graphics g) {
      g.setColor(SwingColors.getTextAreaForegroundColor());
      g.drawString(this.status, 10, 13);
   }
}
