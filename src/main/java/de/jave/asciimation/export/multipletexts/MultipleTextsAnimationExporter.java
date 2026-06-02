package de.jave.asciimation.export.multipletexts;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class MultipleTextsAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private int frameCount = 0;
   private int ciffers = 4;

   public MultipleTextsAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.ciffers = 1;
      frameCount--;

      while (frameCount >= 10) {
         frameCount /= 10;
         this.ciffers++;
      }
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(
            new FileWriter(this.options.getFileNameWithoutExtension() + "_" + TextTools.fillFront(String.valueOf(this.frameCount), '0', this.ciffers) + ".txt")
         );
         String[] lines = content.toStringArray();

         for (int i = 0; i < lines.length; i++) {
            writer.write(lines[i]);
            writer.newLine();
         }
      } finally {
         IOUtilities.close(writer);
      }

      this.frameCount++;
   }

   @Override
   public void finish() throws Exception {
   }

   @Override
   public void rollBack() {
   }
}
