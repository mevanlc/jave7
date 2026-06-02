package net.dizzy.commons.swing.events.mouse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class OverallMouseListeningPanel extends JPanel {
   public OverallMouseListeningPanel(JComponent content) {
      super(new BorderLayout());
      add(content, BorderLayout.CENTER);
      installForwarding(content);
   }

   private void installForwarding(Component component) {
      component.addMouseListener(new MouseListener() {
         @Override public void mouseClicked(MouseEvent event) { dispatch(event); }
         @Override public void mousePressed(MouseEvent event) { dispatch(event); }
         @Override public void mouseReleased(MouseEvent event) { dispatch(event); }
         @Override public void mouseEntered(MouseEvent event) { dispatch(event); }
         @Override public void mouseExited(MouseEvent event) { dispatch(event); }
      });
      component.addMouseMotionListener(new MouseMotionListener() {
         @Override public void mouseDragged(MouseEvent event) { dispatch(event); }
         @Override public void mouseMoved(MouseEvent event) { dispatch(event); }
      });
   }

   private void dispatch(MouseEvent event) {
      dispatchEvent(javax.swing.SwingUtilities.convertMouseEvent(event.getComponent(), event, this));
   }
}
