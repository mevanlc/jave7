package de.jave.jave.algorithm.repair;

import net.disy.commons.core.util.Ensure;

public class AsciiRepairAlgorithmConfiguration {
   private final AsciiRepairRule[] rules;
   private final int identical;
   private final int identicalLeft;
   private final int identicalRight;

   public AsciiRepairAlgorithmConfiguration(AsciiRepairRule[] rules, int identical, int identicalLeft, int identicalRight) {
      Ensure.ensureArgumentNotNull(rules);
      this.rules = rules;
      this.identical = identical;
      this.identicalLeft = identicalLeft;
      this.identicalRight = identicalRight;
   }

   public int getIdentical() {
      return this.identical;
   }

   public int getIdenticalLeft() {
      return this.identicalLeft;
   }

   public int getIdenticalRight() {
      return this.identicalRight;
   }

   public AsciiRepairRule[] getRules() {
      return this.rules;
   }
}
