package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public final class ResetZoomAction extends AbstractJaveAction {
   public ResetZoomAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Reset Zoom", null);
      this.setAcceleratorKey(JaveKeyBindings.RESET_ZOOM);
      this.setToolTipText("Restore the configured display font size");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      editor.getPlate().getZoomableFontModel().resetZoom();
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }
}
