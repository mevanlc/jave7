package net.dizzy.commons.swing.fontchooser.view;

import javax.swing.JButton;
import javax.swing.JComponent;

import net.dizzy.commons.swing.component.IComponentContainer;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class FontChooserButton implements IComponentContainer {
   private final JButton button;

   public FontChooserButton(FontModel model) {
      button = new JButton(model.getFontName());
      button.addActionListener(event -> {
         FontChooserDialog dialog = new FontChooserDialog(button, model);
         dialog.show();
      });
   }

   public FontChooserButton(FontModel model, net.dizzy.commons.swing.fontchooser.view.fixedwidth.FixedWidthFontChooserDialogFactory factory) {
      this(model);
   }

   @Override public JComponent getContent() { return button; }

   public void setEnabled(boolean enabled) {
      button.setEnabled(enabled);
   }
}
