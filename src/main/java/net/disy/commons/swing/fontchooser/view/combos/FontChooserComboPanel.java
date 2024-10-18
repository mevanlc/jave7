package net.disy.commons.swing.fontchooser.view.combos;

import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.IFontModelView;

public class FontChooserComboPanel implements IFontModelView {
   private final JComponent content;
   private final FontChooserComboComponents allComponents;

   public FontChooserComboPanel(FontModel fontModel) {
      this.allComponents = new FontChooserComboComponents(fontModel);
      this.content = this.createContent();
   }

   private JComponent createContent() {
      JPanel panel = new JPanel();
      JComponent[] components = this.allComponents.getComponents();

      for (int index = 0; index < components.length; index++) {
         panel.add(components[index]);
      }

      return panel;
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public FontModel getFontModel() {
      return this.allComponents.getFontModel();
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.allComponents.setEnabled(enabled);
   }

   @Override
   public boolean isEnabled() {
      return this.allComponents.isEnabled();
   }
}
