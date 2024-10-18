package de.jave.figlet.engine.primitives;

import de.jave.lib.IPublicCloneable;

public class HorizontalSmushingRules implements IPublicCloneable {
   private static final int EQUAL_CHARACTER_VALUE = 1;
   private static final int UNDERSCORE_FLAG = 2;
   private static final int HIERARCHY_FLAG = 4;
   private static final int OPPOSITE_PAIR_FLAG = 8;
   private static final int BIG_X_FLAG = 16;
   private static final int HARDBLANK_FLAG = 32;
   private static final int ALL_FLAGS_MASK = 63;
   private int layout;

   public HorizontalSmushingRules(int layout) {
      this.layout = layout;
   }

   public HorizontalSmushingRules() {
      this.layout = 0;
   }

   public boolean isEqualCharacter() {
      return this.isSet(1);
   }

   public boolean isUnderscore() {
      return this.isSet(2);
   }

   public boolean isHierarchy() {
      return this.isSet(4);
   }

   public boolean isOppositePair() {
      return this.isSet(8);
   }

   public boolean isBigX() {
      return this.isSet(16);
   }

   public boolean isHardblank() {
      return this.isSet(32);
   }

   public boolean isNoRulesSpecified() {
      return (this.layout & 63) == 0;
   }

   public HorizontalSmushingRules intersect(HorizontalSmushingRules other) {
      return new HorizontalSmushingRules(this.layout & other.layout);
   }

   public int getBitMask() {
      return this.layout & 63;
   }

   private boolean isSet(int flag) {
      return (this.layout & flag) == flag;
   }

   public void setEqualCharacter(boolean b) {
      this.set(1, b);
   }

   private void set(int flag, boolean b) {
      this.layout = (this.layout | flag) - flag;
      if (b) {
         this.layout |= flag;
      }
   }

   public void setUnderscore(boolean b) {
      this.set(2, b);
   }

   public void setHierarchy(boolean b) {
      this.set(4, b);
   }

   public void setOppositePair(boolean b) {
      this.set(8, b);
   }

   public void setBigX(boolean b) {
      this.set(16, b);
   }

   public void setHardblank(boolean b) {
      this.set(32, b);
   }

   @Override
   public Object clone() {
      return new HorizontalSmushingRules(this.layout);
   }
}
