package de.jave.image2ascii;

import javax.swing.Icon;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class Image2AsciiAlgorithmItemUi extends AbstractObjectUi<IImage2AsciiAlgorithmItem> {
   public Icon getIcon(IImage2AsciiAlgorithmItem value) {
      IImage2AsciiAlgorithm algorithm = value.getAlgorithm();
      return algorithm.getIcon();
   }

   public String getLabel(IImage2AsciiAlgorithmItem value) {
      IImage2AsciiAlgorithm algorithm = value.getAlgorithm();
      return algorithm.getName();
   }
}
