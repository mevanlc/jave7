package net.disy.commons.swing.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.disy.commons.core.io.IOUtilities;

public class ImageLoader {
   public static Image getImageWithoutCaching(InputStream inputStream) throws IOException {
      return readImage(inputStream);
   }

   public static Image getMemoryImageWithoutCaching(InputStream inputStream) throws IOException {
      try {
         return createMemoryImage(readImage(inputStream));
      } catch (ImageLoader.LoadingException var2) {
         throw new IOException("Loading: " + var2.getMessage(), var2);
      }
   }

   public static Image readImage(InputStream inputStream) throws IOException {
      BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      IOUtilities.copyStream(bufferedInputStream, byteArrayOutputStream);
      return Toolkit.getDefaultToolkit().createImage(byteArrayOutputStream.toByteArray());
   }

   public static Image createMemoryImage(Image image) {
      ImageLoader.DimensionGetter dimensionGetter = new ImageLoader.DimensionGetter(image);

      int h;
      int w;
      try {
         w = dimensionGetter.getWidth();
         h = dimensionGetter.getHeight();
      } catch (Exception var8) {
         throw new ImageLoader.LoadingException("image missing or corrupted", var8);
      }

      int[] pixels = new int[w * h];
      PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);

      try {
         pixelGrabber.grabPixels();
      } catch (InterruptedException var7) {
         throw new ImageLoader.LoadingException("interrupted waiting for pixels!", var7);
      }

      if ((pixelGrabber.getStatus() & 128) != 0) {
         throw new ImageLoader.LoadingException("image fetch aborted or errored");
      } else {
         MemoryImageSource memoryImageSource = new MemoryImageSource(w, h, pixels, 0, w);
         return Toolkit.getDefaultToolkit().createImage(memoryImageSource);
      }
   }

   private static final class DimensionGetter implements ImageObserver {
      private final Image image;
      private boolean succeeded = false;

      DimensionGetter(Image image) {
         this.image = image;
      }

      @Override
      public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
         if ((infoflags & 64) == 64) {
            synchronized (this) {
               this.notifyAll();
               return false;
            }
         } else if (width != -1 && height != -1) {
            synchronized (this) {
               this.succeeded = true;
               this.notifyAll();
               return false;
            }
         } else {
            return true;
         }
      }

      int getWidth() {
         this.ensureLoaded();
         return this.image.getWidth(this);
      }

      int getHeight() {
         this.ensureLoaded();
         return this.image.getHeight(this);
      }

      private void ensureLoaded() {
         synchronized (this) {
            if (this.image.getWidth(this) == -1 || this.image.getHeight(this) == -1) {
               try {
                  this.wait();
               } catch (InterruptedException var4) {
               }
            }

            if (!this.succeeded) {
               throw new RuntimeException("error while loading image.");
            }
         }
      }
   }

   public static class LoadingException extends RuntimeException {
      private LoadingException(String message) {
         super(message);
      }

      private LoadingException(String message, Throwable cause) {
         super(message + " :" + cause.getMessage());
      }
   }
}
