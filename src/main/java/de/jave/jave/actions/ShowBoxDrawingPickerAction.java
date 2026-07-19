package de.jave.jave.actions;

import de.jave.jave.BoxDrawingPickerDialog;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class ShowBoxDrawingPickerAction extends AbstractJaveAction {
   private BoxDrawingPickerDialog pickerDialog;

   public ShowBoxDrawingPickerAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Box Drawing Picker...", null);
      this.setAcceleratorKey(JaveKeyBindings.BOX_DRAWING_PICKER);
      this.setToolTipText("Open the box drawing character picker");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (this.pickerDialog == null) {
         this.pickerDialog = new BoxDrawingPickerDialog(parentComponent, this.getMainPanel());
      }
      this.pickerDialog.show();
   }
}
