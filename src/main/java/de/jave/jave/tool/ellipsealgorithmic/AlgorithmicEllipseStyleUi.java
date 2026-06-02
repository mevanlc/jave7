package de.jave.jave.tool.ellipsealgorithmic;

import de.jave.jave.JaveMessages;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class AlgorithmicEllipseStyleUi extends AbstractObjectUi<AlgorithmicEllipseStyle> {
   public String getLabel(AlgorithmicEllipseStyle value) {
      switch (value) {
         case CHARACTERS:
            return JaveMessages.Tool_EllipseAlgorithmic_StyleCharactersName;
         case LINE:
            return JaveMessages.Tool_EllipseAlgorithmic_StyleLineName;
         default:
            throw new IllegalArgumentException();
      }
   }
}
