package net.disy.commons.swing.events.modifier;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import net.disy.commons.core.model.listener.WeakListenerList;
import net.disy.commons.core.util.IClosure;

public class GlobalInputModifierStateModel {
   private static final GlobalInputModifierStateModel instance = new GlobalInputModifierStateModel();
   private final WeakListenerList<IInputModifierStateListener> listeners = new WeakListenerList<>();
   private InputModifierState inputModifierState = new InputModifierState();

   public static GlobalInputModifierStateModel getInstance() {
      return instance;
   }

   private GlobalInputModifierStateModel() {
      KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      kfm.addKeyEventDispatcher(new KeyEventDispatcher() {
         @Override
         public boolean dispatchKeyEvent(KeyEvent e) {
            if (!GlobalInputModifierStateModel.this.inputModifierState.isEqualState(e)) {
               GlobalInputModifierStateModel.this.inputModifierState = new InputModifierState(e);
               GlobalInputModifierStateModel.this.fireInputModifierStateChangeEvent();
            }

            return false;
         }
      });
   }

   public void addWeakInputModifierStateListener(IInputModifierStateListener listener) {
      this.listeners.add(listener);
   }

   public void removeWeakInputModifierStateListener(IInputModifierStateListener listener) {
      this.listeners.remove(listener);
   }

   private void fire(final InputModifierStateChangeEvent event) {
      this.listeners.forAllDo(new IClosure<IInputModifierStateListener>() {
         public void execute(IInputModifierStateListener input) {
            input.inputModifierStateChanged(event);
         }
      });
   }

   private void fireInputModifierStateChangeEvent() {
      this.fire(new InputModifierStateChangeEvent(this.inputModifierState));
   }

   public InputModifierState getInputModifierState() {
      return this.inputModifierState;
   }
}
