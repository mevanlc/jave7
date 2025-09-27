package net.disy.commons.swing.fontchooser.view.fixedwidth;

import java.awt.Component;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.FontChooserDialog;
import net.disy.commons.swing.fontchooser.view.FontChooserPanel;
import net.disy.commons.swing.fontchooser.view.IFontChooserDialogFactory;

public class FixedWidthFontChooserDialogFactory implements IFontChooserDialogFactory {
   private static final FixedWidthFontChooserDialogFactory instance = new FixedWidthFontChooserDialogFactory();

   public static FixedWidthFontChooserDialogFactory getInstance() {
      return instance;
   }

   @Override
   public FontChooserDialog createFontChooserDialog(Component parentComponent, FontModel model) {
      FontChooserDialog dialog = new FontChooserDialog(parentComponent, model);
      FontChooserPanel panel = dialog.getFontChooserPanel();
      panel.setAccessory(new FixedWidthOptionFontChooserAccessory(panel));
      return dialog;
   }
}
