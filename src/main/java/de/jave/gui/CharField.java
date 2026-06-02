package de.jave.gui;

import de.jave.gfx.GfxTools;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.UIManager;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.color.SwingColors;

public class CharField extends JComponent implements KeyListener, FocusListener, MouseListener {
   private static final int PREFERRED_WIDTH = 16;
   private static final int PREFERRED_HEIGHT = 20;
   private final CharacterModel model;
   private boolean focus = false;
   private boolean editable = true;
   private String historyString;
   private int historyIndex = -1;
   private final int HISTORY_MAX_LENGTH = 50;

   public CharField(CharacterModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.addKeyListener(this);
      this.addFocusListener(this);
      this.addMouseListener(this);
      this.historyAdd(model.getCharacter());
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            CharField.this.repaint();
         }
      });
      this.setBorder(UIManager.getBorder("TextField.border"));
   }

   public CharacterModel getModel() {
      return this.model;
   }

   public void setEditable(boolean editable) {
      if (this.editable != editable) {
         this.editable = editable;
         this.repaint();
      }
   }

   public boolean isEditable() {
      return this.editable;
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(16, 20);
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(16, 20);
   }

   @Override
   public Dimension getMaximumSize() {
      return new Dimension(16, 20);
   }

   @Override
   protected void paintComponent(Graphics g) {
      Dimension d = this.getSize();
      g.setColor(SwingColors.getTextAreaBackgroundColor());
      g.fillRect(0, 0, d.width, d.height);
      if (this.isEnabled()) {
         g.setColor(SwingColors.getTextAreaForegroundColor());
         this.draw(g, d);
      } else {
         g.setColor(SwingColors.getTextAreaInactiveForegroundColor());
         this.draw(g, d);
      }

      if (this.focus && this.isEnabled()) {
         g.setColor(SwingColors.getTextAreaForegroundColor());
         GfxTools.drawDottedRectangle(g, 3, 3, d.width - 7, d.height - 7);
      }
   }

   protected final void draw(Graphics g, Dimension d) {
      if (this.model.getCharacter() == 160) {
         g.drawLine(2, 2, d.width - 3, d.height - 3);
         g.drawLine(d.width - 3, 2, 2, d.height - 3);
      } else {
         g.drawString(String.valueOf(this.model.getCharacter()), 5 + (d.width - 16) / 2, 15);
      }
   }

   @Override
   public boolean isFocusTraversable() {
      return true;
   }

   @Override
   public void mousePressed(MouseEvent e) {
      this.requestFocus();
   }

   @Override
   public void mouseReleased(MouseEvent e) {
   }

   @Override
   public void mouseClicked(MouseEvent e) {
   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (this.editable) {
         int code = e.getKeyCode();
         if (code != 9) {
            if (code == 39 || code == 40) {
               this.historyForward();
            } else if (code != 37 && code != 38) {
               char lastChar = e.getKeyChar();
               if (lastChar >= ' ' && lastChar <= 255 && (lastChar <= '~' || lastChar >= 145)) {
                  if (lastChar == ' ' && e.isShiftDown()) {
                     lastChar = 160;
                  }

                  if (this.model.getCharacter() != lastChar) {
                     this.model.setCharacter(lastChar);
                     this.historyAdd(lastChar);
                  }
               }
            } else {
               this.historyBackward();
            }
         }
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   private synchronized void historyForward() {
      if (this.historyString.length() > this.historyIndex + 1) {
         this.historyIndex++;
         this.model.setCharacter(this.historyString.charAt(this.historyIndex));
      }
   }

   private synchronized void historyBackward() {
      if (this.historyIndex >= 1) {
         this.historyIndex--;
         this.model.setCharacter(this.historyString.charAt(this.historyIndex));
      }
   }

   private synchronized void historyAdd(char ch) {
      if (this.historyString != null && this.historyIndex != -1) {
         if (this.historyString.charAt(this.historyIndex) != ch) {
            this.historyString = this.historyString.substring(0, this.historyIndex + 1) + String.valueOf(ch);
            this.historyIndex++;
            if (this.historyIndex + 1 > 50) {
               this.historyString = this.historyString.substring(1);
               this.historyIndex--;
            }
         }
      } else {
         this.historyString = String.valueOf(ch);
         this.historyIndex = 0;
      }
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

   public void copy() {
      char ch = this.model.getCharacter();
      StringSelection sel = new StringSelection(String.valueOf(ch));
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
   }

   public void cut() {
      if (!this.editable || !this.isEnabled()) {
         return;
      }
      this.copy();
      if (this.model.getCharacter() != ' ') {
         this.model.setCharacter(' ');
         this.historyAdd(' ');
      }
   }

   public void paste() {
      if (!this.editable || !this.isEnabled()) {
         return;
      }
      try {
         Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
         if (t == null || !t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return;
         }
         String s = (String)t.getTransferData(DataFlavor.stringFlavor);
         if (s == null || s.isEmpty()) {
            return;
         }
         char ch = s.charAt(0);
         if (ch < ' ') {
            return;
         }
         if (this.model.getCharacter() != ch) {
            this.model.setCharacter(ch);
            this.historyAdd(ch);
         }
      } catch (Exception ignored) {
      }
   }
}
