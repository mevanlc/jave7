package de.jave.jave.actions;

import de.jave.jave.Plate;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class LayerCropAction extends AbstractJaveAction {
   public LayerCropAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Crop", null);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      if (!plate.cropActiveLayerToSelection()) {
         plate.showStatus("Layer crop requires a selection");
         plate.requestFocus();
         return;
      }
      this.getMainPanel().getCurrentTool().checkSize();
      this.getMainPanel().saveCurrentState("layer crop");
      plate.requestFocus();
   }
}
