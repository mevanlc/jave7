package de.jave.image2ascii;

import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.progress.ICanceledListener;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.dialog.progress.ProgressMonitorBar;

public class ProxyProgressMonitor implements IProgressMonitor {
   private final ListenerList<ICanceledListener> listeners = new ListenerList<>();
   private final ProgressMonitorBar progressMonitor;
   private boolean canceled = false;

   public ProxyProgressMonitor(ProgressMonitorBar progressMonitor) {
      Ensure.ensureArgumentNotNull(progressMonitor);
      this.progressMonitor = progressMonitor;
   }

   @Override
   public void beginTask(String name, int totalWork) {
      if (!this.isCanceled()) {
         this.progressMonitor.beginTask(name, totalWork);
      }
   }

   @Override
   public void done() {
      if (!this.isCanceled()) {
         this.progressMonitor.done();
      }
   }

   @Override
   public void setCanceled(boolean canceled) {
      if (this.canceled != canceled) {
         this.canceled = canceled;
         if (canceled) {
            this.fireCanceledEvent();
         }
      }
   }

   @Override
   public void subTask(String name) {
      if (!this.isCanceled()) {
         this.progressMonitor.subTask(name);
      }
   }

   @Override
   public void worked(int work) {
      if (!this.isCanceled()) {
         this.progressMonitor.worked(work);
      }
   }

   public boolean isCanceled() {
      return this.canceled || this.progressMonitor.isCanceled();
   }

   public void addCanceledListener(ICanceledListener listener) {
      Ensure.ensureNotNull(listener);
      this.listeners.add(listener);
   }

   public void removeCanceledListener(ICanceledListener listener) {
      this.listeners.remove(listener);
   }

   private void fireCanceledEvent() {
      this.listeners.forAllDo(new IClosure<ICanceledListener>() {
         public void execute(ICanceledListener listener) throws RuntimeException {
            listener.canceled();
         }
      });
   }

   @Override
   public void beginTaskWithUnknownTotalWork(String name) {
      this.beginTask(name, -1);
   }
}
