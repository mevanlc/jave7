package net.disy.commons.swing.fontchooser.view.accessory;

import javax.swing.JComponent;
import net.disy.commons.swing.fontchooser.model.FontModel;

public interface IFontChooserAccessory {
   void setModel(FontModel var1);

   JComponent getContent();

   void setEnabled(boolean var1);
}
