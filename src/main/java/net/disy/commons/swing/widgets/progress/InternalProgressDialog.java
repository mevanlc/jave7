package net.disy.commons.swing.dialog.progress;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import net.disy.commons.core.progress.ICanceledListener;
import net.disy.commons.core.progress.IObservableCancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.layout.util.ButtonPanelBuilder;
import net.disy.commons.swing.util.GuiUtilities;

public class InternalProgressDialog implements IProgressMonitor, IObservableCancelable, IProgressComponent {
   private JDialog dialog;
   private final SmartAction cancelAction;
   private boolean cancelable = false;
   private final InternalProgressDialogModel model;
   private boolean disposed = false;
   private boolean showing = false;
   private boolean dialogClosed = false;
   private final Component parentComponent;
   private final String title;
   private final Collection<ICanceledListener> canceledListeners = new ArrayList<>();
   private final ProgressMonitorComponent monitorComponent;

   public InternalProgressDialog(Component parentComponent, String title, InternalProgressDialogModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.parentComponent = parentComponent;
      this.title = title;
      this.model = model;
      this.cancelAction = new SmartAction(DisyCommonsSwingDialogMessages.CANCEL) {
         @Override
         protected void execute(Component parent) {
            InternalProgressDialog.this.performCancel();
         }
      };
      this.monitorComponent = new ProgressMonitorComponent();
   }

   private void performCancel() {
      if (this.cancelable) {
         this.setCanceled(true);
         this.cancelAction.setEnabled(false);
      }

      this.yield();
   }

   public void setCancelable(boolean cancelable) {
      this.cancelable = cancelable;
      this.cancelAction.setEnabled(cancelable);
   }

   @Override
   public void show() {
      synchronized (this) {
         if (this.disposed) {
            return;
         }

         this.showing = true;
         this.dialog = this.createDialog();
      }

      GuiUtilities.centerToParent(this.dialog);
      this.dialog.setVisible(true);
   }

   private JDialog createDialog() {
      JButton cancelButton = new JButton(this.cancelAction);
      ButtonPanelBuilder builder = new ButtonPanelBuilder();
      builder.add(cancelButton);
      JDialog newDialog = GuiUtilities.createDialog(this.parentComponent, this.title);
      newDialog.getContentPane().setLayout(new BorderLayout(2, 2));
      newDialog.getContentPane().add(this.monitorComponent.getContent(), "Center");
      newDialog.getContentPane().add(builder.createPanel(), "South");
      newDialog.pack();
      newDialog.setSize(Math.max(newDialog.getWidth(), 450), newDialog.getHeight());
      newDialog.setResizable(false);
      newDialog.setModal(true);
      newDialog.getRootPane().setDefaultButton(cancelButton);
      newDialog.setDefaultCloseOperation(0);
      newDialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            InternalProgressDialog.this.performCancel();
         }

         @Override
         public void windowClosed(WindowEvent e) {
            synchronized (InternalProgressDialog.this) {
               InternalProgressDialog.this.dialogClosed = true;
            }
         }
      });
      return newDialog;
   }

   @Override
   public void dispose() {
      synchronized (this) {
         this.disposed = true;
         if (this.showing) {
            while ((this.dialog == null || !this.dialog.isVisible()) && !this.dialogClosed) {
               try {
                  this.wait(50L);
               } catch (InterruptedException var4) {
               }
            }
         }
      }

      if (this.dialog != null) {
         this.dialog.dispose();
      }
   }

   @Override
   public final void beginTaskWithUnknownTotalWork(String name) {
      this.monitorComponent.beginTaskWithUnknownTotalWork(name);
      this.yield();
   }

   @Override
   public void beginTask(String name, int totalWork) {
      this.monitorComponent.beginTask(name, totalWork);
      this.yield();
   }

   @Override
   public void done() {
      this.monitorComponent.done();
      this.yield();
   }

   @Override
   public void setCanceled(boolean canceled) {
      this.model.setCanceled(canceled);
      if (canceled) {
         this.fireCanceled();
      }

      this.yield();
   }

   @Override
   public synchronized void addCanceledListener(ICanceledListener listener) {
      Ensure.ensureNotNull(listener);
      this.canceledListeners.add(listener);
   }

   @Override
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

   @Override
   public void subTask(String name) {
      this.monitorComponent.subTask(name);
      this.yield();
   }

   @Override
   public void worked(int work) {
      this.monitorComponent.worked(work);
      this.yield();
   }

   @Override
   public boolean isCanceled() {
      this.yield();
      return this.model.isCanceled();
   }

   private void yield() {
      if (this.dialog != null && !this.dialog.isActive()) {
         this.dialog.requestFocus();
      }

      Thread.yield();
   }

   public boolean getTestIsDialogVisible() {
      return this.dialog != null && this.dialog.isVisible();
   }

   public boolean getTestIsCancelable() {
      return this.cancelable;
   }
}
