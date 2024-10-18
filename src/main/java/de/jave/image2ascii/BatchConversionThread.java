package de.jave.image2ascii;

import de.jave.asciimation.export.IAnimationExporter;
import de.jave.image.GImage;
import de.jave.image.Rotation;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.GreyscaleImageFactory;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.IInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.progress.NullProgressMonitor;
import net.disy.commons.core.progress.ProgressUtilities;

public class BatchConversionThread implements IInterruptableRunnableWithProgress {
   private final File[] files;
   private final IImage2AsciiAlgorithm algorithm;
   private final int resultWidth;
   private final double shapeFactor;
   private final boolean normalize;
   private final boolean invert;
   private final int highlight;
   private final int shadow;
   private final double gamma;
   private final double sharpen;
   private final IGreyscaleDithering dithering;
   private final Rotation rotate;
   private final IAnimationExporter exporter;

   public BatchConversionThread(
      File[] files,
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
      IImage2AsciiAlgorithm algorithm,
      IAnimationExporter exporter
   ) {
      this.exporter = exporter;
      this.files = files;
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

   @Override
   public void run(IProgressMonitor monitor, ICancelable cancelable) throws InterruptedException, InvocationTargetException {
      monitor.beginTask("Batch converting...", this.files.length);

      try {
         for (int i = 0; i < this.files.length; i++) {
            ProgressUtilities.checkInterrupted(cancelable);
            monitor.subTask("Converting '" + this.files[i].getName() + "'");
            BufferedImage image = ImageIO.read(this.files[i]);
            int oldWidth = image.getWidth();
            int oldHeight = image.getHeight();
            int[] pixels2 = new int[oldWidth * oldHeight];
            PixelGrabber pg2 = new PixelGrabber(image, 0, 0, oldWidth, oldHeight, pixels2, 0, oldWidth);

            try {
               pg2.grabPixels();
            } catch (InterruptedException var13) {
               System.err.println("Internal Error: " + var13.toString());
               continue;
            }

            if ((pg2.getStatus() & 128) != 0) {
               System.err.println("Internal Error: image fetch aborted or errored");
            } else {
               GGreyscaleImage image2 = new GreyscaleImageFactory().createGreyscaleImage(pixels2, oldWidth, oldHeight);
               ProgressUtilities.checkInterrupted(cancelable);
               image2 = (GGreyscaleImage)image2.rotate(this.rotate);
               ProgressUtilities.checkInterrupted(cancelable);
               oldWidth = image2.getWidth();
               oldHeight = image2.getHeight();
               Dimension newSize = this.algorithm.getImageSizeForSettings(image2.getSize(), this.resultWidth, this.shapeFactor);
               ProgressUtilities.checkInterrupted(cancelable);
               image2 = image2.getScaledInstance(newSize.height, newSize.width);
               ProgressUtilities.checkInterrupted(cancelable);
               image2 = image2.convert(this.normalize, this.invert, this.gamma, this.highlight, this.shadow);
               ProgressUtilities.checkInterrupted(cancelable);
               if (this.sharpen > 0.0) {
                  image2 = image2.sharpen(this.sharpen);
               }

               ProgressUtilities.checkInterrupted(cancelable);
               GImage gi = image2;
               if (this.algorithm.isMonochromeImageRequired() && this.dithering != null) {
                  gi = this.dithering.dither(image2);
               }

               ProgressUtilities.checkInterrupted(cancelable);
               CharacterPlate result = this.algorithm.convert(gi, new NullProgressMonitor(), cancelable);
               if (i == 0) {
                  this.exporter
                     .init(new Dimension(result.getWidth(), result.getHeight()), new AnimationProperties(), this.files.length, new AnimationMetaData());
               }

               this.exporter.writeFrame(result);
               monitor.worked(1);
            }
         }

         this.exporter.finish();
      } catch (InterruptedException var14) {
         throw var14;
      } catch (OutOfMemoryError var15) {
         this.exporter.rollBack();
         throw new InvocationTargetException(new ImageConversionException("Error exporting file: The result file is too big.", var15));
      } catch (Throwable var16) {
         this.exporter.rollBack();
         throw new InvocationTargetException(new ImageConversionException("Error exporting file.", var16));
      }
   }
}
