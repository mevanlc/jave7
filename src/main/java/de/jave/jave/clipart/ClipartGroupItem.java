package de.jave.jave.clipart;

import net.disy.commons.core.util.Ensure;

public class ClipartGroupItem {
   private final ClipartGroup clipartGroup;
   private final boolean existing;

   public ClipartGroupItem(ClipartGroup clipartGroup, boolean existing) {
      Ensure.ensureArgumentNotNull(clipartGroup);
      this.clipartGroup = clipartGroup;
      this.existing = existing;
   }

   public ClipartGroup getClipartGroup() {
      return this.clipartGroup;
   }

   public boolean isExisting() {
      return this.existing;
   }
}
