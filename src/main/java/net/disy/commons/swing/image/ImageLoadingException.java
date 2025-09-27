package net.disy.commons.swing.image;

public class ImageLoadingException extends RuntimeException {
   public ImageLoadingException(String message) {
      super(message);
   }

    public ImageLoadingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ImageLoadingException(Throwable throwable) {
        super(throwable);
    }

    public ImageLoadingException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

    public ImageLoadingException() {
    }
}
