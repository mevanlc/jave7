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
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class CursorBlinker implements FocusListener {
   private boolean active;
   private boolean cursorShowing;
   private Plate currentPlate;
   private final BooleanPreferenceModel cursorBlockStyle;
   private boolean hasSelection = false;
   private boolean insert = false;
   private final Timer timer;
   private final Runnable putCursorAction = this::putCursorOnEventDispatchThread;
   private final Runnable removeCursorAction = this::removeCursorOnEventDispatchThread;
   private final Runnable updateCursorAction = this::updateCursorOnEventDispatchThread;

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
      this.updateCursor();
   }

   @Override
   public void focusLost(FocusEvent evt) {
      this.removeCursor();
   }

   public synchronized void setActive(boolean active) {
      this.active = active;
      if (active) {
         this.updateCursor();
      } else {
         this.removeCursor();
      }
   }

   private synchronized void putCursor() {
      runOnEventDispatchThread(this.putCursorAction);
   }

   private void putCursorOnEventDispatchThread() {
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

            JaveTextCursor newPainter = new JaveTextCursor(
               this.currentPlate.getScreenPointFor(cursorLocation), style, this.currentPlate.getCharacterMetrics()
            );
            this.currentPlate.setXORPainter(newPainter);
            this.cursorShowing = true;
         }
      }
   }

   private synchronized void removeCursor() {
      runOnEventDispatchThread(this.removeCursorAction);
   }

   private void removeCursorOnEventDispatchThread() {
      if (this.currentPlate != null) {
         this.currentPlate.setXORPainter(null);
      }
      this.cursorShowing = false;
   }

   public synchronized void updateCursor() {
      runOnEventDispatchThread(this.updateCursorAction);
   }

   private void updateCursorOnEventDispatchThread() {
      if (this.currentPlate != null && this.active && this.currentPlate.isFocusOwner() && !this.hasSelection) {
         // Replace the old cursor in the same EDT turn, without a queued hidden phase.
         this.putCursor();
         // Keep the cursor visible for a full blink interval after the latest movement.
         this.timer.restart();
      } else {
         this.removeCursor();
      }
   }

   private static void runOnEventDispatchThread(Runnable action) {
      if (SwingUtilities.isEventDispatchThread()) {
         action.run();
      } else {
         SwingUtilities.invokeLater(action);
      }
   }

   public void setHasSelection(boolean hasSelection) {
      this.hasSelection = hasSelection;
   }

   public void setInsert(boolean insert) {
      this.insert = insert;
   }
}
