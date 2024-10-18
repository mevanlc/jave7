package de.jave.jave.algorithm.replaceillegal;

import de.jave.jave.CharacterSets;
import de.jave.jave.JaveSelection;
import de.jave.lib.CharacterPlate;
import net.disy.commons.core.util.Ensure;

public class AsciiReplaceIllegal {
   private final AsciiReplaceIllegalConfiguration configuration;
   private final CharacterSets characterSets;

   public AsciiReplaceIllegal(AsciiReplaceIllegalConfiguration configuration, CharacterSets characterSets) {
      Ensure.ensureArgumentNotNull(configuration);
      Ensure.ensureArgumentNotNull(characterSets);
      this.configuration = configuration;
      this.characterSets = characterSets;
   }

   public final AsciiReplaceIllegalReport replaceIllegal(JaveSelection sel) {
      int illegalCharacterCount = 0;
      int replacedCharacterCount = 0;
      CharacterPlate cp = sel.getContent();
      int h = cp.getHeight();
      int w = cp.getWidth();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (sel.isActive(x, y) && cp.get(x, y) == 175 && sel.isActive(x, y - 1) && cp.get(x, y - 1) == ' ') {
               illegalCharacterCount++;
               replacedCharacterCount++;
               cp.setForce(x, y, ' ');
               cp.setForce(x, y - 1, '_');
            }
         }
      }

      char[] replaceIllegalSource = this.configuration.getReplaceIllegalSource();
      char[] replaceIllegalDestination = this.configuration.getReplaceIllegalDestination();

      for (int y = 0; y < h; y++) {
         for (int xx = 0; xx < w; xx++) {
            if (sel.isActive(xx, y)) {
               char ch = cp.get(xx, y);
               if (!this.characterSets.isLegal(ch)) {
                  illegalCharacterCount++;

                  for (int i = 0; i < replaceIllegalSource.length; i++) {
                     if (ch == replaceIllegalSource[i] && this.characterSets.isLegal(replaceIllegalDestination[i])) {
                        replacedCharacterCount++;
                        cp.setForce(xx, y, replaceIllegalDestination[i]);
                        break;
                     }
                  }
               }
            }
         }
      }

      return new AsciiReplaceIllegalReport(illegalCharacterCount, replacedCharacterCount);
   }
}
