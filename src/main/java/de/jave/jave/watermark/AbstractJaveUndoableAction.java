package de.jave.jave.actions;

import de.jave.jave.JaveSelection;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Icon;

public abstract class AbstractJaveUndoableAction extends AbstractJaveAction {
   public AbstractJaveUndoableAction(JaveMainPanel mainPanel, String label, Icon icon) {
      super(mainPanel, label, icon);
   }

   @Override
   protected final void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveSelection contentOfInterest = editor.getPlate().getContentOfInterest();
      Dimension oldSize = contentOfInterest.getSize();
      JaveSelection result = this.apply(parentComponent, contentOfInterest);
      if (result != null) {
         JaveMainPanel mainPanel = this.getMainPanel();
         mainPanel.setContentOfInterest(result);
         if (!oldSize.equals(result.getSize())) {
            mainPanel.revalidate();
         }

         mainPanel.getCurrentTool().checkSize();
         mainPanel.repaint();
         mainPanel.saveCurrentState(this.getActionName());
      }
   }

   protected abstract String getActionName();

   protected abstract JaveSelection apply(Component var1, JaveSelection var2);
}
