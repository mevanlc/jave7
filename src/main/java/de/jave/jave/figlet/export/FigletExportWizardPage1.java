package de.jave.jave.figlet.export;

import de.jave.jave.JaveGlobalRessources;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletExportWizardPage1 extends AbstractFigletExportWizardPage {
   public FigletExportWizardPage1(FigletExportModel model) {
      super(
         model,
         "FIGlet Font Export",
         "This wizard will help you creating your own FIGlet font. To use this tool you already must have painted the characters. They must be arranged in a regular grid similar to the example below."
      );
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   protected JComponent createContent() {
      JTextArea ta1 = new JTextArea(
         "    _    ___      ___    ___     ___     ___      ___\n   /_\\  | _ )    / __|  |   \\   | __|   | __|    / __|\n  / _ \\ | _ \\   | (__   | |) |  | _|    | _|    | (_ |\n /_/ \\_\\|___/    \\___|  |___/   |___|   |_|      \\___|\n  _  _   ___        _    _  __   _       __  __\n | || | |_ _|    _ | |  | |/ /  | |     |  \\/  |\n | __ |  | |    | || |  | ' <   | |__   | |\\/| |  _ _ _\n |_||_| |___|    \\__/   |_|\\_\\  |____|  |_|  |_| (_|_|_)"
      );
      ta1.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      ta1.setEditable(false);
      JLabel label = new JLabel("If your document fulfills this demand please continue to the next step.");
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JScrollPane(ta1), GridDialogLayoutData.FILL_BOTH);
      panel.add(label);
      return panel;
   }

   @Override
   public boolean canFinish() {
      return this.getModel().canFinish();
   }

   @Override
   public void requestFocus() {
   }
}
