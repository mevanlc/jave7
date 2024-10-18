package net.disy.commons.swing.dialog.input;

import java.util.ArrayList;
import java.util.List;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class SmartDialogPanelsBuilder implements ISmartDialogPanelsBuilder {
   private final GridDialogPanelBuilder builder = new GridDialogPanelBuilder();
   private final List<ISmartDialogPanel> allPanels = new ArrayList<>();

   @Override
   public void add(ISmartDialogPanel... panels) {
      for (ISmartDialogPanel panel : panels) {
         this.addPanel(panel);
      }
   }

   @Deprecated
   @Override
   public void add(Iterable<ISmartDialogPanel> panels) {
      for (ISmartDialogPanel panel : panels) {
         this.addPanel(panel);
      }
   }

   private void addPanel(ISmartDialogPanel panel) {
      this.builder.add(panel);
      this.allPanels.add(panel);
   }

   @Override
   public void addVerticalComponentSpacing() {
      this.addVerticalSpacing(LayoutUtilities.getComponentSpacing());
   }

   private void addVerticalSpacing(int height) {
      this.builder.addVerticalSpacing(height);
   }

   public SmartDialogPanelsBuildResult createResult() {
      return new SmartDialogPanelsBuildResult(this.allPanels.toArray(new ISmartDialogPanel[0]), this.builder.createPanel());
   }
}
