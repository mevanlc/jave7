package net.dizzy.commons.swing.dialog.progress;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.swing.component.IComponentContainer;

public class ProgressMonitorBar extends javax.swing.JPanel implements IProgressMonitor, IComponentContainer {
   private final JProgressBar progressBar = new JProgressBar();
   private boolean canceled;

   public ProgressMonitorBar() {
      super(new java.awt.BorderLayout());
      add(progressBar, java.awt.BorderLayout.CENTER);
   }

   @Override public void beginTask(String name, int totalWork) { progressBar.setIndeterminate(totalWork < 0); progressBar.setMaximum(Math.max(0, totalWork)); progressBar.setValue(0); progressBar.setString(name); }
   @Override public void worked(int work) { progressBar.setValue(progressBar.getValue() + work); }
   @Override public void subTask(String name) { progressBar.setString(name); }
   @Override public void done() { progressBar.setIndeterminate(false); progressBar.setValue(progressBar.getMaximum()); }
   @Override public boolean isCanceled() { return canceled; }
   @Override public void setCanceled(boolean canceled) { this.canceled = canceled; }
   @Override public void beginTaskWithUnknownTotalWork(String name) { beginTask(name, -1); }
   @Override public JComponent getContent() { return this; }
}
