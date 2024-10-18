package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SwingUtilities;

public abstract class JaveGame implements KeyListener, DocumentListener {
   protected JavEApplication jave;
   protected CharacterPlate plate;
   protected PlateDocument document;
   protected boolean enabled;
   private final Plate javePlate;

   public JaveGame(JavEApplication jave) {
      this.jave = jave;
      this.enabled = true;
      this.plate = new CharacterPlate(this.getPreferredSize());
      this.javePlate = jave.pasteAsNewGameDocument(this.plate, this.getTitle());
      this.document = this.javePlate.getDocument();
      this.document.addDocumentListener(this);
      this.document.setModified(false);
      jave.performSetColorScheme(this.getPreferredColorScheme());
      this.javePlate.addKeyListener(this);
      jave.switchToSelectonTool();
      this.requestFocus();
   }

   protected abstract Dimension getPreferredSize();

   protected abstract String getTitle();

   protected abstract ColorScheme getPreferredColorScheme();

   protected abstract void keyPressed(KeyEvent var1, int var2, char var3);

   protected abstract void keyTyped(KeyEvent var1, int var2, char var3);

   public void quit() {
      this.javePlate.removeKeyListener(this);
      this.document.setModified(false);
      this.jave.doClose(this.jave.getFrame());
   }

   protected void requestFocus() {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            JaveGame.this.javePlate.requestFocus();
         }
      });
   }

   protected void repaint() {
      this.javePlate.repaint();
   }

   @Override
   public void keyReleased(KeyEvent evt) {
   }

   @Override
   public void keyPressed(KeyEvent evt) {
      if (this.enabled) {
         this.keyPressed(evt, evt.getKeyCode(), evt.getKeyChar());
      }
   }

   @Override
   public void keyTyped(KeyEvent evt) {
      if (this.enabled) {
         this.keyTyped(evt, evt.getKeyCode(), evt.getKeyChar());
      }
   }

   @Override
   public void documentClosing() {
      this.javePlate.removeKeyListener(this);
      this.document.setModified(false);
   }

   @Override
   public void documentHiding() {
      this.enabled = false;
   }

   @Override
   public void documentShowing() {
      this.requestFocus();
      this.jave.switchToSelectonTool();
      this.enabled = true;
   }

   @Override
   public void documentChanged() {
   }
}
