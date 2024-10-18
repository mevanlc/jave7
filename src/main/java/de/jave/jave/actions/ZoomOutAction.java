package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public final class ZoomOutAction extends AbstractJaveAction {
   public ZoomOutAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Zoom out", JaveIcons.ZOOM_MINUS_ICON);
      this.setToolTipText("Zoom out");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      editor.getPlate().getZoomableFontModel().zoomOut();
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }
}
