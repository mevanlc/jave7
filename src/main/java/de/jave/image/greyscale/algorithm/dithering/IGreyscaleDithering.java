package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;

public interface IGreyscaleDithering extends IMonochromeDithering {
   GGreyscaleImage dither(GGreyscaleImage var1, int var2);
}
