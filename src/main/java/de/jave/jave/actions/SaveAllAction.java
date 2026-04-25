package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.performers.SavePerformer;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class SaveAllAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public SaveAllAction(JavEApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Save All", JaveIcons.SAVE_ALL_EDIT_ICON);
      this.jave = jave;
      this.setToolTipText("Save all Files");
      this.setAcceleratorKey(JaveKeyBindings.SAVE_ALL);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor currentEditor) {
      JaveMainPanel mainPanel = this.getMainPanel();

      for (int i = 0; i < mainPanel.getEditorCount(); i++) {
         IDocumentEditor editor = mainPanel.getEditor(i);
         if (editor.isModified()) {
            boolean success = SavePerformer.performSave(
               parentComponent,
               editor,
               this.jave.getApplicationPreferences().getRecentFileList(),
               this.jave.getDocumentManager().getCurrentDirectoryModel(),
               this.jave.getStatusDisplay(),
               this.jave.getDocumentSaveListener()
            );
            if (!success) {
               return;
            }
         }
      }

      this.jave.getStatusDisplay().showStatus("All Files saved.");
   }
}
