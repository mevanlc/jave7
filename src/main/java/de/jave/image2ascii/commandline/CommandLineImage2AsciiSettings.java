package de.jave.image2ascii.commandline;

import de.jave.image.Rotation;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;

public class CommandLineImage2AsciiSettings {
   private String algorithmName = "JavE Algorithm (4/1)";
   private int resultWidth = 72;
   private final Rotation rotate = Rotation.NONE;
   private double shapeFactor = 1.0;
   private final boolean normalize = true;
   private boolean invert = false;
   private final double gamma = 1.0;
   private final double highlight = 100.0;
   private final double shadow = 0.0;
   private final double sharpen = 0.0;
   private final IGreyscaleDithering dithering = null;
   private String specialCharacters = null;
   private AsciiGreyscaleTable greyscaleTable;

   public CommandLineImage2AsciiSettings(AsciiGreyscaleTableConfiguration configuration) {
      this.greyscaleTable = configuration.getDefaultTable();
   }

   public void setResultWidth(int resultWidth) {
      this.resultWidth = resultWidth;
   }

   public void setAlgorithmName(String algorithmName) {
      this.algorithmName = algorithmName;
   }

   public void setShapeFactor(double shapeFactor) {
      this.shapeFactor = shapeFactor;
   }

   public void setSpecialCharacters(String specialCharacters) {
      this.specialCharacters = specialCharacters;
   }

   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
      this.greyscaleTable = greyscaleTable;
   }

   public String getAlgorithmName() {
      return this.algorithmName;
   }

   public String getSpecialCharacters() {
      return this.specialCharacters;
   }

   public AsciiGreyscaleTable getGreyscaleTable() {
      return this.greyscaleTable;
   }

   public int getResultWidth() {
      return this.resultWidth;
   }

   public Rotation getRotate() {
      return this.rotate;
   }

   public double getShapeFactor() {
      return this.shapeFactor;
   }

   public boolean isNormalize() {
      return true;
   }

   public boolean isInvert() {
      return this.invert;
   }

   public void setInvert(boolean invert) {
      this.invert = invert;
   }

   public double getGamma() {
      return 1.0;
   }

   public double getHighlight() {
      return 100.0;
   }

   public double getShadow() {
      return 0.0;
   }

   public double getSharpen() {
      return 0.0;
   }

   public IGreyscaleDithering getDithering() {
      return this.dithering;
   }
}
