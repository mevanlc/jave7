package de.jave.jave.tool.ellipsealgorithmic;

import net.dizzy.commons.core.util.Ensure;

public class EllipseAlgorithmicOptions {
   private AlgorithmicEllipseStyle style = AlgorithmicEllipseStyle.LINE;

   public AlgorithmicEllipseStyle getStyle() {
      return this.style;
   }

   public void setStyle(AlgorithmicEllipseStyle style) {
      Ensure.ensureArgumentNotNull(style);
      this.style = style;
   }
}
