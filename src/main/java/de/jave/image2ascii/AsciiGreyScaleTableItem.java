package de.jave.image2ascii;

import net.dizzy.commons.core.util.Ensure;

public class AsciiGreyScaleTableItem {
   private final AsciiGreyscaleTable greyscaleTable;
   private final String name;

   public AsciiGreyScaleTableItem(String name, AsciiGreyscaleTable greyscaleTable) {
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(greyscaleTable);
      this.name = name;
      this.greyscaleTable = greyscaleTable;
   }

   public String getName() {
      return this.name;
   }

   public AsciiGreyscaleTable getGreyscaleTable() {
      return this.greyscaleTable;
   }
}
