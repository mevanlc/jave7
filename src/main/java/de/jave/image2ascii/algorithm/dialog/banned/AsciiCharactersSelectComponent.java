package de.jave.image2ascii.algorithm.dialog.banned;

import de.jave.jave.JaveGlobalRessources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class AsciiCharactersSelectComponent {
   private static final Color SELECTED_COLOR = new Color(252, 228, 235);
   private static final Color NOT_SELECTED_COLOR = new Color(240, 240, 240);
   private final JLabel previewLabel;
   private final JCheckBox checkBox;
   private final JComponent component;
   private final AsciiCharacterSetModel model;
   private final char character;

   public AsciiCharactersSelectComponent(final AsciiCharacterSetModel model, final char character, Font font) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.character = character;
      String label = character + " " + character;
      if (character < 'd') {
         label = "0" + label;
      }

      this.previewLabel = new JLabel(String.valueOf(character), 0);
      this.previewLabel.setFont(font);
      this.previewLabel.setPreferredSize(new Dimension(20, 20));
      this.previewLabel.setOpaque(true);
      this.checkBox = new JCheckBox(label);
      this.checkBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setSelected(character, AsciiCharactersSelectComponent.this.checkBox.isSelected());
         }
      });
      this.checkBox.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      JPanel panel = new JPanel(new BorderLayout(0, 0));
      panel.add(this.checkBox, "West");
      panel.add(this.previewLabel, "Center");
      panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
      this.component = panel;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiCharactersSelectComponent.this.updateView();
         }
      });
      this.updateView();
   }

   private void updateView() {
      boolean selected = this.model.isSelected(this.character);
      this.checkBox.setSelected(selected);
      this.previewLabel.setBackground(selected ? SELECTED_COLOR : NOT_SELECTED_COLOR);
   }

   public JComponent getComponent() {
      return this.component;
   }
}
