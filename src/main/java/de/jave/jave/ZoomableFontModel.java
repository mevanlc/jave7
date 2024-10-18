package de.jave.jave;

import java.awt.Font;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class ZoomableFontModel extends AbstractChangeableModel {
   private static final int MIN_FONT_SIZE = 6;
   private static final int MAX_FONT_SIZE = 16;
   private final FontModel fontModel;
   private int sizeDelta = 0;
   private final IChangeListener fontModelChangeListener;

   public ZoomableFontModel(FontModel fontModel) {
      Ensure.ensureArgumentNotNull(fontModel);
      this.fontModel = fontModel;
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
      if (currentSize < 6) {
         this.sizeDelta = 6 - this.fontModel.getFontSize();
      } else if (currentSize > 16) {
         this.sizeDelta = 16 - this.fontModel.getFontSize();
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
