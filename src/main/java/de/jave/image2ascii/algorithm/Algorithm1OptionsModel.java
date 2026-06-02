package de.jave.image2ascii.algorithm;

import de.jave.image2ascii.SharedImage2AsciiOptions;
import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersModel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTableSelectionModel;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class Algorithm1OptionsModel extends AbstractChangeableModel {
   private final SharedImage2AsciiOptions sharedOptions;
   private boolean dithering = true;

   public Algorithm1OptionsModel(SharedImage2AsciiOptions sharedOptions) {
      Ensure.ensureArgumentNotNull(sharedOptions);
      this.sharedOptions = sharedOptions;
   }

   public GreyScaleTableSelectionModel getGreyScaleTableSelectionModel() {
      return this.sharedOptions.getGreyScaleTableSelectionModel();
   }

   public BannedCharactersModel getBannedCharactersModel() {
      return this.sharedOptions.getBannedCharactersModel();
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

   public boolean isDithering() {
      return this.dithering;
   }

   public void setDithering(boolean dithering) {
      if (this.dithering != dithering) {
         this.dithering = dithering;
         this.fireChangeEvent();
      }
   }

   public FontModel getDisplayFontModel() {
      return this.sharedOptions.getDisplayFontModel();
   }
}
