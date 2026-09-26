package de.jave.jave;

import java.awt.Font;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class ZoomableFontModel extends AbstractChangeableModel {
   // A -10 zoom offset at the default 13-point font must remain reachable.
   static final int MIN_FONT_SIZE = 1;
   static final int MAX_FONT_SIZE = 512;
   private final FontModel fontModel;
   private final BooleanModel autoZoomModel;
   private int sizeDelta = 0;
   private final IChangeListener fontModelChangeListener;

   public ZoomableFontModel(FontModel fontModel) {
      this(fontModel, 0);
   }

   public ZoomableFontModel(FontModel fontModel, int initialSizeDelta) {
      this(fontModel, initialSizeDelta, new BooleanModel(false));
   }

   public ZoomableFontModel(FontModel fontModel, int initialSizeDelta, BooleanModel autoZoomModel) {
      Ensure.ensureArgumentNotNull(fontModel);
      Ensure.ensureArgumentNotNull(autoZoomModel);
      this.fontModel = fontModel;
      this.autoZoomModel = autoZoomModel;
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
      this.autoZoomModel.setValue(false);
      this.sizeDelta--;
      this.assureSizeDeltaIsInRangeAndFireChangeEvent();
   }

   public void zoomIn() {
      this.autoZoomModel.setValue(false);
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

   void setAutoZoomDelta(int sizeDelta) {
      if (this.autoZoomModel.getValue() && this.sizeDelta != sizeDelta) {
         this.sizeDelta = sizeDelta;
         this.assureSizeDeltaIsInRangeAndFireChangeEvent();
      }
   }

   public void dispose() {
      this.fontModel.removeChangeListener(this.fontModelChangeListener);
   }
}
