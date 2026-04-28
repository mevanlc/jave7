package de.jave.jave.actions;

import de.jave.jave.UnicodePickerDialog;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class ShowUnicodePickerAction extends AbstractJaveAction {

   private UnicodePickerDialog pickerDialog;

   public ShowUnicodePickerAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Unicode Picker...", null);
      this.setAcceleratorKey(JaveKeyBindings.UNICODE_PICKER);
      this.setToolTipText("Open the Unicode character picker");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (this.pickerDialog == null) {
         this.pickerDialog = new UnicodePickerDialog(parentComponent, this.getMainPanel());
      }
      this.pickerDialog.show();
   }
}
