package de.jave.asciimation.editor;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class FrameListPanel {
   private final JComponent content;
   private final int lastSelectedFrameIndex = -1;
   private final JList frameList;

   public FrameListPanel(final AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.frameList = new JList(new FrameListModel(model));
      this.frameList.setCellRenderer(new JaveAnimationFrameListCellRenderer(model));
      this.frameList.setSelectionModel(model.getFrameSelectionModel());
      this.frameList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            int i = FrameListPanel.this.frameList.getSelectedIndex();
            if (i == -1) {
               i = -1;
            }

            if (i != -1) {
               model.getCurrentFrameIndexModel().setCurrentFrameIndex(i);
            }
         }
      });
      this.frameList.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            if (evt.isMetaDown() || evt.isPopupTrigger()) {
               int index = FrameListPanel.this.frameList.locationToIndex(evt.getPoint());
               if (index != -1 && !FrameListPanel.this.frameList.getSelectionModel().isSelectedIndex(index)) {
                  FrameListPanel.this.frameList.getSelectionModel().setSelectionInterval(index, index);
               }

               JPopupMenu popupMenu = AnimationEditorPopupMenuFactory.createFramePopupMenu(model);
               popupMenu.show(FrameListPanel.this.frameList, evt.getX(), evt.getY());
            }
         }
      });
      JPanel frameListPanel = new JPanel(new BorderLayout(1, 1));
      frameListPanel.add(new JLabel("Frames:"), "North");
      frameListPanel.add(new JScrollPane(this.frameList), "Center");
      this.content = frameListPanel;
      model.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FrameListPanel.this.frameList.ensureIndexIsVisible(model.getCurrentFrameIndexModel().getCurrentFrameIndex());
            FrameListPanel.this.frameList.repaint();
         }
      });
   }

   public JComponent getContent() {
      return this.content;
   }
}
