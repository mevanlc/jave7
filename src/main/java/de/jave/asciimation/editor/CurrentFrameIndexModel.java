package de.jave.asciimation.editor;

import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.util.Ensure;

public class CurrentFrameIndexModel extends AbstractChangeableModel {
   private int currentFrameIndex;

   public void setCurrentFrameIndex(int currentFrameIndex) {
      Ensure.ensureTrue("Frame index must be >=0, was " + currentFrameIndex, currentFrameIndex >= 0);
      if (this.currentFrameIndex != currentFrameIndex) {
         this.currentFrameIndex = currentFrameIndex;
         this.fireChangeEvent();
      }
   }

   public int getCurrentFrameIndex() {
      return this.currentFrameIndex;
   }
}
