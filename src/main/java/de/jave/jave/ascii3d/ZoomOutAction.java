package de.jave.jave.ascii3d;

import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public final class ZoomOutAction extends SmartAction {
   private final Navigate3dModel model;

   public ZoomOutAction(Navigate3dModel model) {
      super(JaveIcons.NAVIGATE_NAVIGATE_OUT);
      this.setToolTipText("Zoom Out");
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.doZoomOut();
   }
}
