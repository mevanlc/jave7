package de.jave.image2ascii;

import de.jave.image2ascii.algorithm.dialog.banned.BannedCharactersModel;
import de.jave.image2ascii.algorithm.dialog.greyscaletable.GreyScaleTableSelectionModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class SharedImage2AsciiOptions {
   private final BannedCharactersModel bannedCharactersModel = new BannedCharactersModel();
   private final GreyScaleTableSelectionModel greyScaleTableSelectionModel;
   private final FontModel displayFontModel;

   public SharedImage2AsciiOptions(FontModel displayFontModel, AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.displayFontModel = displayFontModel;
      this.greyScaleTableSelectionModel = new GreyScaleTableSelectionModel(greyscaleTableConfiguration);
   }

   public FontModel getDisplayFontModel() {
      return this.displayFontModel;
   }

   public BannedCharactersModel getBannedCharactersModel() {
      return this.bannedCharactersModel;
   }

   public GreyScaleTableSelectionModel getGreyScaleTableSelectionModel() {
      return this.greyScaleTableSelectionModel;
   }

   public void addChangeListener(IChangeListener listener) {
      this.bannedCharactersModel.addChangeListener(listener);
      this.greyScaleTableSelectionModel.addChangeListener(listener);
      this.displayFontModel.addChangeListener(listener);
   }

   public void removeChangeListener(IChangeListener listener) {
      this.bannedCharactersModel.removeChangeListener(listener);
      this.greyScaleTableSelectionModel.removeChangeListener(listener);
      this.displayFontModel.removeChangeListener(listener);
   }
}
