package de.jave.figlet.swing.layout;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

public abstract class AbstractLayoutPanel {
   private static final int MIN_SUPERSMUSHING_DEPTH = 0;
   private static final int MAX_SUPERSMUSHING_DEPTH = 12;
   private final Map superSmushingDepthRadioButtonsByDepth = new HashMap();

   protected JPopupMenu createSupersmushingDepthPopupMenu(ActionListener listener) {
      JPopupMenu menu = new JPopupMenu();
      ButtonGroup group = new ButtonGroup();

      for (int depth = 0; depth <= 12; depth++) {
         JRadioButton button = new JRadioButton(String.valueOf(depth));
         button.setToolTipText("Supersmushing depth " + depth);
         button.addActionListener(listener);
         menu.add(button);
         group.add(button);
         this.superSmushingDepthRadioButtonsByDepth.put(depth, button);
      }

      return menu;
   }

   protected void setSelectedSupersmushingDepth(int depth) {
      while (true) {
         JRadioButton button = (JRadioButton)this.superSmushingDepthRadioButtonsByDepth.get(depth);
         if (button != null) {
            button.setSelected(true);
            return;
         }

         depth--;
      }
   }

   protected int getSelectedSuperSmushingDepth() {
      for (int depth = 0; depth <= 12; depth++) {
         JRadioButton button = (JRadioButton)this.superSmushingDepthRadioButtonsByDepth.get(depth);
         if (button.isSelected()) {
            return depth;
         }
      }

      return 12;
   }
}
