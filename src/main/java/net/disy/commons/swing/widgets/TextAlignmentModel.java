package net.disy.commons.swing.model;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.text.TextAlignment;

public class TextAlignmentModel extends AbstractChangeableModel {
   private TextAlignment alignment;

   public TextAlignmentModel(TextAlignment alignment) {
      this.alignment = alignment;
   }

   public TextAlignment getTextAlignment() {
      return this.alignment;
   }

   public void setTextAlignment(TextAlignment alignment) {
      if (this.alignment != alignment) {
         this.alignment = alignment;
         this.fireChangeEvent();
      }
   }
}
