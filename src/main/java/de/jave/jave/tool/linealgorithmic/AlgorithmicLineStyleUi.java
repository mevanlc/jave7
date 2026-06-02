package de.jave.jave.tool.linealgorithmic;

import de.jave.jave.JaveMessages;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class AlgorithmicLineStyleUi extends AbstractObjectUi<AlgorithmicLineStyle> {
   public String getLabel(AlgorithmicLineStyle value) {
      switch (value) {
         case BUG:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorBug;
         case CEEJAY1:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorCeeJayNormal;
         case CEEJAY2:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorCeeJayAlternative;
         case CHARACTERS:
            return JaveMessages.Tool_LineAlgorithmic_Style_Characters;
         case GLORY:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorGlory;
         case SEGERMAN:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorSegerman;
         case VERONICA:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorVeronica;
         case NORMAL:
            return JaveMessages.Tool_LineAlgorithmic_Style_Normal;
         default:
            throw new IllegalArgumentException();
      }
   }

   @Override
   public String getToolTipText(AlgorithmicLineStyle value) {
      switch (value) {
         case NORMAL:
            return JaveMessages.Tool_LineAlgorithmic_Style_NormalTooltip;
         case VERONICA:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorVeronicaTooltip;
         case GLORY:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorGloryTooltip;
         case SEGERMAN:
            return JaveMessages.Tool_LineAlgorithmic_Style_AuthorSegermanTooltip;
         default:
            return null;
      }
   }
}
