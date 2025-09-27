package de.jave.jave.ascii3d;

import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public final class RotateLeftAction extends SmartAction {
   private final Navigate3dModel model;

   public RotateLeftAction(Navigate3dModel model) {
      super(JaveIcons.NAVIGATE_ROTATE_LEFT);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Rotate left");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.doRotateLeft();
   }
}
