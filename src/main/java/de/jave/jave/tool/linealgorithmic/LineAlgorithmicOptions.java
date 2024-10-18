package de.jave.jave.tool.linealgorithmic;

import net.disy.commons.core.util.Ensure;

public class LineAlgorithmicOptions {
   private AlgorithmicLineStyle style = AlgorithmicLineStyle.VERONICA;

   public AlgorithmicLineStyle getStyle() {
      return this.style;
   }

   public void setStyle(AlgorithmicLineStyle style) {
      Ensure.ensureArgumentNotNull(style);
      this.style = style;
   }
}
