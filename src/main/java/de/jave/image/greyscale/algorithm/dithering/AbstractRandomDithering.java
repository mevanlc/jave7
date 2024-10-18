package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.GImage;
import java.util.Random;

public abstract class AbstractRandomDithering implements IGreyscaleDithering {
   private Random random;

   protected final void seed(GImage image) {
      this.random = new Random();
      this.random.setSeed((long) image.getWidth() * image.getHeight());
   }

   protected final int random(int modul) {
      return this.random.nextInt(modul);
   }
}
