package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class DefaultFontChooserDialogFactory implements IFontChooserDialogFactory {
   private static final DefaultFontChooserDialogFactory instance = new DefaultFontChooserDialogFactory();

   public static DefaultFontChooserDialogFactory getInstance() {
      return instance;
   }

   protected DefaultFontChooserDialogFactory() {
   }

   @Override
   public FontChooserDialog createFontChooserDialog(Component parentComponent, FontModel model) {
      return new FontChooserDialog(parentComponent, model);
   }
}
