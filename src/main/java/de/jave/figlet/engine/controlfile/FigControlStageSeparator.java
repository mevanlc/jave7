package de.jave.figlet.engine.controlfile;

import de.jave.figlet.engine.IFigConversionContext;

public class FigControlStageSeparator extends FigControlRule {
   @Override
   public int map(int charIn, IFigConversionContext context) {
      return -1;
   }

   @Override
   public String toString() {
      return "stage separator";
   }
}
