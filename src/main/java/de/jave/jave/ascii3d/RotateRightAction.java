package de.jave.jave.ascii3d;

import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public final class RotateRightAction extends SmartAction {
   private final Navigate3dModel model;

   public RotateRightAction(Navigate3dModel model) {
      super(JaveIcons.NAVIGATE_ROTATE_RIGHT);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Rotate right");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.doRotateRight();
   }
}
