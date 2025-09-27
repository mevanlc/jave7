package net.disy.commons.swing.dialog.animation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.disy.commons.core.util.Ensure;

public class AnimatedCompositeComponent extends JPanel {
   private boolean overlaidComponentVisible;
   private int overlayPosition = 0;
   private final Timer timer;

   public AnimatedCompositeComponent(JComponent baseComponent, JComponent overlaidComponent) {
      Ensure.ensureArgumentNotNull(baseComponent);
      Ensure.ensureArgumentNotNull(overlaidComponent);
      this.setLayout(new AnimatedCompositeLayout(baseComponent, overlaidComponent));
      this.add(overlaidComponent);
      this.add(baseComponent);
      this.timer = new Timer(15, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AnimatedCompositeComponent.this.updateAnimation();
         }
      });
      this.timer.setInitialDelay(0);
   }

   public void setOverlaidComponentVisible(boolean overlaidComponentVisible) {
      if (overlaidComponentVisible != this.overlaidComponentVisible) {
         this.overlaidComponentVisible = overlaidComponentVisible;
         this.overlayPosition = this.getHeight() - this.overlayPosition;
         this.timer.start();
      }
   }

   private void updateAnimation() {
      if (this.oneStep()) {
         this.revalidate();
      } else {
         this.timer.stop();
      }
   }

   private boolean oneStep() {
      if (this.overlayPosition <= 0) {
         this.overlayPosition = 0;
         return false;
      } else {
         this.overlayPosition--;
         return true;
      }
   }

   public boolean isOverlayVisible() {
      return this.overlaidComponentVisible;
   }

   public int getOverlayPosition() {
      return this.overlayPosition;
   }
}
