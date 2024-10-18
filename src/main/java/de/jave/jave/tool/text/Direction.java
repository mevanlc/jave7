package de.jave.jave.tool.text;

public class Direction {
   public static final Direction RIGHT = new Direction(0);
   public static final Direction DOWN = new Direction(1);
   public static final Direction LEFT = new Direction(7);
   public static final Direction UP = new Direction(6);
   public static final Direction RIGHT_UP = new Direction(2);
   public static final Direction RIGHT_DOWN = new Direction(3);
   public static final Direction LEFT_UP = new Direction(4);
   public static final Direction LEFT_DOWN = new Direction(5);
   private final int value;

   private Direction(int value) {
      this.value = value;
   }

   public boolean isOpposite(Direction other) {
      return this.value + other.value == 7;
   }
}
