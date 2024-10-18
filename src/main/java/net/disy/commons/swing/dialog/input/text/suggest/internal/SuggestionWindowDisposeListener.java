package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dispose.IDisposable;
import net.disy.commons.swing.util.GuiUtilities;

public class SuggestionWindowDisposeListener {
   private final IDisposable window;
   private final WindowListener parentWindowListener = new WindowAdapter() {
      @Override
      public void windowDeactivated(WindowEvent e) {
         SuggestionWindowDisposeListener.this.window.dispose();
      }
   };
   private final ComponentAdapter componentListener = new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
         SuggestionWindowDisposeListener.this.window.dispose();
      }

      @Override
      public void componentMoved(ComponentEvent e) {
         SuggestionWindowDisposeListener.this.window.dispose();
      }

      @Override
      public void componentHidden(ComponentEvent e) {
         SuggestionWindowDisposeListener.this.window.dispose();
      }
   };

   public SuggestionWindowDisposeListener(IDisposable window) {
      Ensure.ensureArgumentNotNull(window);
      this.window = window;
   }

   public void attachTo(JComponent hookComponent) {
      Window parentWindow = GuiUtilities.getWindowFor(hookComponent);
      parentWindow.addWindowListener(this.parentWindowListener);
      parentWindow.addComponentListener(this.componentListener);
      hookComponent.addComponentListener(this.componentListener);
   }

   public void detachFrom(JComponent hookComponent) {
      Window parentWindow = GuiUtilities.getWindowFor(hookComponent);
      parentWindow.removeWindowListener(this.parentWindowListener);
      parentWindow.removeComponentListener(this.componentListener);
      hookComponent.removeComponentListener(this.componentListener);
   }
}
