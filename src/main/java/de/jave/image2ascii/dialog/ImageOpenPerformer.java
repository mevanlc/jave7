package de.jave.image2ascii.dialog;

import de.jave.image.ImageLoader;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.GreyscaleImageFactory;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import de.jave.image2ascii.model.SourceImageContainer;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.progress.INonInterruptableRunnableWithProgress;
import net.disy.commons.core.progress.IProgressMonitor;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.progress.ProgressMonitorDialog;

public class ImageOpenPerformer {
   private static final int PREVIEW_MAX_WIDTH = 128;
   private static final int PREVIEW_MAX_HEIGHT = 75;
   private final Image2AsciiSourceImageModel model;

   public ImageOpenPerformer(Image2AsciiSourceImageModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   public boolean performOpen(Component parentComponent, final File file) {
      final SourceImageContainer[] sourceImageContainer = new SourceImageContainer[1];
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(parentComponent, "Load image");

      try {
         dialog.run(new INonInterruptableRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
               monitor.beginTask("Loading image", -1);

               try {
                  sourceImageContainer[0] = ImageOpenPerformer.createSourceImageContainer(file);
               } catch (IOException var3) {
                  throw new InvocationTargetException(var3);
               }
            }
         });
      } catch (InvocationTargetException var7) {
         if (var7.getCause() instanceof IOException) {
            IOException ioException = (IOException)var7.getCause();
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("Image2Ascii", "Error loading image", MessageType.ERROR, ioException));
            return false;
         }

         if (var7.getCause() instanceof OutOfMemoryError) {
            OutOfMemoryError error = (OutOfMemoryError)var7.getCause();
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message("Image2Ascii", "Error loading image: The image file is too big.", MessageType.ERROR, error)
            );
            return false;
         }

         MessageDialogFactory.showMessageDialog(
            parentComponent,
            new Message(
               "Image2Ascii", "Error loading image: Wrong file format?\nSupported formats: GIF, JPG, BMP (experimental).", MessageType.ERROR, var7.getCause()
            )
         );
         return false;
      }

      this.model.set(file, sourceImageContainer[0]);
      this.model.setCurrentDirectory(file.getParentFile());
      return true;
   }

   private static SourceImageContainer createSourceImageContainer(File file) throws IOException {
      BufferedImage imageRaw = ImageLoader.loadImage(file);
      if (imageRaw == null) {
         throw new RuntimeException("Unable to read image");
      } else {
         int sourceImageWidth = imageRaw.getWidth();
         int sourceImageHeight = imageRaw.getHeight();
         if (sourceImageWidth > 0 && sourceImageHeight > 0) {
            double scaleX = 128.0 / (double)sourceImageWidth;
            double scaleY = 75.0 / (double)sourceImageHeight;
            double scale = Math.min(scaleX, scaleY);
            int previewWidth = (int)((double)sourceImageWidth * scale);
            int previewHeight = (int)((double)sourceImageHeight * scale);
            Image imageT;
            if (scale >= 1.0) {
               imageT = imageRaw;
               previewWidth = sourceImageWidth;
               previewHeight = sourceImageHeight;
            } else {
               imageT = imageRaw.getScaledInstance(previewWidth, previewHeight, 2);
            }

            int[] pixels = new int[previewWidth * previewHeight];
            PixelGrabber pg = new PixelGrabber(imageT, 0, 0, previewWidth, previewHeight, pixels, 0, previewWidth);

            try {
               pg.grabPixels();
            } catch (InterruptedException var20) {
               throw new RuntimeException("Internal Error: " + var20.toString());
            }

            if ((pg.getStatus() & 128) != 0) {
               throw new RuntimeException("Internal Error: Image fetch aborted or errored.");
            } else {
               GGreyscaleImage previewImageRaw = new GreyscaleImageFactory().createGreyscaleImage(pixels, previewWidth, previewHeight);
               int[] pixels2 = new int[sourceImageWidth * sourceImageHeight];
               PixelGrabber pg2 = new PixelGrabber(imageRaw, 0, 0, sourceImageWidth, sourceImageHeight, pixels2, 0, sourceImageWidth);

               try {
                  pg2.grabPixels();
               } catch (InterruptedException var19) {
                  throw new RuntimeException("Error: Unable to convert loaded image.", var19);
               }

               if ((pg2.getStatus() & 128) != 0) {
                  throw new RuntimeException("Error: Unable to convert loaded image.");
               } else {
                  GGreyscaleImage gImageRaw = new GreyscaleImageFactory().createGreyscaleImage(pixels2, sourceImageWidth, sourceImageHeight);
                  return new SourceImageContainer(gImageRaw, previewImageRaw, imageRaw);
               }
            }
         } else {
            throw new RuntimeException("Image file damaged or unable to open file.");
         }
      }
   }
}
