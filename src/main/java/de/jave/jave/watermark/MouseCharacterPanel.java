package de.jave.jave.plate;

import de.jave.gui.CharField;
import de.jave.gui.CharacterModel;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.icon.JaveIcons;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;

public class MouseCharacterPanel {
   private final CharacterModel cf1 = new CharacterModel();
   private final CharacterModel cf2 = new CharacterModel();
   private final CharField cfChar1 = new CharField(this.cf1);
   private final CharField cfChar2;
   private final JLabel iconLabel;
   private final JComponent content;

   public MouseCharacterPanel(final MouseCharacterModel model) {
      this.cfChar1.setToolTipText("Character on Left Mouse Button");
      this.cfChar1.setFont(JaveGlobalRessources.FONT_DEFAULT);
      this.cfChar1.setBackground(JaveGlobalRessources.COLOR_CHARFIELD_BACK);
      this.cf1.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            model.setCharacter1(MouseCharacterPanel.this.cf1.getCharacter());
         }
      });
      this.iconLabel = new JLabel(JaveIcons.MOUSE_ICON);
      this.iconLabel.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent evt) {
            if (MouseCharacterPanel.this.iconLabel.isEnabled()) {
               char ch1 = MouseCharacterPanel.this.cf1.getCharacter();
               char ch2 = MouseCharacterPanel.this.cf2.getCharacter();
               model.setCharacter1(ch2);
               model.setCharacter2(ch1);
            }
         }
      });
      this.cfChar2 = new CharField(this.cf2);
      this.cfChar2.setToolTipText("Character on Right Mouse Button");
      this.cfChar2.setFont(JaveGlobalRessources.FONT_DEFAULT);
      this.cfChar2.setBackground(JaveGlobalRessources.COLOR_CHARFIELD_BACK);
      this.cf2.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            model.setCharacter2(MouseCharacterPanel.this.cf2.getCharacter());
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(3, false, 0, 0));
      panel.add(this.cfChar1);
      panel.add(this.iconLabel);
      panel.add(this.cfChar2);
      this.content = panel;
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            MouseCharacterPanel.this.updateView(model);
         }
      });
      this.updateView(model);
   }

   private void updateView(MouseCharacterModel model) {
      this.cf1.setCharacter(model.getCharacter1());
      this.cf2.setCharacter(model.getCharacter2());
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setEnabled(boolean enabled) {
      this.cfChar1.setEnabled(enabled);
      this.cfChar2.setEnabled(enabled);
      this.iconLabel.setEnabled(enabled);
   }
}
