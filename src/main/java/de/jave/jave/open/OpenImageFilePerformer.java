package de.jave.jave.open;

import de.jave.jave.JavEApplication;
import de.jave.jave.WatermarkTool;
import de.jave.jave.actions.CamelizerAction;
import de.jave.jave.actions.Image2AsciiAction;
import de.jave.jave.actions.JaveActions;
import de.jave.jave.actions.ToolBar;
import de.jave.jave.plate.ToolManager;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import net.dizzy.commons.swing.dialog.input.select.AbstractOneOutOfManyDialogConfiguration;
import net.dizzy.commons.swing.dialog.input.select.ISomeOutOfManyDialogResult;
import net.dizzy.commons.swing.dialog.input.select.SmartSomeOutOfManySelectionDialog;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.IObjectUi;

public class OpenImageFilePerformer {
   public static void performOpenImageFile(Component parentComponent, File file, JavEApplication application, JaveActions actions, ToolManager toolManager) {
      WatermarkTool watermarkTool = (WatermarkTool)toolManager.getTool(ToolBar.WATERMARK_TOOL_INDEX);
      Image2AsciiAction image2AsciiAction = actions.getImage2AsciiAction();
      CamelizerAction camelizerAction = actions.getCamelizerAction();
      final IImageOpenPerformStrategy[] stategies = new IImageOpenPerformStrategy[]{
         new Image2AsciiImageOpenPerformStrategy(image2AsciiAction),
         new WatermarkImageOpenPerformStrategy(watermarkTool, application),
         new CamelizerImageOpenPerformStrategy(camelizerAction, application.getMainPanel())
      };
      AbstractOneOutOfManyDialogConfiguration<IImageOpenPerformStrategy> configuration = new AbstractOneOutOfManyDialogConfiguration<IImageOpenPerformStrategy>(
         
      ) {
         @Override
         public String getDefaultMessageText() {
            return "Please select the action to perform with the specified image file.";
         }

         @Override
         public String getTitle() {
            return "Open Image File";
         }

         @Override
         public String getLabel() {
            return "Action:";
         }

         @Override
         public String getNoItemSelectedErrorMessageText() {
            return "There is no action selected. Please select the action to perform with the specified image file.";
         }

         public IImageOpenPerformStrategy[] getItems() {
            return stategies;
         }

         @Override
         public IObjectUi<IImageOpenPerformStrategy> getObjectUi() {
            return new AbstractObjectUi<IImageOpenPerformStrategy>() {
               public String getLabel(IImageOpenPerformStrategy value) {
                  return value.getName();
               }

               public String getToolTipText(IImageOpenPerformStrategy value) {
                  return value.getToolTipText();
               }

               public Icon getIcon(IImageOpenPerformStrategy value) {
                  return value.getIcon();
               }
            };
         }
      };
      ISomeOutOfManyDialogResult<IImageOpenPerformStrategy> result = SmartSomeOutOfManySelectionDialog.showSelectOneOutOfManyDialog(
         parentComponent, configuration
      );
      if (!result.isCanceled()) {
         IImageOpenPerformStrategy strategy = result.getSelectedItem();
         strategy.perform(parentComponent, file);
      }
   }
}
