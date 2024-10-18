package de.jave.jave.actions.fileimport;

import net.disy.commons.core.model.AbstractChangeableModel;

public class AsciimationOptionsModel extends AbstractChangeableModel {
   private int skipTop = 0;
   private int frameHeight = 20;
   private int frameGap = 1;
   private int frameDuration = 66;

   public int getFrameGap() {
      return this.frameGap;
   }

   public int getFrameHeight() {
      return this.frameHeight;
   }

   public int getSkipTop() {
      return this.skipTop;
   }

   public void setFrameGap(int frameGap) {
      if (this.frameGap != frameGap) {
         this.frameGap = frameGap;
         this.fireChangeEvent();
      }
   }

   public void setFrameHeight(int frameHeight) {
      if (this.frameHeight != frameHeight) {
         this.frameHeight = frameHeight;
         this.fireChangeEvent();
      }
   }

   public void setSkipTop(int skipTop) {
      if (this.skipTop != skipTop) {
         this.skipTop = skipTop;
         this.fireChangeEvent();
      }
   }

   public int getFrameDuration() {
      return this.frameDuration;
   }

   public void setFrameDuration(int frameDuration) {
      if (this.frameDuration != frameDuration) {
         this.frameDuration = frameDuration;
         this.fireChangeEvent();
      }
   }

   public void initialize(int skipTop, int frameHeight, int frameGap) {
      this.skipTop = skipTop;
      this.frameHeight = frameHeight;
      this.frameGap = frameGap;
      this.fireChangeEvent();
   }
}
