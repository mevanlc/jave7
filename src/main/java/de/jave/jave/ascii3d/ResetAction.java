package de.jave.jave.ascii3d;

import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public final class ResetAction extends SmartAction {
   private final Navigate3dModel model;

   public ResetAction(Navigate3dModel model) {
      super(JaveIcons.NAVIGATE_RESET);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Reset");
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.reset();
   }
}
