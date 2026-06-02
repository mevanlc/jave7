package net.dizzy.commons.swing.dialog.progress;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import net.dizzy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.dizzy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.dizzy.commons.core.progress.NullProgressMonitor;
import net.dizzy.commons.core.progress.NonCancelable;

public class ProgressMonitorDialog {
   public ProgressMonitorDialog(Component parent, String title) {
   }

   public void run(INonInterruptableRunnableWithProgress runnable) throws InvocationTargetException {
      runnable.run(new NullProgressMonitor());
   }

   public void run(IInterruptableRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
      runnable.run(new NullProgressMonitor(), NonCancelable.getInstance());
   }
}
