package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public final class ZoomInAction extends AbstractJaveAction {
   public ZoomInAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Zoom in", JaveIcons.ZOOM_PLUS_ICON);
      this.setAcceleratorKey(JaveKeyBindings.ZOOM_IN);
      this.setToolTipText("Zoom in");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      editor.getPlate().getZoomableFontModel().zoomIn();
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }
}
