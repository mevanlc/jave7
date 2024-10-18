package net.disy.commons.swing.dialog.progress;

import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.swing.dialog.wizard.AbstractWizardPage;
import net.disy.commons.swing.dialog.wizard.IWizardConfiguration;

public abstract class AbstractProgressMonitorWizardPage extends AbstractWizardPage {
   private final BooleanModel taskDoneModel = new BooleanModel();
   private final ProgressMonitorComponent progressMonitorComponent = new ProgressMonitorComponent();

   public AbstractProgressMonitorWizardPage(String description, String title, String runningMessageText, IWizardConfiguration wizardConfiguration) {
      super(description, title, runningMessageText, wizardConfiguration);
   }

   @Override
   protected final JComponent createContent() {
      this.taskDoneModel.addChangeListener(this.getCheckInputValidListener());
      return this.progressMonitorComponent.getContent();
   }

   @Override
   protected final IBasicMessage createCurrentMessage() {
      return this.isTaskDone() ? new BasicMessage(this.getDoneMessageText()) : this.getDefaultMessage();
   }

   protected boolean isTaskDone() {
      return this.taskDoneModel.getValue();
   }

   @Override
   public final void requestFocus() {
   }

   @Override
   public final void enter() {
      try {
         this.executeTaskInProgressMonitor();
      } catch (InvocationTargetException var3) {
         Throwable cause = var3.getCause();
         if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
         } else {
            throw new RuntimeException(cause);
         }
      }
   }

   private void executeTaskInProgressMonitor() throws InvocationTargetException {
      ProgressMonitorExecutor progressMonitorExecutor = new ProgressMonitorExecutor(new InternalProgressDialogModel(), new IProgressComponent() {
         @Override
         public void show() {
            AbstractProgressMonitorWizardPage.this.taskDoneModel.setValue(false);
         }

         @Override
         public void dispose() {
            AbstractProgressMonitorWizardPage.this.taskDoneModel.setValue(true);
         }
      });
      INonInterruptableRunnableWithProgress runnable = new INonInterruptableRunnableWithProgress() {
         @Override
         public void run(IProgressMonitor monitor) throws InvocationTargetException {
            AbstractProgressMonitorWizardPage.this.executeWithProgress(monitor);
         }
      };
      progressMonitorExecutor.run(runnable, this.progressMonitorComponent);
   }

   protected abstract void executeWithProgress(IProgressMonitor var1) throws InvocationTargetException;

   protected abstract String getDoneMessageText();
}
