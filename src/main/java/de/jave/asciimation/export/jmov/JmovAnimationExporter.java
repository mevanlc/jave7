package de.jave.asciimation.export.jmov;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.persistence.JaveAnimationFileWriter;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class JmovAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private BufferedWriter bw;
   private Dimension frameSize;
   private AnimationMetaData metaData;

   public JmovAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   protected final Dimension getFrameSize() {
      return this.frameSize;
   }

   protected final AnimationExportOptions getOptions() {
      return this.options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.frameSize = maxFrameSize;
      this.metaData = metaData;
      this.bw = new BufferedWriter(new FileWriter(this.getJmovOutputFile()));
      JaveAnimationFileWriter.writeMetaDataTags(this.bw, metaData);
      JaveAnimationFileWriter.writeAnimationPropertiesTags(this.bw, animationProperties);
   }

   protected final String getTitle() {
      return this.metaData.getTitle();
   }

   protected File getJmovOutputFile() {
      return this.options.getFile();
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      this.bw.write("J:");
      this.bw.write(AsciiPacker.encode(content.getContent()));
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
