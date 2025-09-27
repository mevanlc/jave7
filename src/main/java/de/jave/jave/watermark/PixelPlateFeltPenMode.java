package de.jave.jave.pixelplate;

public class PixelPlateFeltPenMode extends PixelPlateMode {
   private final char character;
   private final int size;
   public static final PixelPlateFeltPenMode FELTPEN_8 = new PixelPlateFeltPenMode('8');
   public static final PixelPlateFeltPenMode FELTPEN_M = new PixelPlateFeltPenMode('M');
   public static final PixelPlateFeltPenMode FELTPEN_DOLLAR = new PixelPlateFeltPenMode('$');
   public static final PixelPlateFeltPenMode FELTPEN_COLON = new PixelPlateFeltPenMode(':');
   public static final PixelPlateFeltPenMode FELTPEN_EXCLAM = new PixelPlateFeltPenMode('!');
   public static final PixelPlateFeltPenMode FELTPEN_O = new PixelPlateFeltPenMode('O');
   public static final PixelPlateFeltPenMode FELTPEN_X = new PixelPlateFeltPenMode('X');

   private PixelPlateFeltPenMode(char character) {
      this(character, 1);
   }

   public PixelPlateFeltPenMode(char character, int size) {
      super("feltpen " + character);
      this.character = character;
      this.size = size;
   }

   public char getCharacter() {
      return this.character;
   }

   public int getSize() {
      return this.size;
   }

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
}
