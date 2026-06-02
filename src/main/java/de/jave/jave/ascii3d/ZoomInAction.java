package de.jave.jave.ascii3d;

import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public final class ZoomInAction extends SmartAction {
   private final Navigate3dModel model;

   public ZoomInAction(Navigate3dModel model) {
      super(JaveIcons.NAVIGATE_NAVIGATE_IN);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Zoom In");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.doZoomIn();
   }
}
