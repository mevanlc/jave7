package net.disy.commons.swing.dialog.input;

import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;

public class SmartDialogPanelsBuildResult {
   private final ISmartDialogPanel[] panels;
   private final JPanel panel;

   public SmartDialogPanelsBuildResult(ISmartDialogPanel[] panels, JPanel panel) {
      Ensure.ensureArgumentNotNull(panels);
      Ensure.ensureArgumentArrayContentsNotNull(panels);
      Ensure.ensureArgumentNotNull(panel);
      this.panels = panels;
      this.panel = panel;
   }

   public ISmartDialogPanel[] getPanels() {
      return this.panels;
   }

   public JPanel getCompletePanel() {
      return this.panel;
   }
}
