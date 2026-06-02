package net.dizzy.commons.swing.fontchooser.view.fixedwidth;

import java.awt.Component;

import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.fontchooser.view.FontChooserDialog;

public final class FixedWidthFontChooserDialogFactory {
   private static final FixedWidthFontChooserDialogFactory INSTANCE = new FixedWidthFontChooserDialogFactory();

   private FixedWidthFontChooserDialogFactory() {
   }

   public static FixedWidthFontChooserDialogFactory getInstance() {
      return INSTANCE;
   }

   public static FontChooserDialog createDialog(Component parent, FontModel model) {
      return new FontChooserDialog(parent, model);
   }

   public FontChooserDialog createFontChooserDialog(Component parent, FontModel model) {
      return new FontChooserDialog(parent, model);
   }
}
