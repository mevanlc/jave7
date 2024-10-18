package net.disy.commons.swing.fontchooser.view.character;

import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontCharacterTextComponent {
   private final JTextField characterCodeTextField;
   private final JComponent content;
   private final FontCharacterChooserModel model;

   public FontCharacterTextComponent(FontCharacterChooserModel model) {
      this.model = model;
      this.characterCodeTextField = new JTextField(6);
      this.characterCodeTextField.setEditable(false);
      JPanel panel = new JPanel(new FlowLayout(0));
      panel.add(new JLabel(DisyCommonsSwingFontChooserMessages.getString("FontCharacterTextComponent.characterCode.label.text")));
      panel.add(this.characterCodeTextField);
      this.content = panel;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FontCharacterTextComponent.this.updateView();
         }
      });
      this.updateView();
   }

   public JComponent getContent() {
      return this.content;
   }

   private void updateView() {
      this.characterCodeTextField.setText(String.valueOf(this.model.getCharacter()));
   }
}
