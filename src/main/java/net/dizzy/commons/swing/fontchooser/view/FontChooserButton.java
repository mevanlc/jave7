package net.dizzy.commons.swing.fontchooser.view;

import javax.swing.JButton;
import javax.swing.JComponent;

import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.fontchooser.view.fixedwidth.FixedWidthFontChooserDialogFactory;

public class FontChooserButton implements IComponentContainer {
   private final JButton button;

   public FontChooserButton(FontModel model) {
      button = new JButton(model.getFontName());
      button.addActionListener(event -> {
         FontChooserDialog dialog = new FontChooserDialog(button, model);
         IDialogResult result = dialog.show();
         if (!result.isCanceled()) {
            button.setText(model.getFontName());
         }
      });
   }

   public FontChooserButton(FontModel model, FixedWidthFontChooserDialogFactory factory) {
      button = new JButton(model.getFontName());
      button.addActionListener(event -> {
         FontChooserDialog dialog = factory.createFontChooserDialog(button, model);
         IDialogResult result = dialog.show();
         if (!result.isCanceled()) {
            button.setText(model.getFontName());
         }
      });
   }

   @Override public JComponent getContent() { return button; }

   public void setEnabled(boolean enabled) {
      button.setEnabled(enabled);
   }
}
