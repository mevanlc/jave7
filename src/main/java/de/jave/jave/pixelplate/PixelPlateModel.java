package de.jave.jave.pixelplate;

import net.dizzy.commons.core.model.ObjectModel;

/**
 * Shared state for the six generic drawing tools — pencil size, line
 * style (when thin), and felt-pen character (when thick). One instance
 * is owned at application level; each generic tool's inline options
 * view binds to the same model so the user's choices survive tool
 * switches.
 *
 * <p>This class is the data half of what {@code PixelPlateOptionsPanel}
 * used to mix together. The view half lives in
 * {@code PixelPlateOptionsView}.
 */
public final class PixelPlateModel {
   private final ObjectModel<PencilSize> sizeModel = new ObjectModel<>(PencilSize.THIN);
   private final ObjectModel<LineStyle> lineStyleModel = new ObjectModel<>(LineStyle.LINE);
   private final ObjectModel<Character> feltPenStyleModel = new ObjectModel<>(PixelPlate.FELTPEN_CHARS[PixelPlate.DEFAULT_FELTPEN_CHAR_INDEX]);

   public ObjectModel<PencilSize> getSizeModel() {
      return this.sizeModel;
   }

   public ObjectModel<LineStyle> getLineStyleModel() {
      return this.lineStyleModel;
   }

   public ObjectModel<Character> getFeltPenStyleModel() {
      return this.feltPenStyleModel;
   }

   public boolean isLineMode() {
      return this.sizeModel.getValue() == PencilSize.THIN;
   }

   public boolean isFeltpenMode() {
      return !this.isLineMode();
   }

   public double getFeltpenPreviewDiameter() {
      switch (this.sizeModel.getValue()) {
         case THIN:
            return 1.0;
         case THICK1:
            return 1.9;
         case THICK2:
            return 2.5;
         case THICK3:
            return 3.2;
         case THICK4:
            return 4.0;
         default:
            throw new IllegalStateException();
      }
   }

   public void configure(PixelPlate pixelPlate) {
      PencilSize size = this.sizeModel.getValue();
      if (size == PencilSize.THIN) {
         switch (this.lineStyleModel.getValue()) {
            case LINE:
               pixelPlate.setMode(PixelPlateMode.PIXEL);
               break;
            case DOT:
               pixelPlate.setMode(PixelPlateMode.DOT);
               break;
            default:
               throw new IllegalStateException();
         }
      } else {
         pixelPlate.setMode(new PixelPlateFeltPenMode(this.feltPenStyleModel.getValue(), getFeltPenSize(size)));
      }
   }

   private static int getFeltPenSize(PencilSize size) {
      switch (size) {
         case THIN:
            return 0;
         case THICK1:
            return 1;
         case THICK2:
            return 2;
         case THICK3:
            return 3;
         case THICK4:
            return 4;
         default:
            throw new IllegalStateException();
      }
   }
}
