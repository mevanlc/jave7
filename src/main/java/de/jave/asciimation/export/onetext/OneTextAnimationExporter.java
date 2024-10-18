package de.jave.asciimation.export.onetext;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import net.disy.commons.core.io.IOUtilities;
import net.disy.commons.core.util.Ensure;

public class OneTextAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private BufferedWriter bw;

   public OneTextAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.bw = new BufferedWriter(new FileWriter(this.options.getFile()));
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      String[] lines = content.toStringArray();

      for (int i = 0; i < lines.length; i++) {
         this.bw.write(lines[i]);
         this.bw.newLine();
      }

      this.bw.newLine();
   }

   @Override
   public void finish() throws Exception {
      IOUtilities.close(this.bw);
   }

   @Override
   public void rollBack() {
      IOUtilities.close(this.bw);
   }
}
