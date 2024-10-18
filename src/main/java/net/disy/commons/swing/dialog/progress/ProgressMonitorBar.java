package net.disy.commons.swing.dialog.progress;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import net.disy.commons.core.progress.ICanceledListener;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.util.Ensure;

public class ProgressMonitorBar extends JProgressBar implements IProgressMonitor {
   private static final int EXPIRATION_MILLIS = 5000;
   private final Timer expirationTimer = new Timer(5000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         ProgressMonitorBar.this.setIndeterminate(ProgressMonitorBar.this.active);
      }
   });
   private boolean unknownTotalWork;
   private final Collection<ICanceledListener> canceledListeners = new ArrayList<>();
   private boolean canceled = false;
   private boolean active = false;

   public ProgressMonitorBar() {
      this.setOpaque(true);
      this.setDoubleBuffered(false);
      this.setBorderPainted(false);
      this.setStringPainted(false);
   }

   @Override
   public final void beginTaskWithUnknownTotalWork(String name) {
      this.beginTask(name, -1);
   }

   @Override
   public void beginTask(String name, int totalWork) {
      this.canceled = false;
      this.setMinimum(0);
      this.unknownTotalWork = totalWork == -1;
      this.setIndeterminate(this.unknownTotalWork);
      this.setStringPainted(!this.unknownTotalWork);
      if (!this.unknownTotalWork) {
         super.setMaximum(totalWork);
      }

      this.actuallySetValue(0, true);
   }

   @Override
   public void done() {
      this.setStringPainted(false);
      this.actuallySetValue(0, false);
      this.setIndeterminate(false);
   }

   public boolean isCanceled() {
      return this.canceled;
   }

   @Override
   public void setCanceled(boolean canceled) {
      this.canceled = canceled;
      if (canceled) {
         this.fireCanceled();
      }
   }

   @Override
   public void subTask(String name) {
   }

   @Override
   public void worked(int work) {
      if (!this.unknownTotalWork) {
         this.actuallySetValue(this.getValue() + work, true);
      }
   }

   private void actuallySetValue(int newValue, boolean newActive) {
      this.active = newActive;
      this.setValue(newValue);
      this.expirationTimer.restart();
      this.setIndeterminate(this.unknownTotalWork);
   }

   public void finish() {
      this.setIndeterminate(false);
      this.actuallySetValue(this.getMaximum(), false);
   }

   public synchronized void addCanceledListener(ICanceledListener listener) {
      Ensure.ensureNotNull(listener);
      this.canceledListeners.add(listener);
   }

   public synchronized void removeCanceledListener(ICanceledListener listener) {
      this.canceledListeners.remove(listener);
   }

   private void fireCanceled() {
      List<ICanceledListener> clonedListeners;
      synchronized (this) {
         clonedListeners = new ArrayList<>(this.canceledListeners);
      }

      for (ICanceledListener listener : clonedListeners) {
         listener.canceled();
      }
   }
}
