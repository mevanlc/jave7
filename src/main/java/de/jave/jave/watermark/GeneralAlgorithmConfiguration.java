package de.jave.jave.algorithm;

import net.disy.commons.core.util.Ensure;

public class GeneralAlgorithmConfiguration {
   private final String mirror;
   private final String flip;
   private final String rotate180;
   private final String rotate90R;
   private final String rotate90L;

   public GeneralAlgorithmConfiguration(String mirror, String flip, String rotate180, String rotate90R, String rotate90L) {
      Ensure.ensureArgumentNotNull(mirror);
      Ensure.ensureArgumentNotNull(flip);
      Ensure.ensureArgumentNotNull(rotate180);
      Ensure.ensureArgumentNotNull(rotate90R);
      Ensure.ensureArgumentNotNull(rotate90L);
      this.mirror = mirror;
      this.flip = flip;
      this.rotate180 = rotate180;
      this.rotate90R = rotate90R;
      this.rotate90L = rotate90L;
   }

   public String getFlip() {
      return this.flip;
   }

   public String getMirror() {
      return this.mirror;
   }

   public String getRotate180() {
      return this.rotate180;
   }

   public String getRotate90Left() {
      return this.rotate90L;
   }

   public String getRotate90Right() {
      return this.rotate90R;
   }
}
