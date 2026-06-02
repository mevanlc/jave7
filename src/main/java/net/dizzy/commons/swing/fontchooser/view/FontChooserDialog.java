package net.dizzy.commons.swing.fontchooser.view;

import java.awt.Component;
import java.awt.Font;

import net.dizzy.commons.swing.dialog.core.DialogResult;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class FontChooserDialog {
   private final FontModel model;

   public FontChooserDialog(Component parent, FontModel model) {
      this.model = model;
   }

   public IDialogResult show() {
      return DialogResult.OK;
   }

   public IDialogResult show(String title) {
      return show();
   }

   public Font getFont() {
      return model.getFont();
   }
}
