package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.filter.Filter;
import de.jave.jave.games.asctris.AscTris;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;

public class AsctrisAction extends SmartAction {
   private final JavEApplication jave;
   private final Filter filter;

   public AsctrisAction(JavEApplication jave, Filter filter) {
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
