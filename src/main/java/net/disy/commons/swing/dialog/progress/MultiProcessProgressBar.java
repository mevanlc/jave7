package net.disy.commons.swing.dialog.progress;

import javax.swing.JComponent;
import net.disy.commons.core.model.IObjectSetModel;
import net.disy.commons.core.model.ObjectSetModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.util.GuiUtilities;

public class MultiProcessProgressBar implements IComponentContainer {
   final ProgressMonitorBar progressMonitorBar = new ProgressMonitorBar();
   ObjectSetModel<Object> listModel = new ObjectSetModel<>();

   public MultiProcessProgressBar() {
      this.listModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            GuiUtilities.invokeLaterIfNecessary(new Runnable() {
               @Override
               public void run() {
                  if (MultiProcessProgressBar.this.listModel.isEmpty()) {
                     MultiProcessProgressBar.this.progressMonitorBar.done();
                  } else {
                     MultiProcessProgressBar.this.progressMonitorBar.beginTaskWithUnknownTotalWork(null);
                  }
               }
            });
         }
      });
   }

   @Override
   public JComponent getContent() {
      return this.progressMonitorBar;
   }

   public IObjectSetModel<Object> getSetModel() {
      return this.listModel;
   }
}
