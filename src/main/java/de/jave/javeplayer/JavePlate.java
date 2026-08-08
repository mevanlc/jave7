package de.jave.javeplayer;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.jave.rendering.GlyphRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;
import javax.swing.JComponent;

public class JavePlate extends JComponent {
   private int[][] content;
   private int[][] selection;
   private JaveAnimationFrame currentFrame;
   private int width;
   private int height;
   private int selectionWidth;
   private int selectionHeight;
   private CharacterMetrics characterMetrics = new CharacterMetrics(0, 0, 0);
   private boolean isRepainted;
   private String statusMessage = "* No Animation Loaded *";
   private static final Font MESSAGE_FONT = new Font("Dialog", 0, 11);
   private static final int DEFAULT_FONT_SIZE = 11;
   private static final int MIN_FONT_SIZE = 7;
   private static final int MAX_FONT_SIZE = 16;
   private int fontSize;

   public JavePlate() {
      this.setFontSize(11);
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
      this.repaint();
   }

   public void setContent(JaveAnimationFrame frame) {
      if (frame == null) {
         this.content = null;
         this.selection = null;
         this.currentFrame = frame;
      } else {
         this.content = frame.getContent();
         this.selection = frame.getSelection();
         this.height = this.content.length;
         if (this.height == 0) {
            this.width = 0;
         } else {
            this.width = this.content[0].length;
         }

         if (this.selection != null) {
            this.selectionHeight = this.selection.length;
            this.selectionWidth = this.selection[0].length;
         }

         this.currentFrame = frame;
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return this.width != 0 && this.height != 0
         ? new Dimension(this.width * this.characterMetrics.getWidth() + 4, this.height * this.characterMetrics.getHeight() + 4)
         : new Dimension(320, 250);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   private void setFontSize(int size) {
      if (size != this.fontSize) {
         this.fontSize = size;
         this.setFont(new Font("Monospaced", 0, this.fontSize));
         this.characterMetrics = CharacterMetrics.createCharacterMetrics(this.getFont());
         this.repaint();
      }
   }

   public void zoomIn() {
      if (this.fontSize < 16) {
         this.setFontSize(this.fontSize + 1);
      }
   }

   public void zoomOut() {
      if (this.fontSize > 7) {
         this.setFontSize(this.fontSize - 1);
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      g.setColor(this.getBackground());
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(1, 1, d.width - 1, 1);
      g.drawLine(1, 2, 1, d.height - 1);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(0, 0, d.width - 1, 0);
      g.drawLine(0, 1, 0, d.height - 1);
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(1, d.height - 2, d.width - 2, d.height - 2);
      g.drawLine(d.width - 2, d.height - 3, d.width - 2, 1);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(0, d.height - 1, d.width - 1, d.height - 1);
      g.drawLine(d.width - 1, d.height - 2, d.width - 1, 0);
      g.setColor(this.getForeground());
      g.setFont(this.getFont());
      if (this.content != null && this.content.length > 0) {
         int x0 = (d.width - this.content[0].length * this.characterMetrics.getWidth()) / 2;
         int y0 = (d.height - this.content.length * this.characterMetrics.getHeight()) / 2;
         if (x0 < 2) {
            x0 = 2;
         }

         if (y0 < 2) {
            y0 = 2;
         }

         for (int y = 0; y < this.height; y++) {
            GlyphRenderer.drawRow(
               g,
               this.content[y],
               x0,
               y0 + y * this.characterMetrics.getHeight() + this.characterMetrics.getHeight() * 3 / 4,
               this.characterMetrics.getWidth()
            );
         }

         if (this.selection != null) {
            Color fg = this.getForeground();
            Color bg = this.getBackground();
            g.setColor(new Color((fg.getRed() + bg.getRed()) / 2, (fg.getGreen() + bg.getGreen()) / 2, (fg.getBlue() + bg.getBlue()) / 2));
            int selectionX = this.currentFrame.getSelectionX();
            int selectionY = this.currentFrame.getSelectionY();

            for (int y = 0; y < this.selectionHeight; y++) {
               GlyphRenderer.drawRow(
                  g,
                  this.selection[y],
                  x0 + selectionX * this.characterMetrics.getWidth(),
                  y0 + (selectionY + y) * this.characterMetrics.getHeight() + this.characterMetrics.getHeight() * 3 / 4,
                  this.characterMetrics.getWidth()
               );
            }

            g.setColor(this.getBackground().darker());
            g.drawRect(
               x0 + selectionX * this.characterMetrics.getWidth(),
               y0 + selectionY * this.characterMetrics.getHeight(),
               this.selectionWidth * this.characterMetrics.getWidth(),
               this.selectionHeight * this.characterMetrics.getHeight()
            );
         }

         String tool = this.currentFrame.getTool();
         int cursorX = this.currentFrame.getCursorX();
         int cursorY = this.currentFrame.getCursorY();
         if (tool != null && tool.equals("Text") && cursorX >= 0 && cursorY >= 0) {
            g.setColor(Color.gray);
            g.drawLine(
               x0 + cursorX * this.characterMetrics.getWidth() + 2,
               y0 + cursorY * this.characterMetrics.getHeight() + this.characterMetrics.getHeight() - 2,
               x0 + cursorX * this.characterMetrics.getWidth() + 2,
               y0 + cursorY * this.characterMetrics.getHeight() + 2
            );
         }
      } else if (this.statusMessage != null && this.statusMessage.length() > 0) {
         g.setFont(MESSAGE_FONT);
         int w = this.getFontMetrics(MESSAGE_FONT).stringWidth(this.statusMessage);
         g.drawString(this.statusMessage, (d.width - w) / 2, d.height / 2 + 12);
      }

      this.isRepainted = true;
   }

   @Override
   public void repaint() {
      this.isRepainted = false;
      super.repaint();
   }

   public boolean isRepainted() {
      return this.isRepainted;
   }
}
