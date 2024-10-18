package de.jave.image2ascii;

import de.jave.image.GImage;
import de.jave.image.Rotation;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.util.Ensure;

public class ConversionJob {
   private final GGreyscaleImage originalImage;
   private final IImage2AsciiAlgorithm algorithm;
   private final int resultWidth;
   private final double shapeFactor;
   private final boolean normalize;
   private final boolean invert;
   private final int highlight;
   private final int shadow;
   private final double gamma;
   private final double sharpen;
   private final Rotation rotate;
   private final IProgressMonitor progressMonitor;
   private final IGreyscaleDithering dithering;

   public ConversionJob(
      IProgressMonitor progressMonitor,
      GGreyscaleImage image,
      int resultWidth,
      double shapeFactor,
      boolean normalize,
      boolean invert,
      int highlight,
      int shadow,
      double gamma,
      double sharpen,
      IGreyscaleDithering dithering,
      Rotation rotate,
      IImage2AsciiAlgorithm algorithm
   ) {
      Ensure.ensureArgumentNotNull(progressMonitor);
      this.progressMonitor = progressMonitor;
      this.originalImage = image;
      this.resultWidth = resultWidth;
      this.shapeFactor = shapeFactor;
      this.normalize = normalize;
      this.invert = invert;
      this.shadow = shadow;
      this.highlight = highlight;
      this.gamma = gamma;
      this.sharpen = sharpen;
      this.dithering = dithering;
      this.algorithm = algorithm;
      this.rotate = rotate;
   }

   public CharacterPlate run(ICancelable cancelable) throws InterruptedException {
      this.progressMonitor.beginTask("Preparing image...", 5);
      Thread.yield();
      ProgressUtilities.checkInterrupted(cancelable);
      if (this.originalImage != null && this.resultWidth != 0) {
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         Dimension newSize;
         if (this.rotate != Rotation.RIGHT && this.rotate != Rotation.LEFT) {
            newSize = this.algorithm.getImageSizeForSettings(this.originalImage.getSize(), this.resultWidth, this.shapeFactor);
         } else {
            Dimension imageSizeForSettings = this.algorithm
               .getImageSizeForSettings(
                  new Dimension(this.originalImage.getSize().height, this.originalImage.getSize().width), this.resultWidth, this.shapeFactor
               );
            newSize = new Dimension(imageSizeForSettings.height, imageSizeForSettings.width);
         }

         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         GGreyscaleImage image = this.originalImage.getScaledInstance(newSize.width, newSize.height);
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         this.progressMonitor.worked(1);
         image = (GGreyscaleImage)image.rotate(this.rotate);
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         this.progressMonitor.worked(1);
         image = image.convert(this.normalize, this.invert, this.gamma, this.highlight, this.shadow);
         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         this.progressMonitor.worked(1);
         if (this.sharpen > 0.0) {
            image = image.sharpen(this.sharpen);
         }

         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         this.progressMonitor.worked(1);
         GImage gi = image;
         if (this.algorithm.isMonochromeImageRequired() && this.dithering != null) {
            gi = this.dithering.dither(image);
         }

         Thread.yield();
         ProgressUtilities.checkInterrupted(cancelable);
         this.progressMonitor.worked(1);

         CharacterPlate cp;
         try {
            cp = this.algorithm.convert(gi, this.progressMonitor, cancelable);
         } finally {
            this.progressMonitor.done();
         }

         return cp;
      } else {
         return new CharacterPlate(0, 0);
      }
   }
}
