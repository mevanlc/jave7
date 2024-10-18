package net.jmge.gif;

public enum GifFrameDisposeMode {
   UNDEFINED(0),
   LEAVE(1),
   BGCOLOR(2),
   REVERT(3);

   private final int value;

   private GifFrameDisposeMode(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
