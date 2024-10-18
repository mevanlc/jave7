package de.jave.image.greyscale.algorithm.dithering;

import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.monochrome.GMonochromeImage;

public interface IMonochromeDithering {
   GMonochromeImage dither(GGreyscaleImage var1);

   String getName();
}
