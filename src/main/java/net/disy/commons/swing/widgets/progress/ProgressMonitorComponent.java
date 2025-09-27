package net.disy.commons.swing.dialog.progress;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.message.LargeIconMessageTypeUi;
import net.disy.commons.swing.widgets.AutoWrappingLabel;

public class ProgressMonitorComponent implements IProgressMonitor, IComponentContainer {
   private static final String TASK_LABEL_ELLIPSIS = "...";
   private final ProgressMonitorBar progressBar;
   private final AutoWrappingLabel taskNameLabel = new AutoWrappingLabel();
   private final JLabel subTaskNameLabel;

   public ProgressMonitorComponent() {
      this.taskNameLabel.setOpaque(false);
      this.subTaskNameLabel = new JLabel("", 2);
      this.subTaskNameLabel.setPreferredSize(new Dimension(this.subTaskNameLabel.getPreferredSize().width, 25));
      this.progressBar = new ProgressMonitorBar();
      this.progressBar.setBorderPainted(true);
   }

   @Override
   public JComponent getContent() {
      Icon icon = new LargeIconMessageTypeUi().getIcon(MessageType.INFORMATION);
      JPanel topPanel = new JPanel(new BorderLayout(10, 8));
      topPanel.add(new JLabel(icon), "West");
      topPanel.add(this.taskNameLabel.getContent(), "Center");
      topPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
      JPanel mainPanel = new JPanel(new GridDialogLayout(1, false));
      mainPanel.add(topPanel, GridDialogLayoutData.FILL_HORIZONTAL);
      mainPanel.add(this.progressBar, GridDialogLayoutData.FILL_HORIZONTAL);
      mainPanel.add(this.subTaskNameLabel, GridDialogLayoutData.FILL_HORIZONTAL);
      mainPanel.setBorder(new EmptyBorder(5, 7, 0, 7));
      return mainPanel;
   }

   @Override
   public final void beginTaskWithUnknownTotalWork(String name) {
      this.beginTask(name, -1);
   }

   @Override
   public void beginTask(String name, final int totalWork) {
      final String taskName = addTaskLabelEllipsis(name);
      this.subTask("");
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressMonitorComponent.this.progressBar.beginTask(taskName, totalWork);
            ProgressMonitorComponent.this.taskNameLabel.setText(taskName);
         }
      });
   }

   public static String addTaskLabelEllipsis(String name) {
      if (name.length() == 0) {
         return name;
      } else {
         return name.endsWith("...") ? name : name + "...";
      }
   }

   @Override
   public void done() {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressMonitorComponent.this.progressBar.done();
         }
      });
   }

   @Override
   public void subTask(final String name) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ProgressMonitorComponent.this.subTaskNameLabel.setText(name);
         }
      });
   }

   @Override
   public void worked(int work) {
      this.progressBar.worked(work);
   }

   @Override
   public void setCanceled(boolean canceled) {
      throw new UnsupportedOperationException();
   }
}
