package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.Labyrinth;
import java.awt.Component;
import net.disy.commons.swing.action.SmartAction;

public class LabyrinthAction extends SmartAction {
   private final JavEApplication jave;

   public LabyrinthAction(JavEApplication jave) {
      super("Labyrinth");
      this.jave = jave;
   }

   @Override
   protected void execute(Component parentComponent) {
      Labyrinth at = new Labyrinth(this.jave);
      at.startGame();
   }
}
