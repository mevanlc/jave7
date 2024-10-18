package de.jave.figlet.swing.layout;

import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.swing.ui.HorizontalAlignmentUi;
import de.jave.lib.gui.GuiUtilities;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class HorizontalAlignmentPanel {
   private final List buttons = new ArrayList();
   private final List alignments = new ArrayList();
   private final JPanel panel;
   private final Map toggleButtonsByAlignment = new HashMap();

   public HorizontalAlignmentPanel(ActionListener listener) {
      ButtonGroup group = new ButtonGroup();
      this.panel = new JPanel(new FlowLayout(0, 0, 0));
      this.addAlignmentToggleButton(this.panel, group, HorizontalAlignment.LEFT, listener);
      this.addAlignmentToggleButton(this.panel, group, HorizontalAlignment.CENTER, listener);
      this.addAlignmentToggleButton(this.panel, group, HorizontalAlignment.RIGHT, listener);
      this.setSelectedAlignment(HorizontalAlignment.LEFT);
   }

   public void setSelectedAlignment(HorizontalAlignment alignment) {
      JToggleButton button = (JToggleButton)this.toggleButtonsByAlignment.get(alignment);
      if (button != null) {
         button.setSelected(true);
      } else {
         throw new RuntimeException();
      }
   }

   public HorizontalAlignment getSelectedAlignment() {
      for (int i = 0; i < this.buttons.size(); i++) {
         if (((JToggleButton)this.buttons.get(i)).isSelected()) {
            return (HorizontalAlignment)this.alignments.get(i);
         }
      }

      throw new RuntimeException();
   }

   private void addAlignmentToggleButton(JPanel panel, ButtonGroup group, HorizontalAlignment alignment, ActionListener listener) {
      JToggleButton button = new JToggleButton(new HorizontalAlignmentUi().getIcon(alignment));
      button.addActionListener(listener);
      button.setPreferredSize(GuiUtilities.TOOLBAR_BUTTON_SIZE);
      button.setFocusPainted(false);
      group.add(button);
      panel.add(button);
      this.toggleButtonsByAlignment.put(alignment, button);
      this.buttons.add(button);
      this.alignments.add(alignment);
   }

   public JComponent getComponent() {
      return this.panel;
   }
}
