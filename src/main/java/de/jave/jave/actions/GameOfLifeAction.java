package de.jave.jave.actions;

import de.jave.jave.ColorScreenSaver;
import de.jave.jave.filter.Filter;
import java.awt.Component;
import java.awt.Frame;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.util.GuiUtilities;

public class GameOfLifeAction extends SmartAction {
   private final Filter filter;

   public GameOfLifeAction(Filter filter) {
      super("Game of Life Screensaver");
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   protected void execute(Component parentComponent) {
      Frame window = (Frame)GuiUtilities.getWindowFor(parentComponent);
      new ColorScreenSaver(window, this.filter);
   }
}
