package de.jave.jave.actions;

import de.jave.jave.CharacterSets;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import de.jave.lib.gui.IStatusDisplay;
import java.awt.Component;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class RepairIllegalRemoveAction extends SmartAction {
   private final JaveMainPanel mainPanel;
   private final CharacterSets characterSets;
   private final IStatusDisplay status;

   public RepairIllegalRemoveAction(JaveMainPanel mainPanel, CharacterSets characterSets, IStatusDisplay status) {
      super("Remove illegal characters");
      Ensure.ensureArgumentNotNull(mainPanel);
      Ensure.ensureArgumentNotNull(characterSets);
      Ensure.ensureArgumentNotNull(status);
      this.mainPanel = mainPanel;
      this.characterSets = characterSets;
      this.status = status;
   }

   @Override
   protected void execute(Component parentComponent) {
      CharacterPlate cp = this.mainPanel.getContentOfInterest().getContent();
      if (cp != null) {
         int count = 0;
         int h = cp.getHeight();
         int w = cp.getWidth();

         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               if (!this.characterSets.isLegal(cp.get(x, y))) {
                  cp.setForce(x, y, ' ');
                  count++;
               }
            }
         }

         if (count == 0) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message("Remove illegal characters", "There are no illegal characters.", MessageType.INFORMATION)
            );
         } else if (count == 1) {
            this.status.showStatus("Removed 1 occurence.");
         } else {
            this.status.showStatus("Removed " + count + " occurrences.");
         }

         this.mainPanel.repaint();
         this.mainPanel.saveCurrentState("remove illegal characters");
      }
   }
}
