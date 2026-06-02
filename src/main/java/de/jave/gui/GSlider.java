package de.jave.gui;

import de.jave.gfx.GfxTools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class GSlider extends JComponent implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
   private final SpinnerNumberModel model;
   private boolean focus = false;
   private boolean mouseSlides;
   private int oldX;
   private int oldXValue;

   public GSlider(SpinnerNumberModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.model.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            GSlider.this.repaint(50L);
         }
      });
      this.addKeyListener(this);
      this.addFocusListener(this);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.mouseSlides = false;
   }

   private int getMinimum() {
      return ((Number)this.model.getMinimum()).intValue();
   }

   private int getMaximum() {
      return ((Number)this.model.getMaximum()).intValue();
   }

   private void inc() {
      Object nextValue = this.model.getNextValue();
      if (nextValue != null) {
         this.model.setValue(nextValue);
      }
   }

   private void dec() {
      Object previousValue = this.model.getPreviousValue();
      if (previousValue != null) {
         this.model.setValue(previousValue);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(99, 35);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      boolean enabled = this.isEnabled();
      int x1 = 10;
      int x2 = 87;
      int y = 23;
      if (enabled) {
         g.setColor(SystemColor.controlDkShadow);
      } else {
         g.setColor(SystemColor.controlShadow);
      }

      g.drawLine(x1, 23, x2, 23);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(x1 - 1, 25, x2 + 2, 25);
      g.drawLine(x2 + 2, 22, x2 + 2, 24);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(x1 - 1, 24, x1 - 1, 23);
      g.drawLine(x1 - 1, 22, x2 + 1, 22);
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(x1, 24, x2, 24);
      g.drawLine(x2 + 1, 23, x2 + 1, 24);
      if (enabled) {
         g.setColor(SystemColor.controlText);
         drawScale(g, 0, 0);
      } else {
         g.setColor(SystemColor.controlLtHighlight);
         drawScale(g, 1, 1);
         g.setColor(SystemColor.controlShadow);
         drawScale(g, 0, 0);
      }

      int x = this.computeSliderXFromValue(this.model.getNumber().intValue());
      x1 = x - 5;
      x2 = x + 5;
      int y1 = 12;
      int y2 = 17;
      int y3 = 31;
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(x, 12, x2, 17);
      g.drawLine(x2, 17, x2, 31);
      g.drawLine(x1, 31, x2, 31);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(x - 1, 13, x1, 17);
      g.drawLine(x1, 17, x1, 30);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(x, 13, x2 - 1, 17);
      g.drawLine(x2 - 1, 17, x2 - 1, 30);
      g.drawLine(x1 + 1, 30, x2 - 1, 30);
      g.setColor(SystemColor.controlHighlight);
      g.drawLine(x - 1, 14, x1 + 1, 17);
      g.drawLine(x1 + 1, 17, x1 + 1, 29);
      g.setColor(SystemColor.control);
      int[] xs = new int[]{x, x2 - 2, x2 - 2, x1 + 2, x1 + 2};
      int[] ys = new int[]{14, 17, 29, 29, 17};
      g.fillPolygon(xs, ys, xs.length);
      g.drawPolygon(xs, ys, xs.length);
      if (this.focus) {
         g.setColor(Color.black);
         GfxTools.drawDottedRectangle(g, 1, 1, d.width - 3, d.height - 3);
      }
   }

   private static final void drawScale(Graphics g, int dx, int dy) {
      int y = 10 + dy;

      for (int i = 0; i <= 10; i++) {
         int x = i * 7 + 14 + dx;
         if (i % 5 == 0) {
            g.drawLine(x, y, x, y - 4);
         } else {
            g.drawLine(x, y, x, y - 3);
         }
      }
   }

   private int computeSliderXFromValue(int v) {
      return 14 + 70 * (v - this.getMinimum()) / (this.getMaximum() - this.getMinimum());
   }

   private int computeValueFromSliderX(int x) {
      return (x - 14) * (this.getMaximum() - this.getMinimum()) / 70 + this.getMinimum();
   }

   @Override
   public boolean isFocusTraversable() {
      return true;
   }

   @Override
   public void mousePressed(MouseEvent evt) {
      this.requestFocus();
      Point p = evt.getPoint();
      if (p.y <= 31 && p.y >= 13) {
         int x = this.computeSliderXFromValue(this.model.getNumber().intValue());
         if (p.x <= x + 5 && p.x >= x - 5) {
            this.mouseSlides = true;
            this.oldX = p.x;
            this.oldXValue = this.computeSliderXFromValue(this.model.getNumber().intValue());
         }
      }
   }

   @Override
   public void mouseReleased(MouseEvent evt) {
      this.mouseSlides = false;
   }

   @Override
   public void mouseClicked(MouseEvent evt) {
   }

   @Override
   public void mouseEntered(MouseEvent evt) {
   }

   @Override
   public void mouseExited(MouseEvent evt) {
   }

   @Override
   public void mouseMoved(MouseEvent evt) {
   }

   @Override
   public void mouseDragged(MouseEvent evt) {
      if (this.mouseSlides) {
         int newX = evt.getPoint().x;
         int dx = newX - this.oldX;
         if (dx != 0) {
            int newXValue = this.oldXValue + dx;
            int newValue = this.computeValueFromSliderX(newXValue);
            int minValue = ((Number)this.model.getMinimum()).intValue();
            if (newValue < minValue) {
               newValue = minValue;
            }

            int maxValue = ((Number)this.model.getMaximum()).intValue();
            if (newValue > maxValue) {
               newValue = maxValue;
            }

            this.model.setValue(newValue);
            this.oldXValue = newXValue;
            this.oldX = newX;
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      int code = e.getKeyCode();
      if (code == 39 || code == 38) {
         this.inc();
      } else if (code == 37 || code == 40) {
         this.dec();
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void focusGained(FocusEvent evt) {
      this.focus = true;
      this.repaint();
   }

   @Override
   public void focusLost(FocusEvent evt) {
      this.focus = false;
      this.repaint();
   }
}
