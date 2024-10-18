package net.disy.commons.swing.dialog.core.preferences;

import java.awt.Rectangle;
import javax.swing.JFrame;
import net.disy.commons.core.util.Ensure;

public class FrameBoundsState {
   private final int extendedFrameState;
   private final Rectangle bounds;

   public static FrameBoundsState createFrom(JFrame frame) {
      return new FrameBoundsState(frame.getBounds(), frame.getExtendedState());
   }

   public FrameBoundsState(Rectangle bounds, int extendedFrameState) {
      Ensure.ensureArgumentNotNull(bounds);
      this.bounds = bounds;
      this.extendedFrameState = extendedFrameState;
   }

   public Rectangle getBounds() {
      return this.bounds;
   }

   public int getExtendedFrameState() {
      return this.extendedFrameState;
   }

   public void applyTo(JFrame frame) {
      frame.setBounds(this.bounds);
      frame.setExtendedState(this.extendedFrameState);
   }
}
