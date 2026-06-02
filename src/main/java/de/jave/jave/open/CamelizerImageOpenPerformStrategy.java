package de.jave.jave.open;

import de.jave.jave.JaveMessages;
import de.jave.jave.actions.CamelizerAction;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class CamelizerImageOpenPerformStrategy implements IImageOpenPerformStrategy {
   private final CamelizerAction action;
   private final JaveMainPanel mainPanel;

   public CamelizerImageOpenPerformStrategy(CamelizerAction action, JaveMainPanel mainPanel) {
      Ensure.ensureArgumentNotNull(action);
      Ensure.ensureArgumentNotNull(mainPanel);
      this.action = action;
      this.mainPanel = mainPanel;
   }

   @Override
   public Icon getIcon() {
      return this.action.getIcon();
   }

   @Override
   public String getName() {
      return "Camelize the Text in the Editor";
   }

   @Override
   public String getToolTipText() {
      return "Opens the image in the Camelizer tool, where the text in the current editor window will be shaped to the shape defined by the image";
   }

   @Override
   public void perform(Component parentComponent, File file) {
      IDocumentEditor activeEditor = this.mainPanel.getActiveEditorModel().getActiveEditor();
      if (activeEditor == null) {
         MessageDialogFactory.showMessageDialog(
            parentComponent, new Message(JaveMessages.JavE, "There is no text document open.\nThe Camelizer can not be applied.", MessageType.INFORMATION)
         );
      } else {
         this.action.perform(parentComponent, activeEditor, file);
      }
   }
}
