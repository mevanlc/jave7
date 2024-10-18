package de.jave.jave.pixelplate;

public abstract class PixelPlateMode {
   public static final PixelPlateMode PIXEL = new PixelPlateMode("pixel") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.LINE;
      }

      @Override
      public int getRasterX() {
         return 3;
      }

      @Override
      public int getRasterY() {
         return 4;
      }
   };
   public static final PixelPlateMode CHAR = new PixelPlateMode("char") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.RAW;
      }

      @Override
      public int getRasterX() {
         return 1;
      }

      @Override
      public int getRasterY() {
         return 1;
      }
   };
   public static final PixelPlateMode TWO_BY_TWO = new PixelPlateMode("two by two") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.TWO_BY_TWO;
      }

      @Override
      public int getRasterX() {
         return 2;
      }

      @Override
      public int getRasterY() {
         return 2;
      }
   };
   public static final PixelPlateMode THREE_BY_TWO = new PixelPlateMode("three by two") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.THREE_BY_TWO;
      }

      @Override
      public int getRasterX() {
         return 2;
      }

      @Override
      public int getRasterY() {
         return 3;
      }
   };
   public static final PixelPlateMode THICK_THIN = new PixelPlateMode("thick thin") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.THICK;
      }

      @Override
      public int getRasterX() {
         return 3;
      }

      @Override
      public int getRasterY() {
         return 4;
      }
   };
   public static final PixelPlateMode DOT = new PixelPlateMode("dot") {
      @Override
      public PixelPlateConverterMode getConverterMode() {
         return PixelPlateConverterMode.DOT;
      }

      @Override
      public int getRasterX() {
         return 1;
      }

      @Override
      public int getRasterY() {
         return 2;
      }
   };
   private final String name;

   protected PixelPlateMode(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return "PixelPlateMode{" + this.name + "}";
   }

   public abstract PixelPlateConverterMode getConverterMode();

   public abstract int getRasterX();

   public abstract int getRasterY();
}
