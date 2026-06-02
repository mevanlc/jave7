package de.jave.jave.algorithm.replaceillegal;

import net.dizzy.commons.core.util.Ensure;

public class AsciiReplaceIllegalConfiguration {
   private final char[] replaceIllegalSource;
   private final char[] replaceIllegalDestination;

   public AsciiReplaceIllegalConfiguration(char[] replaceIllegalSource, char[] replaceIllegalDestination) {
      Ensure.ensureArgumentNotNull(replaceIllegalSource);
      Ensure.ensureArgumentNotNull(replaceIllegalDestination);
      this.replaceIllegalSource = replaceIllegalSource;
      this.replaceIllegalDestination = replaceIllegalDestination;
   }

   public char[] getReplaceIllegalSource() {
      return this.replaceIllegalSource;
   }

   public char[] getReplaceIllegalDestination() {
      return this.replaceIllegalDestination;
   }
}
