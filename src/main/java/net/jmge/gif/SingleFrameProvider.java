package net.jmge.gif;

import net.dizzy.commons.core.util.Ensure;

public class SingleFrameProvider implements IGifFrameProvider {
   private final Gif89Frame frame;

   public SingleFrameProvider(Gif89Frame frame) {
      Ensure.ensureArgumentNotNull(frame);
      this.frame = frame;
   }

   @Override
   public Gif89Frame get(int index) {
      return this.frame;
   }

   @Override
   public int getSize() {
      return 1;
   }
}
