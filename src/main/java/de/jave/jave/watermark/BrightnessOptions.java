package de.jave.jave.algorithm;

import de.jave.image2ascii.AsciiGreyscaleTable;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.jave.JaveSelection;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class BrightnessOptions extends JaveAlgorithmOptions {
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;
   private int algorithm;
   private double factor;
   private AsciiGreyscaleTable greyscaleTable;
   private char ch;

   public BrightnessOptions(AsciiGreyscaleTableConfiguration greyscaleTableConfiguration) {
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
   }

   public void setAlgorithm(int algorithm) {
      this.algorithm = algorithm;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setFactor(double factor) {
      this.factor = factor;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setGreyscaleTable(AsciiGreyscaleTable greyscaleTable) {
      this.greyscaleTable = greyscaleTable;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setChar(char ch) {
      this.ch = ch;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public double getFactor() {
      return this.factor;
   }

   public int getAlgorithm() {
      return this.algorithm;
   }

   public AsciiGreyscaleTable getGreyscaleTable() {
      return this.greyscaleTable;
   }

   public char getChar() {
      return this.ch;
   }

   @Override
   public JaveAlgorithmOptionsPanel getPanel(FontModel displayFontModel) {
      return new BrightnessOptionsPanel(this, displayFontModel, this.greyscaleTableConfiguration);
   }

   @Override
   public void adjustTo(JaveSelection sel) {
      this.factor = 0.0;
   }
}
