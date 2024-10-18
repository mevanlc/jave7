package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import net.disy.commons.swing.fontchooser.model.FontModel;

public interface IFontChooserDialogFactory {
   FontChooserDialog createFontChooserDialog(Component var1, FontModel var2);
}
