package net.disy.commons.swing.list;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

public class JListMouseListener {
   private final MouseAdapter mouseListener;
   private final JList list;

   private JListMouseListener(final JList list, final IListMouseHandler handler) {
      this.list = list;
      this.mouseListener = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent event) {
            if (event.isMetaDown() && this.isClickOnListItem(list, event)) {
               int index = list.locationToIndex(event.getPoint());
               if (!list.isSelectedIndex(index)) {
                  list.setSelectedIndex(index);
               }

               handler.handleContextMenuClick(list.getSelectedValues());
            }
         }

         @Override
         public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2 && this.isClickOnListItem(list, event)) {
               handler.handleDoubleClick(list.getSelectedValues());
            }
         }

         private boolean isClickOnListItem(JList jList, MouseEvent event) {
            boolean state = true;
            int index = jList.locationToIndex(event.getPoint());
            if (index == -1) {
               state = false;
            }

            if (!jList.getCellBounds(index, index).contains(event.getPoint())) {
               state = false;
            }

            return state;
         }
      };
      list.addMouseListener(this.mouseListener);
   }

   public void dispose() {
      this.list.removeMouseListener(this.mouseListener);
   }

   public static JListMouseListener attachTo(JList list, IListMouseHandler handler) {
      return new JListMouseListener(list, handler);
   }
}
