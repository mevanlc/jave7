package net.disy.commons.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public abstract class SmartAction extends AbstractDisyAction {
   public SmartAction() {
      this(new ActionConfiguration());
   }

   public SmartAction(String name) {
      this(new ActionConfiguration(name));
   }

   public SmartAction(String name, Icon icon) {
      this(new ActionConfiguration(name, icon));
   }

   public SmartAction(Icon icon) {
      this(null, icon);
   }

   public SmartAction(IActionConfiguration configuration) {
      Ensure.ensureArgumentNotNull(configuration);
      this.setName(configuration.getName());
      this.setIcon(configuration.getIcon());
      this.setToolTipText(configuration.getToolTipText());
   }

   @Override
   public final void actionPerformed(ActionEvent e) {
      Component parentComponent = this.getParentComponent(e);
      this.execute(parentComponent);
   }

   protected abstract void execute(Component var1);
}
