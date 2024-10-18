package de.jave.figlet.engine.primitives;

public class VerticalSmushingRules {
   private static final int EQUAL_CHARACTER_FLAG = 256;
   private static final int UNDERSCORE_FLAG = 512;
   private static final int HIERARCHY_FLAG = 1024;
   private static final int HORIZONTAL_LINE_FLAG = 2048;
   private static final int VERTICAL_LINE_FLAG = 4096;
   private static final int ALL_FLAGS_MASK = 7936;
   private int layout;

   public VerticalSmushingRules(int layout) {
      this.layout = layout;
   }

   public VerticalSmushingRules() {
      this.layout = 0;
   }

   public boolean isEqualCharacter() {
      return this.isSet(256);
   }

   public boolean isUnderscore() {
      return this.isSet(512);
   }

   public boolean isHierarchy() {
      return this.isSet(1024);
   }

   public boolean isHorizontalLine() {
      return this.isSet(2048);
   }

   public boolean isVerticalLine() {
      return this.isSet(4096);
   }

   public boolean isNoRulesSpecified() {
      return (this.layout & 7936) == 0;
   }

   public VerticalSmushingRules intersect(VerticalSmushingRules other) {
      return new VerticalSmushingRules(this.layout & other.layout);
   }

   public int getBitMask() {
      return this.layout & 7936;
   }

   private boolean isSet(int flag) {
      return (this.layout & flag) == flag;
   }

   private void set(int flag, boolean b) {
      this.layout = (this.layout | flag) - flag;
      if (b) {
         this.layout |= flag;
      }
   }

   public void setEqualCharacter(boolean b) {
      this.set(256, b);
   }

   public void setHierarchy(boolean b) {
      this.set(1024, b);
   }

   public void setUnderscore(boolean b) {
      this.set(512, b);
   }

   public void setHorizontalLine(boolean b) {
      this.set(2048, b);
   }

   public void setVerticalLine(boolean b) {
      this.set(4096, b);
   }

   @Override
   public Object clone() {
      return new VerticalSmushingRules(this.layout);
   }
}
