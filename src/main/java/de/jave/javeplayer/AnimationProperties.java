package de.jave.javeplayer;

import java.awt.Color;
import net.dizzy.commons.core.util.Ensure;

public class AnimationProperties {
   private int frameDuration = 66;
   private Color foregroundColor = Color.BLACK;
   private Color backgroundColor = Color.WHITE;

   public int getFrameDuration() {
      return this.frameDuration;
   }

   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public void setBackgroundColor(Color backgroundColor) {
      Ensure.ensureArgumentNotNull(backgroundColor);
      this.backgroundColor = backgroundColor;
   }

   public void setForegroundColor(Color foregroundColor) {
      Ensure.ensureArgumentNotNull(foregroundColor);
      this.foregroundColor = foregroundColor;
   }

   public void setFrameDuration(int frameDuration) {
      this.frameDuration = frameDuration;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof AnimationProperties)) {
         return false;
      } else {
         AnimationProperties other = (AnimationProperties)obj;
         return other.backgroundColor.equals(this.backgroundColor)
            && other.foregroundColor.equals(this.foregroundColor)
            && other.frameDuration == this.frameDuration;
      }
   }
}
