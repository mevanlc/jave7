package de.jave.image2ascii.algorithm;

import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersModel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTableSelectionModel;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class AlgorithmJavEOptionsModel extends AbstractChangeableModel {
   private final SharedImage2AsciiOptions sharedOptions;

   public AlgorithmJavEOptionsModel(SharedImage2AsciiOptions sharedOptions) {
      Ensure.ensureArgumentNotNull(sharedOptions);
      this.sharedOptions = sharedOptions;
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      super.addChangeListener(listener);
      this.sharedOptions.addChangeListener(listener);
   }

   @Override
   public void removeChangeListener(IChangeListener listener) {
      super.removeChangeListener(listener);
      this.sharedOptions.removeChangeListener(listener);
   }

   public BannedCharactersModel getBannedCharactersModel() {
      return this.sharedOptions.getBannedCharactersModel();
   }

   public GreyScaleTableSelectionModel getGreyScaleTableSelectionModel() {
      return this.sharedOptions.getGreyScaleTableSelectionModel();
   }

   public FontModel getDisplayFontModel() {
      return this.sharedOptions.getDisplayFontModel();
   }
}
