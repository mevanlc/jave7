package net.disy.commons.swing.dialog.core;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Window;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import net.disy.commons.core.util.Ensure;

public class SwingFrame implements ISwingFrameOrDialog {
   private final JFrame frame;
   private boolean modal = false;

   public SwingFrame(JFrame frame) {
      Ensure.ensureArgumentNotNull(frame);
      this.frame = frame;
   }

   @Override
   public void setTitle(String title) {
      this.frame.setTitle(title);
   }

   @Override
   public JRootPane getRootPane() {
      return this.frame.getRootPane();
   }

   @Override
   public void pack() {
      this.frame.pack();
   }

   @Override
   public void setModal(boolean modal) {
      this.modal = modal;
   }

   @Override
   public Container getContentPane() {
      return this.frame.getContentPane();
   }

   @Override
   public void setDefaultCloseOperation(int closeOperation) {
      this.frame.setDefaultCloseOperation(closeOperation);
   }

   @Override
   public void addWindowListener(WindowListener windowListener) {
      this.frame.addWindowListener(windowListener);
   }

   @Override
   public void removeWindowListener(WindowListener windowListener) {
      this.frame.removeWindowListener(windowListener);
   }

   @Override
   public void dispose() {
      this.setVisible(false);
      this.frame.dispose();
   }

   @Override
   public void validate() {
      this.frame.validate();
   }

   @Override
   public void repaint() {
      this.frame.repaint();
   }

   @Override
   public Window getWindow() {
      return this.frame;
   }

   @Override
   public void setResizable(boolean resizable) {
      this.frame.setResizable(resizable);
   }

   @Override
   public void show() {
      this.setVisible(true);
   }

   @Override
   public void setVisible(boolean visible) {
      this.frame.setVisible(visible);
      if (this.modal) {
         if (visible) {
            this.startModal();
         } else {
            this.stopModal();
         }
      }
   }

   private synchronized void startModal() {
      try {
         if (SwingUtilities.isEventDispatchThread()) {
            EventQueue theQueue = this.frame.getToolkit().getSystemEventQueue();

            while (this.isVisible()) {
               AWTEvent event = theQueue.getNextEvent();
               Object source = event.getSource();
               if (event instanceof ActiveEvent) {
                  ((ActiveEvent)event).dispatch();
               } else if (source instanceof Component) {
                  ((Component)source).dispatchEvent(event);
               } else if (source instanceof MenuComponent) {
                  ((MenuComponent)source).dispatchEvent(event);
               } else {
                  System.err.println("Unable to dispatch: " + event);
               }
            }
         } else {
            while (this.isVisible()) {
               this.wait();
            }
         }
      } catch (InterruptedException var4) {
      }
   }

   private synchronized void stopModal() {
      this.notifyAll();
   }

   @Override
   public boolean isVisible() {
      return this.frame.isVisible();
   }
}
