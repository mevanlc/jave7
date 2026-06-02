package de.jave.asciimation.export.scrollbar;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class ScrollbarAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private BufferedWriter bw;
   private int frameCount = 0;
   private int frameHeight;

   public ScrollbarAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.frameHeight = maxFrameSize.height;
      this.bw = new BufferedWriter(new FileWriter(this.options.getFile()));
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      if (this.frameCount == 0) {
         writeScrollbarAnimationHeader(this.bw, this.frameHeight);
      }

      String[] lines = content.toStringArray();

      for (int i = 0; i < lines.length; i++) {
         this.bw.write(lines[i]);
         this.bw.newLine();
      }

      this.bw.newLine();
      this.frameCount++;
   }

   @Override
   public void finish() throws Exception {
      IOUtilities.close(this.bw);
   }

   @Override
   public void rollBack() {
      IOUtilities.close(this.bw);
   }

   private static void writeScrollbarAnimationHeader(BufferedWriter bw, int height) throws IOException {
      height += 2;
      if (height < 9) {
         int fuellzeilen = height - 2;
         bw.write(",--> Resize your browser window until this line is at the top and... <-.");
         bw.newLine();

         for (int j = 0; j < fuellzeilen; j++) {
            bw.write("|                                                                      |");
            bw.newLine();
         }

         bw.write("`-> ...this one is at the bottom. Hit page down repeatedly to scroll.<-'");
         bw.newLine();
      } else {
         int fuellzeilen = height - 9;
         int fuellzeilen1 = (fuellzeilen + 2) / 3;
         int fuellzeilen2 = (fuellzeilen - fuellzeilen1 + 1) / 2;
         int fuellzeilen3 = fuellzeilen - fuellzeilen1 - fuellzeilen2;
         bw.write("---> Resize your browser window until this line is at the top and... <--");
         bw.newLine();
         bw.write("] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [");
         bw.newLine();
         bw.write("------------------------------------------------------------------------");
         bw.newLine();

         for (int j = 0; j < fuellzeilen1; j++) {
            bw.write("    |                 |                 |                 |");
            bw.newLine();
         }

         bw.write("    |   Scrollbar     |   Scrollbar     |   Scrollbar     |   Scrollbar");
         bw.newLine();
         bw.write("    |    Animation    |    Animation    |    Animation    |    Animation");
         bw.newLine();

         for (int j = 0; j < fuellzeilen2; j++) {
            bw.write("    |                 |                 |                 |");
            bw.newLine();
         }

         bw.write("    |            (created using JavE " + JaveTitleProvider.JAVE_VERSION + " - http://www.jave.de)");
         bw.newLine();

         for (int j = 0; j < fuellzeilen3; j++) {
            bw.write("    |                 |                 |                 |");
            bw.newLine();
         }

         bw.write("------------------------------------------------------------------------");
         bw.newLine();
         bw.write("] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [] [");
         bw.newLine();
         bw.write("--> ...this one is at the bottom. Hit page down repeatedly to scroll.<--");
         bw.newLine();
      }
   }
}
