package de.jave.image2ascii.algorithm;

import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersModel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTableSelectionModel;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class Algorithm4OptionsModel extends AbstractChangeableModel {
   private final SharedImage2AsciiOptions sharedOptions;
   private boolean optimize = true;

   public Algorithm4OptionsModel(SharedImage2AsciiOptions sharedOptions) {
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

   public boolean isOptimize() {
      return this.optimize;
   }

   public void setOptimize(boolean optimize) {
      if (this.optimize != optimize) {
         this.optimize = optimize;
         this.fireChangeEvent();
      }
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
