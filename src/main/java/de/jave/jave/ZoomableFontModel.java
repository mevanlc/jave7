package de.jave.jave;

import java.awt.Font;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class ZoomableFontModel extends AbstractChangeableModel {
   private static final int MIN_FONT_SIZE = 6;
   private static final int MAX_FONT_SIZE = 512;
   private final FontModel fontModel;
   private int sizeDelta = 0;
   private final IChangeListener fontModelChangeListener;

   public ZoomableFontModel(FontModel fontModel) {
      this(fontModel, 0);
   }

   public ZoomableFontModel(FontModel fontModel, int initialSizeDelta) {
      Ensure.ensureArgumentNotNull(fontModel);
      this.fontModel = fontModel;
      this.sizeDelta = initialSizeDelta;
      this.fontModelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            ZoomableFontModel.this.assureSizeDeltaIsInRangeAndFireChangeEvent();
         }
      };
      fontModel.addChangeListener(this.fontModelChangeListener);
      this.assureSizeDeltaIsInRangeAndFireChangeEvent();
   }

   private void assureSizeDeltaIsInRangeAndFireChangeEvent() {
      int currentSize = this.fontModel.getFontSize() + this.sizeDelta;
      if (currentSize < MIN_FONT_SIZE) {
         this.sizeDelta = MIN_FONT_SIZE - this.fontModel.getFontSize();
      } else if (currentSize > MAX_FONT_SIZE) {
         this.sizeDelta = MAX_FONT_SIZE - this.fontModel.getFontSize();
      }

      this.fireChangeEvent();
   }

   public void zoomOut() {
      this.sizeDelta--;
      this.assureSizeDeltaIsInRangeAndFireChangeEvent();
   }

   public void zoomIn() {
      this.sizeDelta++;
      this.assureSizeDeltaIsInRangeAndFireChangeEvent();
   }

   public Font getFont() {
      Font font = this.getOriginalFont();
      return font.deriveFont((float)font.getSize() + (float)this.sizeDelta);
   }

   public Font getOriginalFont() {
      return this.fontModel.getFont();
   }

   public int getSizeDelta() {
      return this.sizeDelta;
   }

   public void dispose() {
      this.fontModel.removeChangeListener(this.fontModelChangeListener);
   }
}
