package de.jave.jave.actions;

import de.jave.jave.JaveApplication;
import de.jave.jave.Labyrinth;
import java.awt.Component;
import net.dizzy.commons.swing.action.SmartAction;

public class LabyrinthAction extends SmartAction {
   private final JaveApplication jave;

   public LabyrinthAction(JaveApplication jave) {
      super("Labyrinth");
      this.jave = jave;
   }

   @Override
   protected void execute(Component parentComponent) {
      Labyrinth at = new Labyrinth(this.jave);
      at.startGame();
   }
}
