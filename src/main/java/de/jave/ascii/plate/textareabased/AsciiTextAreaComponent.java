package de.jave.ascii.plate.textareabased;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTextArea;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import net.disy.commons.core.model.listener.IChangeListener;

public class AsciiTextAreaComponent extends JTextArea {
   private final IBackgroundPainter backgroundPainter;

   public AsciiTextAreaComponent(Dimension size, final AsciiTextAreaProperties properties) {
      super(size.height, size.width);
      properties.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiTextAreaComponent.this.updateProperties(properties);
         }
      });
      this.updateProperties(properties);
      this.backgroundPainter = new IBackgroundPainter() {
         @Override
         public void paintBackground(Graphics g) {
            if (properties.getRulerProperties().isPrintMarginColumnVisible()) {
               g.setColor(properties.getRulerProperties().getForegroundColor());
               Rectangle clip = g.getClipBounds();
               int x = (int)(properties.getRulerProperties().getCharacterWidth() * (double)properties.getRulerProperties().getPrintMarginColumn());
               g.drawLine(x, clip.y, x, clip.y + clip.height);
            }
         }
      };
      super.setUI(new BasicTextAreaUI() {
         @Override
         protected void paintBackground(Graphics g) {
            super.paintBackground(g);
            AsciiTextAreaComponent.this.backgroundPainter.paintBackground(g);
         }
      });
   }

   @Override
   public void setUI(TextUI ui) {
   }

   private void updateProperties(AsciiTextAreaProperties properties) {
      this.setFont(properties.getFont());
      this.setEditable(properties.isEditable());
      this.setForeground(properties.getForegroundColor());
      this.setBackground(properties.getBackgroundColor());
   }
}
