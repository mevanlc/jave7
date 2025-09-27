package de.jave.jave.actions.fileimport;

import java.util.regex.Pattern;

public class AsciimationImportHeuristic {
   private static final Pattern PATTERN = Pattern.compile("\\s*['`,.]?-+>.*<-+['`,.]?\\s*");

   public static void initialize(String[] lines, AsciimationOptionsModel asciimationOptionsModel) {
      int frameHeight = getScrollBarFrameHeight(lines);
      if (frameHeight != -1) {
         asciimationOptionsModel.initialize(frameHeight, frameHeight - 1, 0);
      }
   }

   public static int getScrollBarFrameHeight(String[] lines) {
      if (lines.length < 2) {
         return -1;
      } else if (!isScrollBarAnimationBoundaryLine(lines[0])) {
         return -1;
      } else {
         for (int i = 1; i < lines.length; i++) {
            if (isScrollBarAnimationBoundaryLine(lines[i])) {
               return i + 1;
            }
         }

         return -1;
      }
   }

   public static boolean isScrollBarAnimationBoundaryLine(String line) {
      return PATTERN.matcher(line).matches();
   }
}
