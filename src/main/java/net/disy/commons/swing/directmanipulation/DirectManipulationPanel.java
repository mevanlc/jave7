package net.disy.commons.swing.directmanipulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.events.mouse.OverallMouseListeningPanel;

@Deprecated
public class DirectManipulationPanel {
   private final JComponent content;
   private final DirectManipulationObjectModel model = new DirectManipulationObjectModel();

   public DirectManipulationPanel(JComponent content, final IDirectManipulationProvider directManipulationProvider) {
      final JPanel panel = new OverallMouseListeningPanel(content) {
         @Override
         protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            DirectManipulationObject directManipulationObject = DirectManipulationPanel.this.model.getDirectManipulationObject();
            if (directManipulationObject != null) {
               IManipulationMaker[] markers = directManipulationObject.getMarkerProvider().getMarkers();
               g.setColor(Color.black);
               g.setXORMode(Color.white);

               for (int i = 0; i < markers.length; i++) {
                  this.paintMarker(g, markers[i].getPoint());
               }

               g.setPaintMode();
            }
         }

         private void paintMarker(Graphics g, Point point) {
            g.fillRect(point.x - 2, point.y - 2, 5, 5);
         }
      };
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            panel.repaint();
         }
      });
      panel.addMouseListener(
         new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               this.adjustDirectManipulationObject(e);
            }

            private void adjustDirectManipulationObject(MouseEvent e) {
               DirectManipulationObject directManipulationObject = directManipulationProvider.getDirectManipulationObject(e.getPoint());
               DirectManipulationPanel.this.model.setDirectManipulationObject(directManipulationObject);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               this.adjustDirectManipulationObject(e);
               if (DirectManipulationPanel.this.model.getDirectManipulationObject() != null) {
                  if (e.isMetaDown()) {
                     directManipulationProvider.handleContextMenuInvoked(
                        DirectManipulationPanel.this.getContent(), e.getPoint(), DirectManipulationPanel.this.model.getDirectManipulationObject()
                     );
                  } else if (e.getClickCount() == 2) {
                     directManipulationProvider.handleDoubleClick(
                        DirectManipulationPanel.this.getContent(), e.getPoint(), DirectManipulationPanel.this.model.getDirectManipulationObject()
                     );
                  }
               }
            }
         }
      );
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }
}
