package de.jave.jave.actions;

import de.jave.jave.JaveApplication;
import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.performers.SavePerformer;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class SaveAction extends AbstractJaveAction {
   private final JaveApplication jave;

   public SaveAction(JaveApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Save", JaveIcons.SAVE_EDIT_ICON);
      this.jave = jave;
      this.setToolTipText("Save File");
      this.setAcceleratorKey(JaveKeyBindings.SAVE);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      SavePerformer.performSave(
         parentComponent,
         editor,
         this.jave.getApplicationPreferences(),
         this.jave.getDocumentManager().getCurrentDirectoryModel(),
         this.jave.getStatusDisplay(),
         this.jave.getDocumentSaveListener()
      );
   }
}
