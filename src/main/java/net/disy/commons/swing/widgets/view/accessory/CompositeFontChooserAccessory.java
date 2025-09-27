package net.disy.commons.swing.fontchooser.view.accessory;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class CompositeFontChooserAccessory implements IFontChooserAccessory {
   private final IFontChooserAccessory[] accessories;
   private final JComponent content;

   public CompositeFontChooserAccessory(IFontChooserAccessory[] accessories) {
      Ensure.ensureArgumentNotNull(accessories);
      Ensure.ensureArgumentArrayContentsNotNull(accessories);
      this.accessories = accessories;
      JPanel panel = new JPanel(new GridDialogLayout(1, false));

      for (int i = 0; i < accessories.length; i++) {
         panel.add(accessories[i].getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
      }

      this.content = panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void setEnabled(boolean enabled) {
      for (int i = 0; i < this.accessories.length; i++) {
         this.accessories[i].setEnabled(enabled);
      }
   }

   @Override
   public void setModel(FontModel model) {
      for (int i = 0; i < this.accessories.length; i++) {
         this.accessories[i].setModel(model);
      }
   }
}
