package de.jave.image2ascii.algorithm.dialog.greyscaletable;

import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.DynamicalGreyScaleTableProvider;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class GreyScaleTableSelectionModel extends AbstractChangeableModel {
   private boolean automaticFromFont = false;
   private AsciiGreyscaleTable greyscaleTable;

   public GreyScaleTableSelectionModel(AsciiGreyscaleTableConfiguration configuration) {
      this.greyscaleTable = configuration.getDefaultTable();
   }

   public void setAutomaticFromFont(boolean automaticFromFont) {
      if (this.automaticFromFont != automaticFromFont) {
         this.automaticFromFont = automaticFromFont;
         this.fireChangeEvent();
      }
   }

   public boolean isAutomaticFromFont() {
      return this.automaticFromFont;
   }

   public void setGreyScaleTable(AsciiGreyscaleTable greyscaleTable) {
      Ensure.ensureArgumentNotNull(greyscaleTable);
      if (this.greyscaleTable != greyscaleTable) {
         this.greyscaleTable = greyscaleTable;
         this.fireChangeEvent();
      }
   }

   public AsciiGreyscaleTable getGreyscaleTable() {
      return this.greyscaleTable;
   }

   public AsciiGreyscaleTable getActualGreyscaleTable(FontModel fontModel) {
      return this.automaticFromFont ? DynamicalGreyScaleTableProvider.getGreyScaleTable(fontModel.getFont()) : this.greyscaleTable;
   }
}
