package de.jave.figlet.swing.layout;

import de.jave.figlet.engine.layout.PrintDirection;
import de.jave.figlet.swing.ui.PrintDirectionUi;
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

public class PrintDirectionPanel {
   private final List buttons = new ArrayList();
   private final List directions = new ArrayList();
   private final JPanel panel;
   private final Map toggleButtonsByDirection = new HashMap();

   public PrintDirectionPanel(ActionListener listener) {
      ButtonGroup group = new ButtonGroup();
      this.panel = new JPanel(new FlowLayout(0, 0, 0));
      this.addAlignmentToggleButton(this.panel, group, PrintDirection.RIGHT_TO_LEFT, listener);
      this.addAlignmentToggleButton(this.panel, group, PrintDirection.LEFT_TO_RIGHT, listener);
      this.setSelectedDirection(PrintDirection.LEFT_TO_RIGHT);
   }

   public void setSelectedDirection(PrintDirection alignment) {
      JToggleButton button = (JToggleButton)this.toggleButtonsByDirection.get(alignment);
      if (button != null) {
         button.setSelected(true);
      } else {
         throw new RuntimeException();
      }
   }

   public PrintDirection getSelectedDirection() {
      for (int i = 0; i < this.buttons.size(); i++) {
         if (((JToggleButton)this.buttons.get(i)).isSelected()) {
            return (PrintDirection)this.directions.get(i);
         }
      }

      throw new RuntimeException();
   }

   private void addAlignmentToggleButton(JPanel panel, ButtonGroup group, PrintDirection direction, ActionListener listener) {
      PrintDirectionUi ui = new PrintDirectionUi();
      JToggleButton button = new JToggleButton(ui.getIcon(direction));
      button.setToolTipText(ui.getTooltipText(direction));
      button.addActionListener(listener);
      button.setPreferredSize(GuiUtilities.TOOLBAR_BUTTON_SIZE);
      button.setFocusPainted(false);
      group.add(button);
      panel.add(button);
      this.toggleButtonsByDirection.put(direction, button);
      this.buttons.add(button);
      this.directions.add(direction);
   }

   public JComponent getComponent() {
      return this.panel;
   }
}
