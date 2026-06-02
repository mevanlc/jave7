package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.algorithm.repair.AsciiRepairAlgorithm;
import de.jave.jave.algorithm.repair.AsciiRepairAlgorithmConfiguration;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import net.dizzy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.dizzy.commons.core.progress.IProgressMonitor;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.progress.ProgressMonitorDialog;

public final class RepairShakedLinesAction extends AbstractJaveAction {
   private final AsciiRepairAlgorithmConfiguration configuration;

   public RepairShakedLinesAction(JaveMainPanel mainPanel, AsciiRepairAlgorithmConfiguration configuration) {
      super(mainPanel, "Shaked lines", null);
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      final CharacterPlate cp = this.getMainPanel().getContentOfInterest().getContent();
      ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(parentComponent, "Repairing Shaked Lines");

      try {
         progressDialog.run(new INonInterruptableRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
               monitor.beginTask("Repair shaked lines", -1);
               CharacterPlate cr = new AsciiRepairAlgorithm(RepairShakedLinesAction.this.configuration).repairShaked(cp);
               if (cr != null) {
                  RepairShakedLinesAction.this.getMainPanel().setContentOfInterest(cr);
                  RepairShakedLinesAction.this.getMainPanel().repaint();
                  RepairShakedLinesAction.this.getMainPanel().saveCurrentState("repair");
               }
            }
         });
      } catch (InvocationTargetException var6) {
         throw new RuntimeException(var6);
      }
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }
}
