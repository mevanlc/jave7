package de.jave.jave.clipart;

import de.jave.gui.FilenameTextField;
import java.awt.Component;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.input.text.ITextInputDialogConfiguration;
import net.disy.commons.swing.dialog.input.text.ITextInputDialogResult;
import net.disy.commons.swing.dialog.input.text.SmartTextInputDialog;
import net.disy.commons.swing.icon.CommonIcons;

public final class NewClipartFolderAction extends SmartAction {
   private final ClipartManager clipartManager;
   private final ClipartGroupListPanel listPanel;

   public NewClipartFolderAction(ClipartManager clipartManager, ClipartGroupListPanel listPanel) {
      super("New", CommonIcons.FOLDER_NEW);
      Ensure.ensureArgumentNotNull(clipartManager);
      Ensure.ensureArgumentNotNull(listPanel);
      this.listPanel = listPanel;
      this.clipartManager = clipartManager;
   }

   @Override
   protected void execute(Component parentComponent) {
      ITextInputDialogResult result = SmartTextInputDialog.showTextInputDialog(
         parentComponent,
         new ITextInputDialogConfiguration() {
            @Override
            public String getTitle() {
               return "New Clipart Category";
            }

            @Override
            public String getDefaultMessageText() {
               return "Please enter a name for a new clipart category.";
            }

            @Override
            public IBasicMessage createCurrentMessage(String selectedText) {
               String newCategoryName = selectedText.trim().toLowerCase();
               if (newCategoryName.length() == 0) {
                  return new BasicMessage("The name is empty. Please specify a name for the new category.", MessageType.ERROR);
               } else if (NewClipartFolderAction.this.clipartManager.hasGroupNameIgnoringCase(newCategoryName)) {
                  return new BasicMessage("The specified category already exists. Please specify a different name for the new category.", MessageType.ERROR);
               } else {
                  return !FilenameTextField.isValidFileName(selectedText)
                     ? new BasicMessage(
                        "The specified category name is not valid. Only characters valid for file names are allowed, no spaces or special characters.",
                        MessageType.ERROR
                     )
                     : null;
               }
            }

            @Override
            public String getLabelText() {
               return "Category Name:";
            }
         },
         "category"
      );
      if (!result.isCanceled()) {
         String newGroupName = result.getText().trim().toLowerCase();
         ClipartGroup newGroup = new ClipartGroup(newGroupName);
         this.listPanel.addNewTemporaryGroup(newGroup);
      }
   }
}
