package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.accessory.IFontChooserAccessory;

public class AccessoryFontChooserDialogFactory implements IFontChooserDialogFactory {
   private final IFontChooserAccessory fontChooserAccessory;

   public AccessoryFontChooserDialogFactory(IFontChooserAccessory fontChooserAccessory) {
      Ensure.ensureArgumentNotNull(fontChooserAccessory);
      this.fontChooserAccessory = fontChooserAccessory;
   }

   @Override
   public FontChooserDialog createFontChooserDialog(Component parentComponent, FontModel model) {
      FontChooserDialog dialog = new FontChooserDialog(parentComponent, model);
      dialog.getFontChooserPanel().setAccessory(this.fontChooserAccessory);
      return dialog;
   }
}
