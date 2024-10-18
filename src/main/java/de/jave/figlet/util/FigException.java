package de.jave.figlet.util;

public class FigException extends Exception {
   public static final int UNDEFINED = -1;
   public static final int NO_BASE_DIR = 1;
   public static final int NOT_READABLE = 2;
   public static final int NO_FONTS_INSTALLED = 3;
   public static final int NO_CATEGORY_FILE = 4;
   public static final int NO_FONT_FILE = 7;
   public static final int CATEGORY_FILE_INVALID = 5;
   public static final int ERROR_LOADING_FONT = 6;
   private final int type;

   public FigException(String message) {
      this(message, -1);
   }

   public FigException(String message, int type) {
      this(message, null, type);
   }

   public FigException(String message, Throwable cause) {
      this(message, cause, -1);
   }

   public FigException(String message, Throwable cause, int type) {
      super(message, cause);
      this.type = type;
   }

   public int getType() {
      return this.type;
   }
}
