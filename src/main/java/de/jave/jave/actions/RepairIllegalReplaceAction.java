package de.jave.jave.actions;

import de.jave.jave.CharacterSets;
import de.jave.jave.JaveSelection;
import de.jave.jave.algorithm.replaceillegal.AsciiReplaceIllegal;
import de.jave.jave.algorithm.replaceillegal.AsciiReplaceIllegalConfiguration;
import de.jave.jave.algorithm.replaceillegal.AsciiReplaceIllegalReport;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.gui.IStatusDisplay;
import java.awt.Component;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;

public class RepairIllegalReplaceAction extends SmartAction {
   private final JaveMainPanel mainPanel;
   private final CharacterSets characterSets;
   private final AsciiReplaceIllegalConfiguration replaceIllegalConfiguration;
   private final IStatusDisplay status;

   public RepairIllegalReplaceAction(
      JaveMainPanel mainPanel, AsciiReplaceIllegalConfiguration replaceIllegalConfiguration, CharacterSets characterSets, IStatusDisplay status
   ) {
      super("Replace illegal characters");
      Ensure.ensureArgumentNotNull(mainPanel);
      Ensure.ensureArgumentNotNull(replaceIllegalConfiguration);
      Ensure.ensureArgumentNotNull(characterSets);
      Ensure.ensureArgumentNotNull(status);
      this.mainPanel = mainPanel;
      this.replaceIllegalConfiguration = replaceIllegalConfiguration;
      this.characterSets = characterSets;
      this.status = status;
   }

   @Override
   protected void execute(Component parentComponent) {
      JaveSelection sel = this.mainPanel.getContentOfInterest();
      if (sel != null) {
         AsciiReplaceIllegalReport report = new AsciiReplaceIllegal(this.replaceIllegalConfiguration, this.characterSets).replaceIllegal(sel);
         this.mainPanel.repaint();
         this.mainPanel.saveCurrentState("replace illegal characters");
         if (report.getIllegalCharacterCount() == 0) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message("Replace illegal characters", "There are no illegal characters.", MessageType.INFORMATION)
            );
         } else {
            int replaced = report.getReplacedCharacterCount();
            int count = report.getIllegalCharacterCount();
            if (replaced == 0) {
               MessageDialogFactory.showMessageDialog(
                  parentComponent,
                  new Message("Replace illegal characters", "Unable to find substitutes for " + count + " illegal characters.", MessageType.INFORMATION)
               );
            } else if (replaced < count) {
               if (count == 1) {
                  MessageDialogFactory.showMessageDialog(
                     parentComponent,
                     new Message(
                        "Replace illegal characters",
                        "Replaced 1 illegal character.\nUnable to find substitutes for the remaining " + (count - replaced) + ".",
                        MessageType.INFORMATION
                     )
                  );
               } else {
                  MessageDialogFactory.showMessageDialog(
                     parentComponent,
                     new Message(
                        "Replace illegal characters",
                        "Replaced " + replaced + " illegal characters.\nUnable to find substitutes for the remaining " + (count - replaced) + ".",
                        MessageType.INFORMATION
                     )
                  );
               }
            } else if (count == 1) {
               this.status.showStatus("Replaced 1 of " + count + " occurence.");
            } else {
               this.status.showStatus("Replaced " + replaced + " of " + count + " occurrences.");
            }
         }
      }
   }
}
