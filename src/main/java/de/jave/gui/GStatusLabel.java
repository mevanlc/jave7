package de.jave.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import net.dizzy.commons.swing.color.SwingColors;

public class GStatusLabel extends JComponent {
   private static final int MINIMUM_WIDTH = 30;
   private String text;

   public GStatusLabel(String text, final IMouseClickHandler mouseClickHandler) {
      this.text = text;
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (mouseClickHandler != null) {
               mouseClickHandler.handleMouseClicked();
            }
         }
      });
   }

   @Override
   public Dimension getPreferredSize() {
      FontMetrics fm = this.getFontMetrics(this.getFont());
      int stringWidth = fm.stringWidth(this.text);
      int requiredWidth = stringWidth + 4;
      return new Dimension(Math.max(requiredWidth, 30), fm.getHeight());
   }

   @Override
   public void paint(Graphics g) {
      g.drawString(this.text, 2, this.getFontMetrics(this.getFont()).getAscent());
      Dimension d = this.getSize();
      g.setColor(SwingColors.getControlShadowColor());
      g.drawLine(0, 0, d.width, 0);
      g.drawLine(0, 0, 0, d.height);
      g.setColor(SwingColors.getControlHighlightColor());
      g.drawLine(d.width - 1, 0, d.width - 1, d.height);
      g.drawLine(1, d.height, d.width - 1, d.height);
   }

   public void setText(String text) {
      this.text = text;
      this.revalidate();
      this.repaint();
   }
}
