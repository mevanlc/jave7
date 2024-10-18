package de.jave.image2ascii.algorithm.kicad;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.ObjectUtilities;

public class KiCadOptionsModel extends AbstractChangeableModel {
   public static final String DEFAULT_MODULE_NAME = "Logo";
   private String moduleName = "Logo";
   private double widthInInch = 1.0;

   public String getModuleName() {
      return this.moduleName;
   }

   public void setModuleName(String moduleName) {
      if (!ObjectUtilities.equals(this.moduleName, moduleName)) {
         this.moduleName = moduleName;
         this.fireChangeEvent();
      }
   }

   public double getWidthInInch() {
      return this.widthInInch;
   }

   public void setWidthInInch(double widthInInch) {
      if (this.widthInInch != widthInInch) {
         this.widthInInch = widthInInch;
         this.fireChangeEvent();
      }
   }
}
