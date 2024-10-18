package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.ToolManager;
import java.awt.Component;
import javax.swing.Icon;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public abstract class AbstractJaveAction extends SmartAction {
   private final JaveMainPanel mainPanel;

   public AbstractJaveAction(JaveMainPanel mainPanel, String label, Icon icon) {
      super(label, icon);
      Ensure.ensureArgumentNotNull(mainPanel);
      this.mainPanel = mainPanel;
      mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractJaveAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      IDocumentEditor activeEditor = this.mainPanel.getActiveEditorModel().getActiveEditor();
      this.setEnabled(this.getEnabledStrategy().isEnabledFor(activeEditor));
   }

   protected abstract IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy();

   protected final JaveMainPanel getMainPanel() {
      return this.mainPanel;
   }

   protected final ToolManager getToolManager() {
      return this.mainPanel.getToolManager();
   }

   @Override
   public final void execute(Component parentComponent) {
      IDocumentEditor activeEditor = this.mainPanel.getEditor();
      this.ececute(parentComponent, activeEditor);
   }

   protected abstract void ececute(Component var1, IDocumentEditor var2);
}
