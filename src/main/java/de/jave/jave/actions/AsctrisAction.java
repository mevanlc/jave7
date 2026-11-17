package de.jave.jave.actions;

import de.jave.jave.JaveApplication;
import de.jave.jave.filter.Filter;
import de.jave.jave.games.asctris.AscTris;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public class AsctrisAction extends SmartAction {
   private final JaveApplication jave;
   private final Filter filter;

   public AsctrisAction(JaveApplication jave, Filter filter) {
      super("Asc-Tris", JaveIcons.ASCTRIS);
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(filter);
      this.jave = jave;
      this.filter = filter;
   }

   @Override
   protected void execute(Component parentComponent) {
      AscTris at = new AscTris(this.jave, this.filter);
      at.startGame();
   }
}
