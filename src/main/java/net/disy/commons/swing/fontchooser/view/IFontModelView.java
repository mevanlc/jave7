package net.disy.commons.swing.fontchooser.view;

import javax.swing.JComponent;
import net.disy.commons.swing.fontchooser.model.FontModel;

public interface IFontModelView {
   JComponent getContent();

   FontModel getFontModel();

   void setEnabled(boolean var1);

   boolean isEnabled();
}
