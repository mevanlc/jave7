package de.jave.jave.tool.text;

import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.Plate;
import de.jave.jave.plate.ActiveEditorModel;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.preferences.BooleanPreferenceModel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class CursorBlinker implements FocusListener {
   private boolean active;
   private boolean cursorShowing;
   private Plate currentPlate;
   private final BooleanPreferenceModel cursorBlockStyle;
   private boolean hasSelection = false;
   private boolean insert = false;
   private final Timer timer;

   public CursorBlinker(final ActiveEditorModel activePlateModel, BooleanPreferenceModel cursorBlockStyle) {
      Ensure.ensureArgumentNotNull(activePlateModel);
      Ensure.ensureArgumentNotNull(cursorBlockStyle);
      this.cursorBlockStyle = cursorBlockStyle;
      this.timer = new Timer(
         JaveGlobalRessources.cursorBlinkPause,
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (CursorBlinker.this.currentPlate != null
                  && CursorBlinker.this.active
                  && CursorBlinker.this.currentPlate.isFocusOwner()
                  && !CursorBlinker.this.hasSelection) {
                  if (CursorBlinker.this.cursorShowing) {
                     if (JaveGlobalRessources.cursorBlink) {
                        CursorBlinker.this.removeCursor();
                     }
                  } else {
                     CursorBlinker.this.putCursor();
                  }
               }
            }
         }
      );
      this.timer.start();
      this.timer.setInitialDelay(0);
      activePlateModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            synchronized (this) {
               if (CursorBlinker.this.currentPlate != null) {
                  CursorBlinker.this.currentPlate.removeFocusListener(CursorBlinker.this);
                  if (CursorBlinker.this.active) {
                     CursorBlinker.this.currentPlate.setXORPainter(null);
                  }
               }

               IDocumentEditor activeEditor = activePlateModel.getActiveEditor();
               if (activeEditor != null) {
                  Plate plate = activeEditor.getPlate();
                  plate.addFocusListener(CursorBlinker.this);
                  CursorBlinker.this.currentPlate = plate;
               } else {
                  CursorBlinker.this.currentPlate = null;
               }
            }
         }
      });
   }

   @Override
   public void focusGained(FocusEvent evt) {
      this.timer.restart();
   }

   @Override
   public void focusLost(FocusEvent evt) {
      this.removeCursor();
   }

   public synchronized void setActive(boolean active) {
      this.active = active;
      if (active) {
         this.putCursor();
      } else {
         this.removeCursor();
      }
   }

   private synchronized void putCursor() {
      if (this.currentPlate != null) {
         Point cursorLocation = this.currentPlate.getDocument().getCursorLocation();
         if (cursorLocation == null) {
            this.removeCursor();
         } else {
            TextCursorStyle style = TextCursorStyle.HORIZONTAL_LINE;
            if (this.insert) {
               style = TextCursorStyle.VERTICAL_LINE;
            } else if (this.cursorBlockStyle.getValue()) {
               style = TextCursorStyle.BLOCK;
            }

            final JaveTextCursor newPainter = new JaveTextCursor(
               this.currentPlate.getScreenPointFor(cursorLocation), style, this.currentPlate.getCharacterMetrics()
            );
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  CursorBlinker.this.currentPlate.setXORPainter(newPainter);
                  CursorBlinker.this.cursorShowing = true;
               }
            });
         }
      }
   }

   private synchronized void removeCursor() {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (CursorBlinker.this.currentPlate != null) {
               CursorBlinker.this.currentPlate.setXORPainter(null);
            }

            CursorBlinker.this.cursorShowing = false;
         }
      });
   }

   public synchronized void updateCursor() {
      this.removeCursor();
      this.timer.restart();
   }

   public void setHasSelection(boolean hasSelection) {
      this.hasSelection = hasSelection;
   }

   public void setInsert(boolean insert) {
      this.insert = insert;
   }
}
