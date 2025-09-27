package net.disy.commons.swing.fontchooser.view.character;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class FontCharacterChooserPanel {
   private final JComponent content;
   private final FontCharacterButtonComponent buttonComponent;

   public FontCharacterChooserPanel(FontCharacterChooserModel model) {
      this.buttonComponent = new FontCharacterButtonComponent(model);
      FontCharacterTextComponent textComponent = new FontCharacterTextComponent(model);
      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      panel.add(this.buttonComponent.getContent(), "Center");
      panel.add(textComponent.getContent(), "South");
      this.content = panel;
   }

   public void addActionListener(ActionListener listener) {
      this.buttonComponent.addActionListener(listener);
   }

   public void removeActionListener(ActionListener listener) {
      this.buttonComponent.removeActionListener(listener);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void requestFocus() {
      this.buttonComponent.requestFocus();
   }
}
